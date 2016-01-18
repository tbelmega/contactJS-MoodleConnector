var exec = require('cordova/exec');

var moodleconnector = function() {};

moodleconnector.getExamResult = function(testnames, successCallback, errorCallback) {
    console.log("CordovaMoodleConnector: Calling java class to get test results for: " + testnames);

    exec(successCallback, errorCallback, "CordovaMoodleConnector", "getExamResult",
        ["http://192.168.2.2/webservice/rest/server.php",
        testnames,
        4,"6a56dc3fa0adfaae8fcb97eb6bdde2e6",
        2]);
}

module.exports = moodleconnector;