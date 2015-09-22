
// user:grp38 pass: 9-yxani3BZ
// 25b4ab49-8e6d-44b2-b930-2c97594be68a
//
//
//
/*
var userNamePass = "grp38:9-yxani3BZ";

var url = "https://ece01.ericsson.net:4443/ecity";

var t2  = new Date().getTime();
var t1 = t2 - (1000 * 120);

var url2 = "https://ece01.ericsson.net:4443/ecity?dgw=Ericsson$Vin_Num_001&sensorSpec=Ericsson$Next_Stop&t1="+ t1 + "&t2=" + t2;

var xhr = new XMLHttpRequest();



xhr.open("GET", url2, true);
xhr.setRequestHeader("Authorization", "Basic " + btoa(userNamePass));
xhr.send();

console.log(xhr.status);
console.log(xhr.statusText);

*/
var t2  = new Date().getTime();
var t1 = t2 - (1000 * 120);

var request = require('request'),
    username = "grp38",
    password = "9-yxani3BZ",
    url = "https://ece01.ericsson.net:4443/ecity?dgw=Ericsson$Vin_Num_001&sensorSpec=Ericsson$Next_Stop&t1="+ t1 + "&t2=" + t2+"",
    auth = "Basic " + new Buffer(username + ":" + password).toString("base64");

request(
    {
        url : url,
        headers : {
            "Authorization" : auth
        }
    },
    function (error, response, body) {
        //console.log(error);
        //console.log(response);
        console.log(body);
    }
);







