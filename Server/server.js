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
  //db.close();
});
/*lib.db.posts.find({},3,0,{date: -1},function(posts){
	console.log(posts)
});
lib.db.posts.save("body","user",55,"mac",function(post){

	console.log(post);
});*/



var app = express();
var http = http.Server(app);
var io = socketIO(http);





app.get('/',function(req,res){
	res.sendFile(__dirname + '/testConnection.html');
	console.log("User on '/' ");
});
io.on('connection', function(socket){ 

	console.log("Connected: '"+socket.id);

	clients.push(socket);//add client to array 
	//NOTE this will be done in authentication 

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
		//get default params incase not sent and check for correct value
		var query = (typeof(options[0]) == 'object')? options[0]: {},
			limit = (typeof(options[1]) == 'number')? options[1]: 10,
			skip  = (typeof(options[2]) == 'number')? options[2]: 0,
			sort  = (typeof(options[3]) == 'object')? options[3]: {};
	  	console.log(query+" : "+limit+" : "+skip+" : "+sort);
	  	lib.db.posts.find(query,limit,skip,sort,function(posts){
	  		socket.emit('posts', posts);
	  	});

	});
	socket.on('disconnect', function () { 
		console.log("Disconnected: '"+socket.id);
	    var i = clients.indexOf(socket);
	    clients.splice(i,1);
	  });

 });

http.listen(SERVER_PORT,function(){
	console.log("Server listening at port %d",SERVER_PORT);
});






