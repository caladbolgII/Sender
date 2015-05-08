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
public class VideoJSONParser 	{/** Receives a JSONObject and returns a list */
public List<HashMap<String,String>> parse(JSONObject jObject){

    JSONArray jVideos = null;
    try {
        /** Retrieves all the elements in the 'countries' array */
        jVideos = jObject.getJSONArray("videoqueue");
    } catch (JSONException e) {
        e.printStackTrace();
    }
    /** Invoking getCountries with the array of json object
     * where each json object represent a country
     */
    return getVideos(jVideos);
}


        private List<HashMap<String, String>> getVideos(JSONArray jVideos){
            int videoCount = jVideos.length();
            List<HashMap<String, String>> videoList = new ArrayList<HashMap<String,String>>();
            HashMap<String, String> video = null;

            /** Taking each country, parses and adds to list object */
            for(int i=0; i<videoCount;i++){

                try {
                    /** Call getCountry with country JSON object to parse the country */
                    video = getVideo((JSONObject)jVideos.get(i));
                    videoList.add(video);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return videoList;
        }

        /** Parsing the Country JSON object */
        private HashMap<String, String> getVideo(JSONObject jVideo){

            HashMap<String, String> video = new HashMap<String, String>();
            String id =  "";
            String videoid = "";
            //String timein = "";
            String timeout = "";
            String url = "";

            try {
                id = jVideo.getString("_id");
                videoid = jVideo.getString("video_id");
                //timein = jVideo.getString("time_in");
                timeout = jVideo.getString("time_out");

                id = "Item:"+id;
                videoid = "Video ID: "+videoid;
                timeout = "Deadline: "+ timeout.substring(0, 10);
               // url = "http://img.youtube.com/vi/"+  videoid + "/default.jpg";
                video.put("_id", id);
                video.put("video_id", videoid);
                //video.put("details", details);
                video.put("time_out",timeout);
                //video.put("video_url", url);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return video;
        }
}
