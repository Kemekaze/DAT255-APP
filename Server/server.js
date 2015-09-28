var express = require('express');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var socketIO = require('socket.io');
var http = require('http');

var lib = require("./lib");


//setup
console.log(lib);
var SERVER_PORT   = 3000;
var DB_URL   = "localhost";
var DB_PORT   = 27017;
var DB_NAME   = "App";
var clients= [];
var client = {}
var numUsers = 0;


//Database


//url = 'mongodb://localhost:27017/App';	
mongoose.connect('mongodb://'+DB_URL+':'+DB_PORT+'/'+DB_NAME);
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function (callback) {
  console.log("Database '"+DB_NAME+"' connected at '"+DB_URL+":"+DB_PORT+"'" );
  console.log("--------------- SERVER BOOTED ---------------");
  //db.close();
});





var app = express();
var http = http.Server(app);
var io = socketIO(http);


function checkAuthToken(token ,callback){
	console.log("Token: '"+token+"'");
	callback(false,true);
}



app.get('/',function(req,res){
	res.sendFile(__dirname + '/testConnection.html');
	console.log("User on '/' ");
});
io.on('connection', function(socket){ 
	console.log("Connected: '"+socket.id); 

	socket.on('authenticate', function(data){
	    //check the auth data sent by the client
	    checkAuthToken(data.token, function(err, success){
	      if (!err && success){
	        console.log("Authenticated socket ", socket.id);
	        socket.auth = true;
	        clients.push(socket);
	      }

	    });
	  });
	 
	  setTimeout(function(){
	    //If the socket didn't authenticate, disconnect it
	    if (!socket.auth) {
	      console.log("Disconnecting socket ", socket.id);
	      socket.disconnect('unauthorized');
	    }
	  }, 1000);
	  
	socket.on('disconnect', function () { 
		console.log("Disconnected: '"+socket.id);
	    var i = clients.indexOf(socket);
	    clients.splice(i,1);
	});

	//POSTS
	socket.on('getPosts', function (options) {
		
		//get default params incase not sent and check for correct value
		var query = (typeof(options[0]) == 'object')? options[0]: {},
			limit = (typeof(options[1]) == 'number' && options[1] != 0)? options[1]: 10,
			skip  = (typeof(options[2]) == 'number')? options[2]: 0,
			sort  = (typeof(options[3]) == 'object')? options[3]: {};
	  	console.log(JSON.stringify(query)+" : "+limit+" : "+skip+" : "+JSON.stringify(sort));
	  	lib.db.posts.find(query,limit,skip,sort,function(posts){
	  		console.log("Returning: "+posts.length+" posts");
	  		socket.emit('posts', posts);
	  	});

	});
	socket.on('getPost', function (data) {
		var post_id = data.post_id;
		// validation here

	  	lib.db.posts.findById(post_id,function(post){
	  		console.log("Returning post with id: "+post_id);
	  		socket.emit('post', post);
	  	});

	});
	socket.on('savePost', function (data) {
		var user = data.user;
		var body = data.body;
		var line = data.line;
		var mac  = data.mac;
		// validation here

		lib.db.posts.save(function(post){
			console.log("Post saved with id: "+post.get("id"));
	  		socket.emit('postSaved', comments);

		});

	});
	socket.on('getComments', function (data) {
		var post_id = data.post_id;
		// validation here

	  	lib.db.posts.getAllComments(post_id,function(comments){
	  		console.log("Returning comments for post id: "+post_id);
	  		socket.emit('comments', comments);
	  	});
	});
	socket.on('saveComment', function (data) {
		var post_id = data.post_id;	
		var user    = data.user;
		var body    = data.body;
		// validation here

	  	lib.db.posts.saveComment(post_id,user,body,function(comment){
	  		console.log("Comment saved for post id: "+post_id);
	  		socket.emit('commentSaved', comment);
	  	});
	});



	

 });

http.listen(SERVER_PORT,function(){
	console.log("Server listening at port %d",SERVER_PORT);
});






