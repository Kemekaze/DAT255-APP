var express = require('express');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var socketIO = require('socket.io');
var http = require('http');

var lib = require("./lib");


//setup
//console.log(lib);
var SERVER_PORT   = 3000;
var DB_URL   = "localhost";
var DB_PORT   = 27017;
var DB_NAME   = "App";
var socketIds= [];
var socketId = {}
var numUsers = 0;


//Database


//url = 'mongodb://localhost:27017/App';	
mongoose.connect('mongodb://'+DB_URL+':'+DB_PORT+'/'+DB_NAME);
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function (callback) {
  console.log("Database '"+DB_NAME+"' connected at '"+DB_URL+":"+DB_PORT+"'" );
  //db.close();
});
/*lib.db.posts.find({},3,0,{date: -1},function(posts){
	console.log(posts)
});
lib.db.posts.save("body","user",55,"mac",function(post){

	console.log(post);
});*/



var app = express();
var server = http.createServer(app);
var io = socketIO(server);

server.listen(SERVER_PORT,function(){
	console.log("Server listening at port %d",SERVER_PORT);
});


var checkAuthToken = function(){

}
app.get('/',function(req,res){
	res.send("hello");
	console.log("User on '/' ");
});
io.on('connection', function(socket){ 
	console.log("User with id '"+socket.id+"' connected.");
	/*socket.on('authenticate', function(data){
	    //check the auth data sent by the client
	    checkAuthToken(data.token, function(err, success){
	      if (!err && success){
	        console.log("Authenticated socket ", socket.id);
	        socket.auth = true;
	      }
	    });
	  });
	 
	  setTimeout(function(){
	    //If the socket didn't authenticate, disconnect it
	    if (!socket.auth) {
	      console.log("Disconnecting socket ", socket.id);
	      socket.disconnect('unauthorized');
	    }
	  }, 1000);*/
	  
	
	socket.on('get posts', function (options) {

	  	//options bestämer dessa parametrar {},5,0,{date: -1}
	  	lib.db.posts.find({},5,0,{date: -1},function(posts){
	  		socket.emit('posts', posts);
	  	});

	});

 });




