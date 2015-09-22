var express = require('express');
var app = express();
var lib = require("./lib");
var db = lib.db;


//setup
console.log(lib);

console.log("Server2.js startad.");

//TidsVariabler i millisekunder
var t2 = (new Date).getTime();
var t1 = t2 - (1000 *120);

//Testar att använda api.get() funktionen för att hämmta info från en buss+sensor
//lib.api.get(Bus, sensor , t1, t2,function(data){ console.log(data)});
lib.api.api.get("Ericsson$Vin_Num_001", "Ericsson$Next_Stop" , t1, t2,function(data){ console.log(data)});












