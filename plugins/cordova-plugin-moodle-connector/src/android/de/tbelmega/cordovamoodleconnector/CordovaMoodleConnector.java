package de.tbelmega.cordovamoodleconnector;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Thiemo on 04.01.2016.
 */
public class CordovaMoodleConnector extends CordovaPlugin {

    public static final String ACTION_GET_EXAM_RESULT = "getExamResult";

    /**
     * This method is called by the JavaScript part of the application.
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     *                          0 - The moodle server url
     *                          1 - An array of test names
     *                          2 - A moodle user ID
     *                          3 - A moodle auth token
     *                          4 - A moodle course ID
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return
     * @throws JSONException
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(ACTION_GET_EXAM_RESULT)) {
            return doActionGetExamResult(args, callbackContext);
        }
        return false;
    }

    private boolean doActionGetExamResult(JSONArray args, CallbackContext callbackContext) throws JSONException {

        // get request parameters from args object
        String   moodleServerUrl = args.getString(0);
        int userId = args.getInt(2);
        int courseId = args.getInt(4);
        String authToken = args.getString(3);

        JSONObject gradeReport;
        //try to retrieve grades table from moodle
        try {

            gradeReport = new MoodleHttpClient(moodleServerUrl, authToken).getGradeReport(courseId,userId);
        } catch (IOException e) {
            callbackContext.error("CordovaMoodleConnector: Could not retrieve grades table from moodle: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        System.out.println("CordovaMoodleConnector: Grade report is \n" + gradeReport.toString(4));


        //read result of tests from table
        GradesTable grades = new GradesTable(gradeReport);

        System.out.println("CordovaMoodleConnector: args are \n" + args.toString(4));

        JSONArray requestedTestNames = args.getJSONArray(1);
        JSONArray results = new JSONArray();

        for (int i = 0; i < requestedTestNames.length(); i++) {
            String testName = requestedTestNames.getString(i);
            Integer result = grades.getPercentage(testName);
            results.put(result);
        }

        System.out.println("CordovaMoodleConnector: Send test results back to JavaScript now: \n" + results.toString(4));

        //pass requested result array back to JavaScript method
        callbackContext.success(results.toString());
        return true;
    }

}
