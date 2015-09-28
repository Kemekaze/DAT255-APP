var mongoose = require('mongoose'),
    Schema = mongoose.Schema;
var api = require('../api/api.js');
var exports = module.exports = {};

//
// Schemas definitions
//
var BusSchema = new Schema({
  dgw: { type: String },
  vin: { type: String },
  regnr: { type: String },
  mac: { type: String }
});

// assign a function to the "methods" object of our Schema
BusSchema.methods.getApiData = function (sensor, t1, t2, callback) {	
	  api.get(this.get("dgw"),sensor,t1,t2,function(data){
	  	callback(data);
	  });
}
var BusModel = exports.Model = mongoose.model('Busses', BusSchema);



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
exports.update = function(id,updateData,callback){
	BusModel.where({_id:id}).update(updateData, function (err, bus) {
	  if (err) return console.error(err);
	  callback(bus);
	});
}
