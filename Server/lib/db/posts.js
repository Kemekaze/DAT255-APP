var db = require('./db.js');
var assert = require('assert');
var ObjectID = require('mongodb').ObjectID;


var exports = module.exports = {};
//variables
var collectionName = "Posts";
/*
{
  "_id": {
    "$oid": "234234234234234234df"
  },
  "text": "Lorem ipsum dolor sit amet, consectetur adipiscing elit.  ",
  "user": "Bengt",
  "time": "1442931349",
  "bus_service": 55
  "votes":{
	"up": 56,
	"down": 34
  },
  "type": ["Post",""]
  
}

*/
exports.add = function(text,user,bus_service,callback){
	// Get a collection
	var collection = db.get().collection(collectionName);

	var obj ={
		text: text, 
		user: user, 
		bus_service : bus_service, 
		time : (new Date).getTime(),
		votes : {
			up:0,
			down:0
		},
		type : [
			"Post"
		]
	};

	collection.insert(obj, function(err, result) {	
		//Test
		assert.equal(err, null);
		assert.equal(1, result.result.n);
    	assert.equal(1, result.ops.length);
    	console.log("Inserted 1 post into "+collectionName+" collection");
    	//Callback
	    callback(result);
	    }
	);
}
exports.voteUp = function(id,callback){
	// Get a collection
	var collection = db.get().collection(collectionName);
	//Create Object id for id
	var o_id = new ObjectID(id);

	collection.updateOne({_id: o_id},{$inc: {"votes.up":1}}, function(err, result) {	
		//Test
		assert.equal(err, null);
		assert.equal(1, result.result.n);
    	console.log("Upvoted post with id: "+id);
    	//Callback
	    callback(result);
	    }
	);
}
exports.voteDown = function(id,callback){
	// Get a collection
	var collection = db.get().collection(collectionName);
	//Create Object id for id
	var o_id = new ObjectID(id);

	collection.updateOne({_id: o_id},{$inc: {"votes.up":-1}}, function(err, result) {	
		//Test
		assert.equal(err, null);
		assert.equal(1, result.result.n);
    	console.log("Upvoted post with id: "+id);
    	//Callback
	    callback(result);
	    }
	);
}

