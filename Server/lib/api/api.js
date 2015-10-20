var exports = module.exports = {};
var request = require('request');



exports.get = function(bus, sensor, t1, t2, callback){
    //console.log(" bus: %s, t1: %d, t2: %d",bus,t1,t2);
    
        var username = "grp38",
            password = "9-yxani3BZ",
            url = "https://ece01.ericsson.net:4443/ecity?dgw="+ bus +"&sensorSpec=" + sensor + "&t1="+ t1 + "&t2=" + t2,
            auth = "Basic " + new Buffer(username + ":" + password).toString("base64");
    
    request({
        url : url,
        headers : {
            "Authorization" : auth
        }
    },
    function (error, response, body) {
            //console.log("Error: " + error);
            //console.log("Response: " + response.statusCode);
            //console.log("Body: '"+ body+"'");
            if(!error){
            	if(body != "")
                callback(JSON.parse(body));
            else
                callback(body);
            }
    });
        
}





