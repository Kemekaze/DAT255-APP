var mongoose = require('mongoose'),
    Schema = mongoose.Schema;
var moment = require('moment');

var api = require('../api/api.js');
var vt = require('../api/vasttrafik.js');
var stops = require('../db/stops.js');

var exports = module.exports = {};

//
// Schemas definitions
//
var BusSchema = new Schema({
    dgw: { type: String },
    vin: { type: String },
    regnr: { type: String },
    mac: { type: String },
    systemid: { type: String },
    journey: { 	  	
    	ids: { 
    		id: String,
    		stopsid: String,
    		serviceid: String
    	},
    	destination: { type: String},
    	stops : [{ 
	  	 			name: String, 
	  	 			stopid: String,
	  				lon: String,
	  				lat: String,
	  				routeIdx: String,
	  				arrTime: String,
	  				arrDate: String,
	  				rtArrTime: String,
	  				rtArrDate: String,
	  				depTime: String,
	  				depDate: String,
	  				rtDepTime: String,
	  				rtDepDate: String
	  				
  		}],
  		events:[{
	  				routeIdx: String,
					min1Stop: { type: Boolean, default: false},
					atStop: { type: Boolean, default: false},
					depStop: { type: Boolean, default: false}
		}], 
  		name: { type: String },
  		route:{
  			idxfrom: String,
  			idxto: String
  		}   
    },
    gps: {
    	longitude:{ type: String },
    	latitude:{ type: String },
    	speed:{ type: String },
    	course:{ type: String },
    	altitude:{ type: String }
    }

});

// assign a function to the "methods" object of our Schema
BusSchema.methods.getApiData = function (sensor, t1, t2, callback) {	
	  api.get(this.get("dgw"),sensor,t1,t2,function(data){
	  	callback(data);
	  });
}
BusSchema.methods.update = function (updateData, callback) {	
	  exports.update(this.get("id"),updateData,function(data){
	  		callback(data);
	  });
}
BusSchema.methods.updateJourneyId = function (callback) {
	var bus = this;	
	var sensor = "Ericsson$Journey_Info",
  		t2     = (new Date).getTime(),
  		t1     = t2-23*1000;
	api.get(this.get("dgw"),sensor,t1,t2,function(journeyinfo){
		if(journeyinfo != ""){
			var updateData = getJourneyInfo(journeyinfo,"journey.ids.");

			if(updateData["journey.ids.id"] != bus.journey.ids.id){				
				exports.findOneAndUpdate({dgw:"Ericsson$"+journeyinfo[0].gatewayId},updateData,function(data){
			  		callback({status:"Bus journey id for '"+bus.regnr+"' updated",data:data});
			    });
			}
			callback({status:"Bus journey id for '"+bus.regnr+"' no change",data:null});
		}else
			callback({status:"No journey data",data:null});			
	});

	function getJourneyInfo(journeyData, prefix){
		var journey = {};
		journeyData.reverse();
		journeyData.forEach(function(resource){
			var resourcename = ((resource.resourceSpec).substring(0,(resource.resourceSpec).length-6)).toLowerCase();
			if(resource.resourceSpec == "Journey_Name_Value"){
				journey[prefix+"id"] = vt.getGID(resource.value);	
			} 						
		});
		return journey;
	}
}
BusSchema.methods.resetEvents = function (callback) {	
	var bus = this;
	bus.journey.events = [];
	bus.save(function (err) {
	    if (!err) 
	    	callback({status:"Events reseted for'"+bus.regnr+"'",data:bus})
	});
}
BusSchema.methods.updateJourney = function(stops,callback){
	var bus = this;
	var gid = bus.get("journey.ids.id");
	var stopsSorted = distances(stops);

	//only 1 journey required to update, unneccessary to loop through entire array if found
	var foundJourney = false;
	if(gid !== undefined ){
		for(var s = 0, slen=stopsSorted.length; s < slen; s++){
			if(foundJourney == true) break;
			if(stopsSorted[s][0].deps === undefined) continue;

			for(var d = 0, dlen=(stopsSorted[s][0].deps).length; d < dlen; d++){				
				if(stopsSorted[s][0].deps[d].journeyid == gid ){
					
					//console.log("Bus %s found at %s",gid,stopsSorted[s][0].deps[d].stop);
					
					var direction = stopsSorted[s][0].deps[d].direction;

					vt.request(stopsSorted[s][0].deps[d].JourneyDetailRef.ref, function(journeyStops){
						if(journeyStops.JourneyDetail !== undefined){
							if(journeyStops.JourneyDetail.Stop !== undefined){
								var updateData=journeyStops.JourneyDetail.Stop;

								for(var i =0,len=updateData.length; i<len;i++ ){
									var stopid = updateData[i].id
									delete updateData[i].id;
									delete updateData[i].track;
									updateData[i]['stopid'] = stopid;
								}

								//update bus with new data				
								bus.journey.stops = updateData;
								bus.journey.name = direction;
								var serviceid = journeyStops.JourneyDetail.JourneyName.name;
								serviceid = serviceid.substring(4,serviceid.length)
								bus.journey.ids.serviceid = serviceid;

								var journeyid = journeyStops.JourneyDetail.JourneyId;
								bus.journey.route = {
												idxfrom: journeyid.routeIdxFrom,
					  							idxto: journeyid.routeIdxTo
								};

								bus.journey.ids.stopsid = journeyid.id; 
								// save updated data
								bus.save(function (err) {
								    if (!err) 
								    	callback({status:"Journey updated for bus '"+bus.regnr+"'",data:bus})
								});
							}
						}
					});
					foundJourney = true;
					break;

				}
			}
		}
	}
	if(!foundJourney) callback({status:"No journey found",data:null});

	function distances(stops){
		var stopsDistances = [];
		for (var i = 0; i < stops.length; ++i) {

            var distance =  distanceToStop(
			            	bus.get("gps.latitude"), 
			            	bus.get("gps.longitude"), 
			            	stops[i].stop.get("lat"), 
			            	stops[i].stop.get("lon") 
            	);
            stopsDistances.push([stops[i],distance]);
        }
        //Sort by distance from bus to stop
        stopsDistances.sort(function(a, b) {return a[1] - b[1]});
        return stopsDistances;

	}
	//Convert Degress to Radians
    function deg2Rad( deg ) {
       return deg * Math.PI / 180;
    }
    function distanceToStop( lat1, lon1, lat2, lon2 ){
	    lat1 = deg2Rad(lat1);
	    lat2 = deg2Rad(lat2);
	    lon1 = deg2Rad(lon1);
	    lon2 = deg2Rad(lon2);
	    var R = 6371; // km
	    var x = (lon2-lon1) * Math.cos((lat1+lat2)/2);
	    var y = (lat2-lat1);
	    var d = Math.sqrt(x*x + y*y) * R;
	    return d;
    }

}

BusSchema.methods.updateGPS = function (callback) {	
	var bus = this;
	var sensor = "Ericsson$GPS2",
  		t2     = (new Date).getTime(),
  		t1     = t2-13*1000;
	api.get(this.get("dgw"),sensor,t1,t2,function(gpsApi){
		if(gpsApi != ""){
			var updateData = getApiGps(gpsApi,"gps.");
			exports.findOneAndUpdate({dgw:"Ericsson$"+gpsApi[0].gatewayId},updateData,function(data){
		  		callback({status:"GPS updated for bus '"+bus.regnr+"'",data:data});
		    });
		}else
			callback({status:"No GPS data",data:null});			
	});
	function getApiGps(gpsApi,prefix){
		// reverse array for latest values incase of duplicate
		gpsApi.reverse(); 
		var gps ={};
		gpsApi.forEach(function(resource){
			if(gps.length == 5) return gps;
			var resourcename = prefix+((resource.resourceSpec).substring(0,(resource.resourceSpec).length-7)).toLowerCase();
			if(!(gps[resourcename] in gps))
				gps[resourcename] = resource.value;			
		});
		return gps;

	}
	  
}
BusSchema.methods.nextStop = function (callback) {	
	var bus = this;
	var stops = bus.journey.stops;	
    var nextStop = getNextStop(stops);
    callback({stop:nextStop,bus:bus});
	
	function getNextStop(stops){
		var now = moment();

		for(var i = 0, slen = stops.length;i< slen;i++){
			var timeStop = null;
			if(typeof stops[i].rtArrTime === 'undefined') {				
				if(typeof stops[i].arrTime === 'undefined' ){
					timeStop = stops[i].depTime
				}else{
					timeStop = stops[i].arrTime 
				}				
			}else{
				timeStop =stops[i].rtArrTime;
			}
			
			var h = parseInt(timeStop.substring(0,2));
			var m = parseInt(timeStop.substring(3,5));
			var rtArrTime = moment().hours(h).minutes(m).seconds(0);
			if(now.isBefore(rtArrTime)){
				return {
					name: stops[i].name,
					time: rtArrTime,
					serviceid: bus.journey.ids.serviceid,
					systemid: bus.systemid,
					stopid: stops[i].stopid,
					routeidx: stops[i].routeIdx
				};
				break;
			}
		}
		return null;
	}	  
}
BusSchema.methods.getServiceid = function () {	
	var bus = this;
	return bus.get("journey.ids.serviceid"); 
}
BusSchema.methods.getGPS = function () {	
	var bus = this;
	return bus.get("gps"); 
}
BusSchema.methods.findEvent = function (routeidx,callback) {
	var bus = this;	
	var eve  = null;
	bus.journey.events.forEach(function(ev){
		if(ev.routeIdx == routeidx) eve = ev;
	});
	callback(eve)
}
BusSchema.methods.saveEvent = function (ev,callback) {
	var bus = this;
	bus.journey.events.push(ev);
	bus.save(function (err, ev) {
	  if (err) return console.error(err);
	  callback(ev);
	});
}
BusSchema.methods.updateEvent = function (ev,updateData,callback) {	
	ev.update(updateData,function (err, bus) {
	  if (err) return console.error(err);
	  callback(bus);
	});
}


var BusModel = exports.model = mongoose.model('Busses', BusSchema);

exports.save = function(dgw,vin,regnr,mac,callback){

	var bus = new BusModel({
	    dgw: dgw,
	    vin: vin,
	    regnr: regnr,
	    mac: mac
	});
  	bus.save(function (err, res) {
	  if (err) return console.error(err);
	  callback(res);
	});	
}

exports.find = function(query,callback){
	BusModel.find(query, function (err, bus) {
	  if (err) return console.error(err);
	  callback(bus);
	});
}

exports.findById = function(id,callback){
	BusModel.find({_id:id}, function (err, p) {
	  if (err) return console.error(err);
	  callback(p);
	});
}
exports.findAll = function(callback){

	BusModel.find(function (err, busses) {
	  if (err) return console.error(err);
	  callback(busses);
	});
}
exports.removeMany = function(query,callback){	
	BusModel.remove(query, function (err, bus) {
	  if (err) return console.error(err);
	  callback(bus);
	});
}
exports.remove = function(id,callback){
	BusModel.remove({_id:id}, function (err, bus) {
	  if (err) return console.error(err);
	  callback(bus);
	});
}
exports.findOneAndUpdate = function(query,updateData,callback){
	BusModel.findOneAndUpdate(query,updateData, function (err, bus) {
	  if (err) return console.error(err);
	  callback(bus);
	});
}
exports.update = function(id,updateData,callback){
	BusModel.where({_id:id}).update(updateData, function (err, bus) {
	  if (err) return console.error(err);
	  callback(bus);
	});
}
exports.getGpsAll = function(callback){
	var gpsData=[];
	exports.findAll(function(buses){
		buses.forEach(function(bus){
			gpsData.push({
				id: bus.get("id"),
				systemid :bus.systemid,
				gps : bus.gps
			});
		});
		callback(gpsData);
	})
}
