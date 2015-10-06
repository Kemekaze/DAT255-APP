var express = require('express');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var socketIO = require('socket.io');
var http = require('http');

var lib = require("./lib");

var exports = module.exports = {};
//setup
console.log(lib);
var SERVER_PORT   = 3000;
var DB_URL   = "localhost";
var DB_PORT   = 27017;
var DB_NAME   = "App";

var clients = {};
var clients_total= 0;
var buses = [];

//Database
	
mongoose.connect('mongodb://'+DB_URL+':'+DB_PORT+'/'+DB_NAME);
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function (callback) {
  lib.db.busses.findAll(function(data){
  	data.forEach(function(bus){
  			var systemid = bus.get("systemid");
  			clients[systemid] = [];
  			buses.push(systemid);
  			exports.buses = buses;
  	});  	
  });
  console.log("Database '"+DB_NAME+"' connected at '"+DB_URL+":"+DB_PORT+"'" );
  console.log("--------------- SERVER BOOTED ---------------");
});

//test västraffik api
lib.api.vasttrafik.get("location.name","chalmers",function(data){
	console.log(data.LocationList.servertime);	
})




var app = express();
var http = http.Server(app);
var io = socketIO(http);




function checkAuthToken(token ,callback){
	console.log("Token: '"+token+"'");
	callback(false,true);
}
//EVENTS
function events(){
	console.log("EVENTS");

	//this should be places somewhere else
	for (var i = 0; i < buses.length; i++) {

		if(clients[buses[i]].length == 0){
			continue;
		} 
		console.log("Fetching next stop for bus '%s'",buses[i]);
		lib.events.nextStop(buses[i],function(nextStop){
			console.log("Sending next stop to %s clients for bus '%s'",clients[nextStop.bus].length, nextStop.bus);
			clients[nextStop.bus].forEach(function(socketid){
				socketid.emit("getBusNextStop",nextStop.post);
			});
		});
	};
	
}

setInterval(events, 5000);


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
	            console.log("Authenticated: ", socket.id);
	            socket.auth = true;

	            var bus_id = socket.bus_id = data.bus_id;
	            clients[bus_id].push(socket);
	            clients_total++;

	            console.log("Clients: "+ clients_total);
	            socket.emit("authorized");
	        }
	    });
	});
	
	//disconnecting if not authenticated
    setTimeout(function(){        
        if (!socket.auth) {
            console.log("Disconnecting socket ", socket.id);
            socket.disconnect('unauthorized');
        }
    }, 1000);
	  

	socket.on('disconnect', function () { 
		console.log("Disconnected: '"+socket.id);
		if(socket.auth == true){
		    var i = clients[socket.bus_id].indexOf(socket);
		    clients[socket.bus_id].splice(i,1);
		    clients_total--;
		}
	    console.log("Clients: "+ clients_total);
	});

	//POSTS
	socket.on('getPosts', function (data) {
		if(data == null) data = {}
		var query = (data.query === undefined || typeof(data.query) != 'object')? {} : data.query, 
			limit = (data.limit === undefined || typeof(data.limit) != 'number')? 10 : data.limit,
			skip  = (data.skip === undefined || typeof(data.skip) != 'number')? 0 : data.skip,
			sort  = (data.sort === undefined || typeof(data.sort) != 'object')? {date: -1} : data.sort;
		

		
		console.log(JSON.stringify(query)+" | "+limit+" | "+skip+" | "+JSON.stringify(query));	
	  	lib.db.posts.find(query,limit,skip,sort,function(posts){
	  		console.log("Returning: "+posts.length+" posts");
	  		var type = (skip == 0)? 1: 0;
	  		var p={posts :posts,type:type};  
	  		socket.emit('getPosts', p);
	  	});

	});
	socket.on('getPost', function (data) {
		var post_id = data.post_id;
		// validation here

	  	lib.db.posts.findById(post_id,function(post){
	  		console.log("Returning post with id: "+post_id);
	  		socket.emit('getPost', post);
	  	});

	});
	socket.on('savePost', function (data) {
		console.log('savePost');
		
		var user = "Anon",
		    body = data.body,
		    line = 0,
		    mac  = socket.mac;
		

		lib.db.posts.save(body,user,line,mac,function(post){
			console.log("Post saved with id: "+post.get("id"));
			console.log("Post : "+JSON.stringify(post));
	  		socket.emit('savePost', {status:"ok",post:post});
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
	//votes
	socket.on('voteUp', function (data) {
		var post_id = data.post_id;
		// validation here

	  	lib.db.posts.voteUp(post_id,function(post){
	  		console.log("Up voted post id: "+post_id);
	  		socket.emit('voteUp', {status:"1"});
	  	});

	});
	socket.on('voteDown', function (data) {
		var post_id = data.post_id;
		// validation here

	  	lib.db.posts.voteDown(post_id,function(post){
	  		console.log("Down voted post id: "+post_id);
	  		socket.emit('voteDown', {status:"1"});
	  	});

	});

 });

http.listen(SERVER_PORT,function(){
	console.log("Server listening at port %d",SERVER_PORT);
});






