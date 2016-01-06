var exec = require('cordova/exec');

var moodleconnector = function() {};

moodleconnector.getExamResult = function(testname, successCallback, errorCallback) {
    console.log("CordovaMoodleConnector: Calling java class now.");

    exec(successCallback, errorCallback, "getExamResult", testname, []);
}

module.exports = moodleconnector;