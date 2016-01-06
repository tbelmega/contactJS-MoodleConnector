window.getExamResult = function(testname, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "SamplePlugin", testname, []);
};