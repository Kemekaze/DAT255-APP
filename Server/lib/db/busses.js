var mongoose = require('mongoose'),
    Schema = mongoose.Schema;
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
    		stopsid: String
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
	  				depTime: String,
	  				depDate: String,
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
	var sensor = "Ericsson$Journey_Info",
  		t2     = (new Date).getTime(),
  		t1     = t2-23*1000;
	api.get(this.get("dgw"),sensor,t1,t2,function(journeyinfo){
		if(journeyinfo != ""){
			var updateData = getJourneyInfo(journeyinfo,"journey.ids.");
			console.log(updateData);
			exports.findOneAndUpdate({dgw:"Ericsson$"+journeyinfo[0].gatewayId},updateData,function(data){
		  		callback({status:"Bus journey id updated",data:data});
		    });
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
/*BusSchema.methods.updateJourney = function (callback) {	
	var sensor = "Ericsson$Journey_Info",
  		t2     = (new Date).getTime(),
  		t1     = t2-23*1000;
	api.get(this.get("dgw"),sensor,t1,t2,function(journeyinfo){
		if(journeyinfo != ""){
			var updateData = getJourneyInfo(journeyinfo,"journey.");
			exports.findOneAndUpdate({dgw:"Ericsson$"+journeyinfo[0].gatewayId},updateData,function(data){
		  		callback({status:"Bus journey updated",data:data});
		    });
		}else
			callback({status:"No journey data",data:null});			
	});

	function getJourneyInfo(journeyData, prefix){
		var journey = {};
		journeyData.reverse();
		journeyData.forEach(function(resource){
			if(journey.length == 2) return journey;
			var resourcename = ((resource.resourceSpec).substring(0,(resource.resourceSpec).length-6)).toLowerCase();
			if(resourcename == "journey_name") resourcename = resourcename.substring(8,resourcename.length);
			if(!(journey[prefix+resourcename] in journey))
				journey[prefix+resourcename] = resource.value;			
		});
		return journey;
	}
}*/
BusSchema.methods.updateJourney = function(stopsDepartures,callback){
	var gid = this.get("journey.id");
	var bus = this;
	
	var stopsDistancesSorted = distances(stopsDepartures);
	console.log(stopsDistancesSorted);
    for(var i = 0;i<stopsDistancesSorted.length;i++){
    	
    	
    }
		

	
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
	var sensor = "Ericsson$GPS2",
  		t2     = (new Date).getTime(),
  		t1     = t2-13*1000;
	api.get(this.get("dgw"),sensor,t1,t2,function(gpsApi){
		if(gpsApi != ""){
			var updateData = getApiGps(gpsApi,"gps.");
			exports.findOneAndUpdate({dgw:"Ericsson$"+gpsApi[0].gatewayId},updateData,function(data){
		  		callback({status:"GPS updated",data:data});
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
