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

    /*
     * server url, auth token, user ID and course ID are hard coded, as long as MOTIVATE does not support configuration
     */
    static final String MOODLE_SERVER_URL = "http://localhost/webservice/rest/server.php";
    static final String AUTH_TOKEN = "6a56dc3fa0adfaae8fcb97eb6bdde2e6";
    static final int USER_ID = 4;
    static final int COURSE_ID = 2;


    public static final String ACTION_GET_EXAM_RESULT = "getExamResult";

    /**
     * This method is called by the JavaScript part of the application.
     * @param action          The action to execute.
     * @param args            The exec() arguments. The first argument must be the name of a test in the moodle course.
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
        JSONObject gradeReport;

        //try to retrieve grades table from moodle
        try {
            gradeReport = new MoodleHttpClient(MOODLE_SERVER_URL, AUTH_TOKEN).getGradeReport(COURSE_ID,USER_ID);
        } catch (IOException e) {
            callbackContext.error("Could not retrieve grades table from moodle: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        //read result of requested test from table
        GradesTable grades = new GradesTable(gradeReport);
        String testName = args.getString(0);
        Integer result = grades.getPercentage(testName);

        if (result == null) {
            callbackContext.error("Could not find specified test:" + testName);
            return false;
        }

        //pass requested result back to JavaScript method
        callbackContext.success(result.toString());
        return true;
    }

}
