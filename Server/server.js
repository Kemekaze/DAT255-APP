var express = require('express');
var app = express();
var lib = require("./lib");
var db = lib.db;


//setup
console.log(lib);



db.con.connect('localhost',27017,'App',function(){
	db.posts.voteUp("5601654a5a0880e4084f9981",function(result){
		console.log(result);
		db.con.close();
	});
});
