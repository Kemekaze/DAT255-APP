var moment = require('moment');
var log = require('single-line-log').stdout;

var exports = module.exports = {};
var api = require('./api').api;
var vt = require('./api').vasttrafik;
var db = require('./db');
var socketEvents = require('../server.js').socket.events;
var server = require('../server.js');


var updateBusesJourneyIdCount = 0;
var updateBusesJourneyCount = 0;
var updateBusesGPSCount = 0;

var JOUNEY_UPDATE_TIME = 30;
var JOUNEY_IDS_UPDATE_TIME = 5;
var GPS_UPDATE_TIME = 5;
var EVENT_NEXTSTOP_TIME = 10;

exports.beginUpdateBuses = function(){
	//setInterval(updateConsole, 1000);
	//console.log("-----BEGIN UPDATEING BUSES-----");

	//update all busses on startup so the api is not overloaded with requests
	exports.updateBusesJourneyId();
	setTimeout(exports.updateBusesJourney, 2000);

	//console.log("-----UPDATEING BUSES GPS DATA-----");
	setInterval(exports.updateBusesGPS, GPS_UPDATE_TIME*1000);
	//console.log("-----UPDATEING BUSES JOURNEY DATA-----");
	setInterval(exports.updateBusesJourneyId, JOUNEY_IDS_UPDATE_TIME*1000);
	setInterval(exports.updateBusesJourney, JOUNEY_UPDATE_TIME*1000);

	//console.log("-----NEXTSTOP SOCKET EVENTS-----");
	setInterval(exports.eventBusesNextStop, EVENT_NEXTSTOP_TIME*1000);
	
}


var counter=0;
function updateConsole(){
	counter++;
	var idsTime = JOUNEY_IDS_UPDATE_TIME-(counter%JOUNEY_IDS_UPDATE_TIME);
	var jTime   = JOUNEY_UPDATE_TIME-(counter%JOUNEY_UPDATE_TIME);
	var gpsTime = GPS_UPDATE_TIME-(counter%GPS_UPDATE_TIME);
	log("-----------UPDATEING BUSES STATUS------------\n"+
		" Journey id's: "+updateBusesJourneyIdCount+"/"+server.totalBuses+" updated. "+
		"Updateing in  "+idsTime+" seconds.\n"+
		" GPS:          "+updateBusesGPSCount+"/"+server.totalBuses+" updated. "+
		"Updateing in  "+gpsTime+" seconds.\n"+
		" Journeys :    "+updateBusesJourneyCount+"/"+server.totalBuses+" updated. "+
		"Updateing in  "+jTime+" seconds.\n"+
		"---------------------------------------------"
	);
}

exports.updateBusesJourneyId = function(){
	updateBusesJourneyIdCount = 0;


	db.busses.findAll(function(buses){
		updateBusesJourneyIdCountTotal = buses.length;
		buses.forEach(function(bus){
			bus.updateJourneyId(function(data){
				if(data.data != null){
					//console.log(data.status);
					updateBusesJourneyIdCount++;
					bus.resetEvents(function(rEv){
						//console.log(rEv.status);
					});					
				}
			});

		});		
	});
}
exports.updateBusJourney = function(bus){


	db.stops.getAllDepForAll(function(stopsDepartures){
		bus.updateJourney(stopsDepartures,function(data){
			if(data.data != null){
				//console.log(data.status);
			}
		});		
	});	
}

exports.updateBusesJourney = function(){
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

exports.updateBusesGPS = function(){
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
			db.posts.saveAny("Next stop "+stop.name,"Alert",systemid,stop.serviceid,stop.time.unix(),"EventNextStop",function(post){
				callback({
					post:post, 
					systemid:systemid
				})
			});
		});
	});
	
}
exports.eventBusesNextStop = function(callback){
	db.busses.findAll(function(buses){
		buses.forEach(function(bus){
			bus.nextStop(function(nstop){
				if(nstop.stop != null){					
					bus.findEvent(nstop.stop.routeidx,function(ev){
						if(nstop.stop.time.diff(moment(), 'seconds') <= 60){
							if(ev != null){
								if(ev.min1Stop == false){
									ev.min1Stop = true;
									nstop.bus.save(function (err, res) {
										if (err) console.error(err);
										emitEvent(nstop);								    
									});
								}									
							}
							else{
								bus.saveEvent({routeIdx: nstop.stop.routeidx, min1Stop: true},function(evS){
									emitEvent(nstop);
								});
							}
						}
						
					});

				}				
			});
		});
	});
	function emitEvent(nstop){
		socketEvents.nextStop({
			post: db.posts.newModel(""+nstop.stop.name+" \n"+moment().to(nstop.stop.time) ,"Next stop",nstop.stop.systemid,nstop.stop.serviceid,(new Date).getTime(),"event"), 
			post_alt: db.posts.newModel("Your stop, "+nstop.stop.name+" \nis "+moment().to(nstop.stop.time) ,"Next stop",nstop.stop.systemid,nstop.stop.serviceid,(new Date).getTime(),"event"), 
			systemid: nstop.stop.systemid,
			stopid: nstop.stop.stopid,
			bus:nstop.bus
		});
	}	
}
