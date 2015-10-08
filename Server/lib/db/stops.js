var mongoose = require('mongoose'),
    Schema = mongoose.Schema;
var vt = require('../api/vasttrafik.js');

var exports = module.exports = {};

var StopSchema = new Schema({
  name: { type: String},
  lon: { type: String},
  lat: { type: String},  
  stopid: { type: String} 
});

var StopModel = exports.model = mongoose.model('Stops', StopSchema);

exports.save = function(name,lon,lat,stopid,callback){

	var stop = new StopModel({
	    name: name,
	    lon: lon,
	    lat: lat,
	    stopid: stopid
	});
  	stop.save(function (err, res) {
	  if (err) return console.error(err);
	  callback(res);
	});	
}

exports.find = function(query,callback){
	StopModel.find(query, function (err, stops) {
	  if (err) return console.error(err);
	  callback(stops);
	});
}
exports.findById = function(id,callback){
	StopModel.find({_id:id}, function (err, stop) {
	  if (err) return console.error(err);
	  callback(stop);
	});
}
exports.findAll = function(callback){

	StopModel.find(function (err, stops) {
	  if (err) return console.error(err);
	  callback(stops);
	});
}
exports.removeMany = function(query,callback){	
	StopModel.remove(query, function (err, stop) {
	  if (err) return console.error(err);
	  callback(stop);
	});
}
exports.remove = function(id,callback){
	StopModel.remove({_id:id}, function (err, stop) {
	  if (err) return console.error(err);
	  callback(stop);
	});
}
exports.findOneAndUpdate = function(query,updateData,callback){
	StopModel.findOneAndUpdate(query,updateData, function (err, stop) {
	  if (err) return console.error(err);
	  callback(stop);
	});
}
exports.update = function(id,updateData,callback){
	StopModel.where({_id:id}).update(updateData, function (err, stop) {
	  if (err) return console.error(err);
	  callback(stop);
	});
}
exports.getAllDepForAll = function(callback){
	exports.findAll(function(stops){
		var recieved = 0;
		var stopDepartures=[];
		for(var i = 0;i<stops.length;i++){        	
        	vt.get("departureBoard",{id:stops[i].get("stopid")},function(response){
        		recieved++;        		
        		var stop;
        		for(var s = 0;s<stops.length;s++){ 
        			if(stops[s].get("stopid") == response.input.id){
        				stop = stops[s];
        				break;
        			}         			
        		}
        		stopDepartures.push({stop:stop,deps:response.data.DepartureBoard.Departure});
        		//Primitive sync functionallity
        		if(recieved == stops.length){
		        	callback(stopDepartures);
		        }
        	});        	
        }
	});
}