var exports = module.exports = {};




exports.get = function(bus, sensor, t1, t2, callback){
    
    var request = require('request'),
        username = "grp38",
        password = "9-yxani3BZ",
        url = "https://ece01.ericsson.net:4443/ecity?dgw="+ bus +"&sensorSpec=" + sensor + "&t1="+ t1 + "&t2=" + t2,
        auth = "Basic " + new Buffer(username + ":" + password).toString("base64");
    
    request(
        {
            url : url,
            headers : {
                "Authorization" : auth
            }
        },
        function (error, response, body) {
                console.log(error);
                console.log(response);
                callback(body);

        }

    );
        
}




