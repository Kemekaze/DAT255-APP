var db = require('./db.js');
var assert = db.assert;

var exports = module.exports = {};

//variables
var collectionName = "Busses";


exports.add = function(values,callback){
	// Get a collection
	var collection = db.getDB().collection(collectionName);
	var obj ={dgw : values[0], vin : values[1], regnr : values[2], mac : values[3]};
	//Add an object to the collection
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
// Example (56002a1f0d8866283c804741,{dgw:123,vin:323})
exports.update = function(id, updateData, callback){
	// Get a collection
	var collection = db.getDB().collection(collectionName);
	//Create object id
	var o_id = new db.ObjectID(id);
	//Update an object in the collection
	collection.updateOne({_id: o_id},{$set: updateData}, function(err, result) {
		//Test
	    assert.equal(err, null);
	    assert.equal(1, result.matchedCount);
	    console.log("Updated id: "+id+" in "+collectionName+" collection ");
	    //Callback
	    callback(result);
	  }); 
	
}
// Example ("dgw","Ericsson$171328",{limit:0,})
exports.get = function(key, value, options, callback){
	// Get a collection
	var collection = db.getDB().collection(collectionName);
	//Create object
	var find = {};
	find[key] = (key == '_id')? new db.ObjectID(value) : value;
	//Get an object from collection
	collection.findOne(find, options, function(err, result) {
		//Test
		assert.equal(err, null);
		//callback
    	callback(result);
    });
	
}
// Example ()
exports.getAll = function(callback){
	// Get a collection
	var collection = db.getDB().collection(collectionName);
	//Get all objects from the collection
	collection.find({}).toArray(function(err, result) {
		//Test
		assert.equal(err, null);
		//Callback
    	callback(result);
    });
	
}
exports.remove = function(key,value,options,callback){
	// Get a collection
	var collection = db.getDB().collection(collectionName);
	//Create object
	var find = {};
	find[key] = (key == '_id')? new db.ObjectID(value) : value;
	//Remove from collection
	collection.removeOne(find, options, function(err, result) {
		//Test
		assert.equal(null, err);
        assert.equal(1, result.result.n);
		//callback
    	callback(result);
    });
}
