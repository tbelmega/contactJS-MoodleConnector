package de.tbelmega.cordovamoodleconnector;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Thiemo on 06.01.2016.
 */
public class MoodleHttpClient {


    public static final String MOODLE_FUNCTION_GRADEREPORT = "gradereport_user_get_grades_table";
    private final String url;
    private final String authToken;
    private final HttpClient client;

    public MoodleHttpClient(String moodleServerUrl, String authToken) {
        this.url = moodleServerUrl;
        this.authToken = authToken;
        this.client = HttpClientBuilder.create().build();
    }

    public JSONObject getGradeReport(Integer courseId, Integer userId) throws IOException, JSONException {

        HttpPost request = new HttpPost(url);
        setPostParameters(courseId, userId, request);

        HttpResponse response = client.execute(request);
        String responsePayload = EntityUtils.toString(response.getEntity());

        return new JSONObject(responsePayload);
    }

    private void setPostParameters(Integer courseId, Integer userId, HttpPost request) throws UnsupportedEncodingException {
        ArrayList<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("moodlewsrestformat", "json"));
        postParameters.add(new BasicNameValuePair("wsfunction", MOODLE_FUNCTION_GRADEREPORT));
        postParameters.add(new BasicNameValuePair("wstoken", authToken));
        postParameters.add(new BasicNameValuePair("courseid", courseId.toString()));
        postParameters.add(new BasicNameValuePair("userid", userId.toString()));
        request.setEntity(new UrlEncodedFormEntity(postParameters));
    }
}
