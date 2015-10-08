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
	//update all busses on startup so the api is not overloaded with requests
	exports.updateBusesJourneyId(false);
	setTimeout(exports.updateBusesJourney, 2000);

	console.log("-----UPDATEING BUSES GPS DATA-----");
	setInterval(exports.updateBusesGPS, 5*1000);
	console.log("-----UPDATEING BUSES JOURNEY DATA-----");
	setInterval(exports.updateBusesJourneyId, 5*1000);
	setInterval(exports.updateBusesJourney, 30*1000);

	/*db.busses.find({dgw:"Ericsson$171234"},function(bus){
		bus[0].nextStop(function(ns){
			console.log(ns);
		});
	});*/

	
}

var updateBusesJourneyIdCount = 0;
var updateBusesJourneyCount = 0;
var updateBusesGPSCount = 0;

var updateBusesJourneyIdProgress = 0;
var updateBusesJourneyProgress = 0;
var updateBusesGPSProgress = 0;

exports.updateBusesJourneyId = function(updateJourney){
	updateJourney = typeof updateJourney === 'undefined' ? false : updateJourney;
	console.log("Journey ids update count : %s",updateBusesJourneyIdCount);
	updateBusesJourneyIdCount = 0;
	db.busses.findAll(function(buses){
		buses.forEach(function(bus){
			bus.updateJourneyId(function(data){
				if(data.data != null){
					console.log(data.status);
					updateBusesJourneyIdCount++;	
					if(updateJourney){		
						exports.updateBusJourney(bus,function(){
							if(data.data != null){
								console.log(data.status);
							}
						});
					}					
					
				}
			});

		});		
	});
}
exports.updateBusJourney = function(bus){
	console.log("Updating journey for %s",bus.get("regnr"));
	db.stops.getAllDepForAll(function(stopsDepartures){
		bus.updateJourney(stopsDepartures,function(data){
			if(data.data != null){
				console.log(data.status);
			}
		});		
	});	
}

exports.updateBusesJourney = function(){
	console.log("Journeys update count : %s",updateBusesJourneyCount);
	updateBusesJourneyCount = 0;
	db.busses.findAll(function(buses){
		db.stops.getAllDepForAll(function(stopsDepartures){
			buses.forEach(function(bus){
				bus.updateJourney(stopsDepartures,function(data){
					if(data.data != null){
						console.log(data.status);
						updateBusesJourneyCount++;
					}
				});								
			});
		});
	});
}

exports.updateBusesGPS = function(){
	console.log("GPS update count : %s",updateBusesGPSCount);
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

exports.nextStop = function(systemid,callback){
	db.busses.find({systemid:systemid},function(bus){
		bus[0].nextStop(function(stop){
			db.posts.saveAny("Next stop "+stop.name,"Alert",systemid,stop.serviceid,stop.time,"EventNextStop",function(post){
				callback({
					post:post, 
					systemid:systemid
				})
			});
		});
	});
	
}