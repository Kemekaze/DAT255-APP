var mongoose = require('mongoose'),
    Schema = mongoose.Schema;

var exports = module.exports = {};

//
// Schemas definitions
//
var PostSchema = new Schema({
  body: { type: String},
  user: { type: String},  
  comments: [{ 
  	 			body: String, 
  	 			user: String,
  				date: Number 
  			}],
  date: { type: Number},
  hidden: { type: Boolean},
  meta: {
    votes: {
    	up: { type: Number},
    	down: { type: Number}
    },
    bus:{
    	systemid: { type: Number},
    	line: { type: Number}
    },
    type: { type: String}
  }

});
PostSchema.methods.saveComment = function(body,user,callback){
	exports.saveComment(this.get("id"),body,user,callback);	
} 
PostSchema.methods.getComments = function(callback){
	exports.getAllComments(this.get("id"),callback);
} 
var PostModel = exports.model = mongoose.model('Posts', PostSchema);

exports.find = function(query,limit,skip,sort,callback){

	PostModel.find(query).limit(limit).skip(skip).sort(sort).exec(function (err, p) {
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
		  date: (new Date).getTime(),
		  hidden: false,
		  meta: {
		    votes: {
		    	up: 0,
		    	down: 0
		    },
		    bus:{
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
exports.saveComment = function(post_id,body,user,callback){
	var comment ={
		body: body, 
  	 	user: user,
  		date: (new Date).getTime()
	};
	PostModel.where({_id:post_id}).update({ $push: { comments: comment }}, function (err, p) {
	  if (err) return console.error(err);
	  callback(p);
	});

} 
exports.getComments = function(post_id,callback){
	exports.findById(post_id,function(post){
		callback(post.comments);
	})

} 