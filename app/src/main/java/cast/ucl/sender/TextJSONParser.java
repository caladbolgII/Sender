package cast.ucl.sender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LENOVO on 5/8/2015.
 */
public class TextJSONParser{/** Receives a JSONObject and returns a list */
public List<HashMap<String,String>> parse(JSONObject jObject){

    JSONArray jTexts = null;
    try {
        /** Retrieves all the elements in the 'countries' array */
        jTexts = jObject.getJSONArray("textqueue");
    } catch (JSONException e) {
        e.printStackTrace();
    }
    /** Invoking getCountries with the array of json object
     * where each json object represent a country
     */
    return getTexts( jTexts);
}


    private List<HashMap<String, String>> getTexts(JSONArray jTexts){
        int textCount = jTexts.length();
        List<HashMap<String, String>> textList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> text = null;

        /** Taking each country, parses and adds to list object */
        //for(int i=0; i<videoCount;i++){
        for(int i=0; i<3;i++){
            try {
                /** Call getCountry with country JSON object to parse the country */
                text = getText((JSONObject) jTexts.get(i));
                textList.add(text);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return textList;
    }

    /** Parsing the Country JSON object */
    private HashMap<String, String> getText(JSONObject jVideo){

        HashMap<String, String> text = new HashMap<String, String>();
        String id =  "";
        String mtext = "";
        //String timein = "";
        String timeout = "";


        try {
            id = jVideo.getString("_id");
            mtext = jVideo.getString("text");
            //timein = jVideo.getString("time_in");
            timeout = jVideo.getString("time_out");

            id = "Item:"+id;
            mtext= "Text: "+mtext;
            timeout = "Deadline: "+ timeout.substring(0, 10);

            text.put("_id", id);
            text.put("video_id", mtext);
            //video.put("details", details);
            text.put("time_out",timeout);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return text;
    }
}
