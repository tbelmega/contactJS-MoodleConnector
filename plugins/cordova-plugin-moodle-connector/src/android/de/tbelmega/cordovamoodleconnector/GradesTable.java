package de.tbelmega.cordovamoodleconnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thiemo on 06.01.2016.
 */
public class GradesTable {

    //map to store test name and result in %
    private Map<String, Integer> percentages = new HashMap<String, Integer>();

    public GradesTable(JSONObject jsonContent) throws JSONException {
        JSONArray tabledata = jsonContent.getJSONArray("tables").getJSONObject(0).getJSONArray("tabledata");

        //for all data items
        for (int i = 0; i < tabledata.length(); i++) {
            JSONObject currentObj = tabledata.getJSONObject(i);

            //if it has a percentage, store the result in the map
            if (currentObj.has("percentage")){
                storePercentageOfTest(currentObj);
            }
        }
    }

    private void storePercentageOfTest(JSONObject currentObj) throws JSONException {
        String name = parseTestName(currentObj.getJSONObject("itemname").getString("content"));
        Integer percentage = parsePercentage(currentObj.getJSONObject("percentage").getString("content"));
        percentages.put(name, percentage);
    }

    static String parseTestName(String nameString) throws JSONException {
        int lastHtmlTagStart = nameString.lastIndexOf("<");
        int secondLastHtmlTagEnd = nameString.substring(0,lastHtmlTagStart).lastIndexOf(">");

        return nameString.substring(secondLastHtmlTagEnd + 1,lastHtmlTagStart);
    }

    static Integer parsePercentage(String percentageContent) {
        if (percentageContent.equals("-")) {
            return 0;
        }

        //make "100.00" out of "100,00 %"
        int index = percentageContent.indexOf("%");
        String number = percentageContent.substring(0,index).trim().replace(",",".");

        Float percentage = Float.parseFloat(number);
        return Math.round(percentage);
    }

    public Integer getPercentage(String testName) {
        return this.percentages.get(testName);
    }
}

