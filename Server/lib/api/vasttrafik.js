var exports = module.exports = {};
var request = require('request');

//Api request data
var protocol      = "http://",
    baseurl       = "api.vasttrafik.se/bin/rest.exe/"
    version        = "v1/",
    authKey       = "25b4ab49-8e6d-44b2-b930-2c97594be68a",
    format        = "json";

//GID static data 
var classGID = "9015",
    thm      = "014",
    line     = "5055"; 
  
exports.get = function(service,params,callback){
    var urlParams =""
    for (var i in params) {
        urlParams += "&"+i+"="+params[i];
    };
    var url = protocol+baseurl+version+service+"?authKey="+authKey+"&format="+format+urlParams;
    request( {url : url},
        function (error, response, body) {
                
                if(!error){
                    callback({data:JSON.parse(body),input:params});
                }else{
                    console.log(""+error);
                    callback({data:null,input:params});
                }
                
        }

    );
}
exports.getGID = function(trip){ 
    var preZeroes ="";
    trip = new String(trip);
    for (var i = 0; i < (5-trip.length); i++) {
        preZeroes+="0";
    };
    return classGID+thm+line+preZeroes+trip;
}
exports.request = function(url,callback){
    request( {
        url : url
        },
        function (error, response, body) {
                callback(JSON.parse(body));
        }

    );
}
