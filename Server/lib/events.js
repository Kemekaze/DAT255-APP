var exports = module.exports = {};
var api = require('./api').api;
var vt = require('./api').vasttrafik;
var db = require('./db');

exports.nextStop = function(bus,callback){
	//console.log("Event nextStop: "+bus);
	db.busses.find({systemid:bus},function(db_bus){
		var dgw    = db_bus[0].get("dgw"),
		  	sensor = "Ericsson$Next_Stop",
		  	t2     = (new Date).getTime(),
		  	t1     = t2- 5*60*1000; 

	
		api.get(dgw,sensor,t1,t2,function(data){
			var post = new db.posts.model({
			    body: "Next stop "+data[data.length-1].value,
				user: "System",  
				comments: [],
				date: (new Date).getTime()+60*1000,//tiden det sskall ta 
			    hidden: false,
				meta: {
				    votes: {
				    	up: 0,
				    	down: 0
				    },
				    bus:{
				    	systemid: 0,
				    	line: 0
				    },
		    		type: "event"
				}
			});
			return callback({
					post:post, 
					bus:bus
				});
		});

		
	});
	
}


exports.beginUpdateBuses = function(){
	console.log("-----BEGIN UPDATEING BUSES-----");
	
	console.log("-----UPDATEING BUSES GPS DATA-----");
	setInterval(exports.updateBusesGPS, 5*1000);
	console.log("-----UPDATEING BUSES JOURNEY DATA-----");
	setInterval(exports.updateBusesJourneyId, 5*1000);
	//setInterval(exports.updateBusesJourney, 5*1000);
}

var updateBusesJourneyIdCount = 0;
exports.updateBusesJourneyId = function(callback){
	console.log("Journey ids update count : %s",updateBusesJourneyIdCount)
	updateBusesJourneyIdCount = 0;
	db.busses.findAll(function(buses){
		buses.forEach(function(bus){
			bus.updateJourneyId(function(data){
				if(data.data != null){
					updateBusesJourneyIdCount++;
				}
			});

		});		
	});
}
var updateBusesJourneyCount = 0;
exports.updateBusesJourney = function(callback){
	console.log("Journeys update count : %s",updateBusesJourneyCount)
	updateBusesJourneyCount = 0;
	db.busses.findAll(function(buses){
		db.stops.getAllDepForAll(function(stopsDepartures){
			buses.forEach(function(bus){
				bus.updateJourney(stopsDepartures,function(data){
					if(data.data != null){
						updateBusesJourneyCount++;
					}
				});								
			});
		});
	});
}
var updateBusesGPSCount = 0;
exports.updateBusesGPS = function(callback){
	console.log("GPS update count : %s",updateBusesGPSCount)
	updateBusesGPSCount = 0;
	db.busses.findAll(function(buses){
		buses.forEach(function(bus){
			bus.updateGPS(function(data){
				if(data.data != null){
					updateBusesGPSCount++;
				}
			});
		});		
	});
}





/*exports.updateBusesJourney(function(){

});*/