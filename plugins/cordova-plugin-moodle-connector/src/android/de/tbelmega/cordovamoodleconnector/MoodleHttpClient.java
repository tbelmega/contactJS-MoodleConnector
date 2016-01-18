package de.tbelmega.cordovamoodleconnector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Thiemo on 06.01.2016.
 */
public class MoodleHttpClient {


    public static final String MOODLE_FUNCTION_GRADEREPORT = "gradereport_user_get_grades_table";
    private final URL url;
    private final String authToken;


    public MoodleHttpClient(String moodleServerUrl, String authToken) throws MalformedURLException {
        this.url = new URL(moodleServerUrl);
        this.authToken = authToken;

    }

    public JSONObject getGradeReport(Integer courseId, Integer userId) throws IOException, JSONException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        sendRequest(courseId, userId, connection);

        int responseCode = connection.getResponseCode();

        String responseContent = readResponse(connection);

        System.out.println(responseContent);

        return new JSONObject(responseContent);
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder responsePayload = new StringBuilder();

        while((line = br.readLine()) != null) {
            responsePayload.append(line);
        }

        return responsePayload.toString();
    }

    private void sendRequest(Integer courseId, Integer userId, HttpURLConnection connection) throws IOException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty("USER-AGENT","Mozilla/5.0");
        connection.setRequestProperty("ACCEPT-LANGUAGE","de-DE, de, en-US, en");

        connection.setDoOutput(true);

        DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());

        dStream.writeBytes(createPostParameters(courseId, userId));
        dStream.flush();
        dStream.close();
    }

    private String createPostParameters(Integer courseId, Integer userId) {
        StringBuilder postParameters = new StringBuilder()
                .append("moodlewsrestformat=json")
                .append("&")
                .append("wsfunction=" + MOODLE_FUNCTION_GRADEREPORT)
                .append("&")
                .append("wstoken=" + authToken)
                .append("&")
                .append("courseid=" + courseId.toString())
                .append("&")
                .append("userid=" + userId.toString());
        return postParameters.toString();
    }

}
