var express = require('express');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var socketIO = require('socket.io');
var http = require('http');
var exports = module.exports = {socket:{events:{}}};
var lib = require("./lib");



//setup
//console.log(lib);
var SERVER_PORT   = 3000;
var DB_URL   = "localhost";
var DB_PORT   = 27017;
var DB_NAME   = "App";

var clients = {};
var clients_total= 0;
var buses_total;
var webGuiclients = [];


//Database
	
mongoose.connect('mongodb://'+DB_URL+':'+DB_PORT+'/'+DB_NAME);
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function (callback) {
  lib.db.busses.findAll(function(data){
  	buses_total = exports.totalBuses = data.length;
  	data.forEach(function(bus){
  			var systemid = bus.get("systemid");
  			clients[systemid] = [];

  	});  	
  });
  console.log("Database '"+DB_NAME+"' connected at '"+DB_URL+":"+DB_PORT+"'" );
  console.log("--------------- SERVER BOOTED ---------------");
});

lib.events.beginUpdateBuses();




var app = express();
var http = http.Server(app);
var io = socketIO(http);

lib.db.posts.schema.post('save', function(next) {
  console.log("wopwop");
  updateWebGui();

});
function updateWebGui(){
	for (var i = 0; i < webGuiclients.length; i++) {
		webGuiclients[i].emit("updatePostFeed");
	};
}


function checkAuthToken(token ,callback){
	console.log("Token: '"+token+"'");
	callback(false,true);
}
//Socket EVENTS

exports.socket.events.nextStop = function(data){
    if(clients[data.systemid].length != 0){
    	console.log("Sending next stop to %s clients for bus '%s'",clients[data.systemid].length, data.bus.regnr);
	    var busSocket = clients[data.systemid];
		busSocket.forEach(function(socketid){
				socketid.emit("getBusNextStop",data.post);
		});
	}
}


app.get('/',function(req,res){
	res.sendFile(__dirname + '/web/dashboard/index.html');
});
app.get('/buses',function(req,res){
	res.sendFile(__dirname + '/web/dashboard/buses.html');
});
app.get('/posts',function(req,res){
	res.sendFile(__dirname + '/web/dashboard/index.html');
});
app.get('/surveys',function(req,res){
	res.sendFile(__dirname + '/web/dashboard/index.html');
});
app.use('/assets', express.static(__dirname + '/web/dashboard/assets/'));

io.on('connection', function(socket){ 
	console.log("Connected: '"+socket.id); 
	
	socket.on('authenticate', function(data){
	    //check the auth data sent by the client
	    checkAuthToken(data.token, function(err, success){
	        if (!err && success){
	            console.log("Authenticated: ", socket.id);
	            socket.auth = true;
	            if(data.web === undefined){
	            	var bus_id = socket.bus_id = data.bus_id;
		            clients[bus_id].push(socket);
		            clients_total++;
	            }else{
	            	socket.web = true;
	            	webGuiclients.push(socket);
	            }
	            

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
		if(socket.auth == true ){
			if(socket.web === undefined){
			    var i = clients[socket.bus_id].indexOf(socket);
			    clients[socket.bus_id].splice(i,1);
			    clients_total--;
			}else{
				var i = webGuiclients.indexOf(socket);
			    webGuiclients.splice(i,1);
			}
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
		var user = (data.user == undefined)? 'Anonymus':data.user,
		    body = data.body,
		    systemid  = socket.bus_id;

		lib.db.posts.save(body,user,systemid,function(post){
			console.log("Post saved with id: "+post.get("id"));
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

	socket.on('incVotesUp', function (data) {
		var post_id = data.post_id;
		// validation here

	  	lib.db.posts.incVotesUp(post_id,function(post){
	  		console.log("Increased up votes id: "+post_id);
	  		socket.emit('incVotesUp', {status:"1"});
	  	});

	});
	socket.on('incVotesDown', function (data) {
		var post_id = data.post_id;
		// validation here

	  	lib.db.posts.incVotesDown(post_id,function(post){
	  		console.log("Increased down votes posts id: "+post_id);
	  		socket.emit('incVotesDown', {status:"1"});
	  	});

	});
	socket.on('decVotesUp', function (data) {
		var post_id = data.post_id;
		// validation here

	  	lib.db.posts.decVotesUp(post_id,function(post){
	  		console.log("Decreased up votes post id: "+post_id);
	  		socket.emit('decVoteUp', {status:"1"});
	  	});

	});
	socket.on('decVotesDown', function (data) {
		var post_id = data.post_id;
		// validation here

	  	lib.db.posts.decVotesDown(post_id,function(post){
	  		console.log("Decreased down votes post id: "+post_id);
	  		socket.emit('decVoteDown', {status:"1"});
	  	});

	});



	socket.on('getBusGPS', function (data) {
		var bus_id = data.bus_id;
	  	lib.db.busses.find({systemid:bus_id},function(bus){
	  		socket.emit('getBusGPS', {gps:bus[0].getGPS(),status:1});
	  	});

	});
	socket.on('getBusesGPS', function (data) {
		lib.db.busses.getGpsAll(function(gpsData){
			console.log("getBusesGPS "+gpsData.length);
			socket.emit('getBusesGPS', {gps:gpsData,status:1});
		});	 
	});
	socket.on('getStops', function (data) {
		lib.db.stops.findAll(function(stops){
			console.log("getStops "+stops.length);
			socket.emit('getStops', {stops:stops,status:1});
		});	 
	});
	//Primary for web ui
	socket.on('getTotalPostCount', function (data) {
		console.log('getPostCount');
		lib.db.posts.countTotal(function(count){
			socket.emit('getTotalPostCount', {count:count});
		});
	});
	socket.on('getClientCount', function (data) {
		console.log('getClientCount');
		var count=0;
		for(var bus in clients){
			for(var client in clients[bus]){
				count++;
			}
		}
		socket.emit('getClientCount', {count:count});
		
	});
	socket.on('saveSurvey', function (data) {
		console.log('saveSurvey');
		console.log(JSON.stringify(data));
		var data = data.survey;	
		var survey = lib.db.posts.newSurvey(data.question,data.user,data.options,data.answers);
	  	survey.save(function (err, su) {
		  if (err) return console.error(err);
		  console.log(JSON.stringify(su));	
		});	
	});
	socket.on('updatePost', function (data, fn) {	
		var updateData = {};
		updateData[data.name] = data.value;
		var post_id = data.pk;
		fn({status:'ok'});
	  	lib.db.posts.findOneAndUpdate(post_id,updateData,function(p){
	  		
	  	});
	});
	socket.on('removePost', function (data) {
		var post_id = data.post_id;
				// validation here

	  	lib.db.posts.remove(post_id,function(comments){
	  		console.log("Removed post "+post_id);
	  		socket.emit('removePost', {status:1,data:null});
	  	});
	});
	socket.on('updateSurvey', function (data) {
		console.log('updateSurvey');
		console.log(JSON.stringify(data));
		var option = data.option,
			post_id = data.post_id;

		lib.db.posts.addVoteSurvey(post_id,option,function(comments){
			console.log("Voted survey "+post_id);
	  		socket.emit('updateSurvey', {status:1,data:null});
	  	})		
	});
	socket.on('getBusesInfo', function (data) {
	  	lib.db.busses.findAll(function(buses){
	  		var stops=[];
			var count=0;
		  	lib.db.busses.findAll(function(buses){
		  		buses.forEach(function(bus){
					bus.nextStop(function(stop){
						count++;
						stops.push(stop);
						if(count == buses.length){
							socket.emit('getBusesInfo', {
					  			stops: stops,
					  			status:1
					  		});
						}						
					});
				});
		  		
		  	});
	  		
	  	});
	});
	

 });

http.listen(SERVER_PORT,function(){
	console.log("Server listening at port %d",SERVER_PORT);
});






