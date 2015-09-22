var mongoose = require('mongoose'),
    Schema = mongoose.Schema;

var exports = module.exports = {};

//
// Schemas definitions
//
var PostSchema = new Schema({
  body: { type: String},
  user: { type: String},  
  comments: [{ body: String, date: Date }],
  time: { type: Date, default: Date.now },
  hidden: { type: Boolean},
  meta: {
    votes: {
    	up: { type: Number},
    	down: { type: Number}
    },
    Bus:{
    	mac: { type: String},
    	line: { type: Number}
    },
    type: { type: String}
  }

});

var PostModel = mongoose.model('Posts', PostSchema);

exports.find = function(key,value,callback){
	var query = {};
	query[key] = value;
	PostModel.find(query, function (err, p) {
	  if (err) return console.error(err);
	  callback(p);
	});
}
exports.findById = function(id,callback){
	PostModel.find({_id:id}, function (err, p) {
	  if (err) return console.error(err);
	  callback(p);
	});
}
exports.save = function(body,user,line,mac,callback){
	
	var post = new PostModel({
	      body: body,
		  user: user,  
		  comments: [],
		  time: (new Date).getTime(),
		  hidden: false,
		  meta: {
		    votes: {
		    	up: 0,
		    	down: 0
		    },
		    Bus:{
		    	mac: mac,
		    	line: line
		    },
    		type: "post"
		  }
	});
  	post.save(function (err, p) {
	  if (err) return console.error(err);
	  callback(p);
	});	
}
exports.voteUp = function(id,callback){
	PostModel.where({_id:id}).update({ $inc: { "meta.votes.up": 1 }}, function (err, p) {
	  if (err) return console.error(err);
	  callback(p);
	});
}
exports.voteDown = function(id,callback){
	PostModel.where({_id:id}).update({ $inc: { "meta.votes.down": 1 }}, function (err, p) {
	  if (err) return console.error(err);
	  callback(p);
	});
}
/*
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

exports.get = function(key, value, options, callback){
	// Get a collection
	var collection = db.get().collection(collectionName);
	//Create object
	var find = {};
	find[key] = (key == '_id')? new ObjectID(value) : value;
	//Get an object from collection
	collection.findOne(find, options, function(err, result) {
		//Test
		assert.equal(err, null);
		//callback
    	callback(result);
    });
	
}
// Example ()
exports.getMany = function(query,fields,skip,limit,callback){
	// Get a collection
	var collection = db.get().collection(collectionName);
	//Get all objects from the collection
	var options = {skip:skip, limit:limit};
	if(fields.length != 0) options.fields =fields;

	collection.find(query,options).toArray(function(err, result) {
		//Test
		assert.equal(err, null);
		//Callback
    	callback(result);
    });
	
}
*/