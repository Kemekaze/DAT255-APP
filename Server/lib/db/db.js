//var MongoClient = require('mongodb').MongoClient;
var mongoose = require('mongoose');

var assert = require('assert');

var exports = module.exports = {};

var db = null;
var isConnected = false;
var url;

exports.isConnected = function(){
	return isConnected;
}
exports.get = function(){
	return db;
}
exports.url = function(){
	return url;
}
exports.connect = function(ip,port,dbName, callback){
    url = 'mongodb://'+ip+':'+port+'/'+dbName;	
	mongoose.connect(url);
	db = mongoose.connection;
	db.on('error', console.error.bind(console, 'connection error:'));
	db.once('open', function (callback) {
	  console.log("Database connection established.");
	});
}
exports.close = function(){
	mongoose.connection.close();
	console.log("Database connection closed.");
}


