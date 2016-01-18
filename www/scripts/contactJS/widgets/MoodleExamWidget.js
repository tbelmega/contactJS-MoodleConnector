define(['contactJS'], function (contactJS) {
    return (function() {
        MoodleExamWidget.description = {
            out: [
                {
                    name: 'MOODLE_EXAM1_RESULT',
                    type: 'INTEGER',
                    parameterList: []
                },
                {
                    name: 'MOODLE_EXAM2_RESULT',
                    type: 'INTEGER',
                    parameterList: []
                }
            ],
            const: [

            ],
            updateInterval: 15000
        };

        /**
         *
         * @extends Widget
         * @param discoverer
         * @returns {MoodleExamWidget}
         * @class MoodleExamWidget
         */
        function MoodleExamWidget(discoverer) {
            contactJS.Widget.call(this, discoverer);
            this._name = 'MoodleExamWidget';
            console.log("MoodleExamWidget created");
            return this;
        }

        MoodleExamWidget.prototype = Object.create(contactJS.Widget.prototype);
        MoodleExamWidget.prototype.constructor = MoodleExamWidget;

        MoodleExamWidget.prototype._initCallbacks = function() {
            this._addCallback(new contactJS.Callback().withName('UPDATE').withContextInformation(this.getOutputContextInformation()));
        };

        MoodleExamWidget.prototype.queryGenerator = function(callback) {
            var self = this;

            //define success callback function
            var successFnc = function (success) {
                console.log("MoodleExamWidget: Success function called with parameter " +  success);
                var grades = JSON.parse(success);

                //respond
                var response = new contactJS.ContextInformationList();

                for (var i = 0; i < grades.length; i++) {
                    response.put(self.getOutputContextInformation().getItems()[i].setValue(grades[i]));
                }

                self._sendResponse(response, callback)
            };


            //define error callback function
            var errorFnc = function (error) {
                console.log("MoodleExamWidget: Error function called with parameter " +  error);
            };

            //execute getExamResult function from plugin
            console.log("MoodleExamWidget: Call MoodleConnectorPlugin now.");
            window.moodleconnector.getExamResult(['Test1','Test2'],successFnc, errorFnc);

        };

        return MoodleExamWidget;
    })();
});