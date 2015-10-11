package cast.ucl.sender;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Caladbolg on 01/10/2015.
 */
public class FBJSONParser {
    public List<HashMap<String,String>> parse(JSONObject jObject){

        JSONArray jfbs = null;
        try {
            /** Retrieves all the elements in the 'countries' array */
            jfbs = jObject.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getCountries with the array of json object
         * where each json object represent a country
         */
        return getfbs(jfbs);
    }


    private List<HashMap<String, String>> getfbs(JSONArray jfbs){
        int fbCount = jfbs.length();
        List<HashMap<String, String>> fbList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> fb = null;

        /** Taking each country, parses and adds to list object */
        for(int i=0; i<fbCount;i++){
            //for(int i=0; i<3;i++){
            try {
                /** Call getCountry with country JSON object to parse the country */
                fb = getfb((JSONObject) jfbs.get(i));
                fbList.add(fb);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return fbList;
    }

    /** Parsing the Country JSON object */
    private HashMap<String, String> getfb(JSONObject jfb){

        HashMap<String, String> fb = new HashMap<String, String>();
        String id =  "";
        String name = "";

        try {
            id = jfb.getString("id");
            name = jfb.getString("name");
          //  Log.v("id",id);
          //  Log.v("name",name);
            fb.put("id", id);
            fb.put("name", name);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fb;
    }
}


