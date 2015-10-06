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


exports.updateBusJourney = function(callback){
	console.log("UPDATEING BUSES");
	db.busses.findAll(function(buses){
			var sensor = "Ericsson$Journey_Info",
		  		t2     = (new Date).getTime(),
		  		t1     = t2-15*60*1000; 
		buses.forEach(function(bus){
			
			var dgw  = bus.get("dgw");
			if( dgw == "Ericsson$171164" || 
				dgw == "Ericsson$171235" ||
				dgw == "Ericsson$171328" ||
				dgw == "Ericsson$171329") return;
			api.get(dgw,sensor,t1,t2,function(data){
				if(data != ""){
					console.log("Length: " + data.length);
					getLastResourceSpec(data,function(cb){
						
						if(cb.name != null && cb.dest != null){
							var updateData ={};
							var dgw =null;
							if(cb.name != null){
								updateData['journey.name'] = cb.name.value;
								dgw = "Ericsson$"+cb.name.gatewayId;
							}
							if(cb.dest != null){
								updateData['journey.destination'] = cb.dest.value;
								dgw = "Ericsson$"+cb.dest.gatewayId;
							}							
							console.log(updateData);
							db.busses.findOneAndUpdate(
								{dgw:dgw},
								{ $set: updateData},
								function(res){

								}
							);
						}
						
						
					});
					
					
				}else{
					//console.log("Null");
				}
			});

		});
		
	});
	

}
function getLastResourceSpec(journeyArray, callback){
	var journeyNameValue = null;
	var destinationValue = null;
	for (var i = journeyArray.length-1; i > 0; i--) {
		 if(journeyNameValue != null && destinationValue != null) break;

		 if(journeyArray[i].resourceSpec == 'Journey_Name_Value'){
		 	journeyNameValue = journeyArray[i];
		 }
		 if(journeyArray[i].resourceSpec == 'Destination_Value'){ 
            destinationValue = journeyArray[i];
		 }
	}
	return callback({name:journeyNameValue,dest:destinationValue});
}
//updated busses every 10 minutes
setInterval(exports.updateBusJourney, 1*60*1000);

