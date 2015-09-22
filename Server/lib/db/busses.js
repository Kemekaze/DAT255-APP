var mongoose = require('mongoose'),
    Schema = mongoose.Schema;

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

var BusModel = mongoose.model('Busses', BusSchema);

exports.save = function(dgw,vin,regnr,mac,callback){

	var bus = new BusModel({
	    dgw: dgw,
	    vin: vin,
	    regnr: regnr,
	    mac: mac
	});
  	bus.save(function (err, bus) {
	  if (err) return console.error(err);
	  callback(bus);
	});	
}

exports.find = function(key,value,callback){
	var query = {};
	query[key] = value;
	BusModel.find(query, function (err, bus) {
	  if (err) return console.error(err);
	  callback(bus);
	});
}
exports.findOne = function(key,value,callback){
	var query = {};
	query[key] = value;
	BusModel.findOne(query, function (err, bus) {
	  if (err) return console.error(err);
	  callback(bus);
	});
}
exports.findAll = function(callback){

	BusModel.find(function (err, busses) {
	  if (err) return console.error(err);
	  callback(busses);
	});
}
exports.removeMany = function(key,value,callback){
	var query = {};
	query[key] = value;
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
