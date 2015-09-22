var express = require('express');
var app = express();
var lib = require("./lib");
var db = lib.db;


//setup
console.log(lib);



db.con.connect('localhost',27017,'App',function(){
	db.busses.get('_id',"5600275e0100867438d7d4e6",{}, function(result){
		console.log(result);
		db.con.close();
	});
});
