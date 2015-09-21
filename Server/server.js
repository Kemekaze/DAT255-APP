var express = require('express');
var app = express();
var lib = require("./lib");
var db = lib.db;


//setup
//console.log(lib);



db.db.connect('localhost',27017,'App',function(){
	db.busses.remove('_id',"56005439cad562ac49d60e23",{}, function(result){
		console.log(result);
		db.db.closeConnection();
	});
});
