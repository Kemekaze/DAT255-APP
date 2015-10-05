var exports = module.exports = {};
var request = require('request');

var protocol      = "http://",
    baseurl       = "api.vasttrafik.se/bin/rest.exe/"
    version        = "v1/",
    authKey       = "25b4ab49-8e6d-44b2-b930-2c97594be68a",
    format        = "json",
    jsonpCallback = "";
  
exports.get = function(service,input,callback){
    request( {
        url : protocol+baseurl+version+service+"?authKey="+authKey+
              "&format="+format+"&input="+input
        },
        function (error, response, body) {
                callback(JSON.parse(body));
        }

    );
} 

