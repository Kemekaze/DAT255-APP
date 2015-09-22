var db = require('./db.js');
var assert = require('assert');
var ObjectID = require('mongodb').ObjectID;


var exports = module.exports = {};
//variables
var collectionName = "Posts";

exports.add = function(values,callback){
	// Get a collection
	var collection = db.get().collection(collectionName);

	var obj ={dgw : values[0], vin : values[1], regnr : values[2], mac : values[3]};
	collection.insert(obj, function(err, result) {	
		//Test
		assert.equal(err, null);
		assert.equal(1, result.result.n);
    	assert.equal(1, result.ops.length);
    	console.log("Inserted 1 item into "+collectionName+" collection");
    	//Callback
	    callback(result);
	    }
	);
}
