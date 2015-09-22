var MongoClient = require('mongodb').MongoClient;
var assert = require('assert');

var exports = module.exports = {};

var db = null;
var isConnected = false;

exports.isConnected = function(){
	return isConnected;
}
exports.get = function(){
	return db;
}
exports.connect = function(ip,port,dbName, callback){
    var url = 'mongodb://'+ip+':'+port+'/'+dbName;
    console.log("Url: "+ url);
	MongoClient.connect(url, function(err, _db) {
	  //test
      assert.equal(null, err);

	  db = _db;
	  isConnected = true;
	  assert.equal(null, err);
	  console.log("Database connection established.");
	  callback();
	});
}
exports.close = function(){
	db.close();
	console.log("Database connection closed.");
}


