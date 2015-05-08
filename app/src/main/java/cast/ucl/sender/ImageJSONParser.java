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
public class ImageJSONParser {/** Receives a JSONObject and returns a list */
public List<HashMap<String,String>> parse(JSONObject jObject){

    JSONArray jImages = null;
    try {
        /** Retrieves all the elements in the 'countries' array */
        jImages = jObject.getJSONArray("imagequeue");
    } catch (JSONException e) {
        e.printStackTrace();
    }
    /** Invoking getCountries with the array of json object
     * where each json object represent a country
     */
    return getVideos(jImages);
}


    private List<HashMap<String, String>> getVideos(JSONArray jImages){
        int imagecount = jImages.length();
        List<HashMap<String, String>> imageList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> image = null;

        /** Taking each country, parses and adds to list object */
        //for(int i=0; i<videoCount;i++){
        for(int i=0; i<3;i++){
            try {
                /** Call getCountry with country JSON object to parse the country */
                image = getImage((JSONObject)jImages.get(i));
                imageList.add(image);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return imageList;
    }

    /** Parsing the Country JSON object */
    private HashMap<String, String> getImage(JSONObject jVideo){

        HashMap<String, String> image = new HashMap<String, String>();
        String id =  "";
        String imageid = "";
        //String timein = "";
        String timeout = "";


        try {
            id = jVideo.getString("_id");
            imageid = jVideo.getString("image_id");
            //timein = jVideo.getString("time_in");
            timeout = jVideo.getString("time_out");

            id = "Item:"+id;
            imageid = "Image URL: "+imageid;
            timeout = "Deadline: "+ timeout.substring(0, 10);

            image.put("_id", id);
            image.put("image_id", imageid);
            //video.put("details", details);
            image.put("time_out",timeout);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }
}
