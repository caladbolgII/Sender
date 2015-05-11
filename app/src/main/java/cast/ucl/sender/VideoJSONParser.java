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
public ArrayList<VideoListModel> parse(JSONObject jObject){

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


        private ArrayList<VideoListModel> getVideos(JSONArray jVideos){
            int videoCount = jVideos.length();
           // List<HashMap<String, String>> videoList = new ArrayList<HashMap<String,String>>();
            //HashMap<String, String> video = null;

            ArrayList<VideoListModel> vidlist = new ArrayList<VideoListModel>();
            /** Taking each country, parses and adds to list object */
            for(int i=0; i<videoCount;i++){
           // for(int i=0; i<3;i++){

                try {
                    /** Call getCountry with country JSON object to parse the country */
                    //video = getVideo((JSONObject)jVideos.get(i));
                    //videoList.add(video);
                    VideoListModel listitem = new VideoListModel();
                    JSONObject jVideo;
                    jVideo = (JSONObject)jVideos.get(i);
                    String id =  "";
                    String videoid = "";
                    String timeout = "";

                    id = jVideo.getString("_id");
                    videoid = jVideo.getString("video_id");
                    timeout = jVideo.getString("time_out");
                    id = "Item:"+id;
                    //videoid = "Video ID: "+videoid;
                    timeout = "Deadline: "+ timeout.substring(0, 10);
                    listitem.setId(id);
                    listitem.setUrl(videoid);
                    listitem.setTimeout(timeout);

                    vidlist.add(i,listitem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return vidlist;
        }

        /** Parsing the Country JSON object */
        private HashMap<String, String> getVideo(JSONObject jVideo){
            List<VideoListModel> list;
            HashMap<String, String> video = new HashMap<String, String>();
            String id =  "";
            String videoid = "";
            //String timein = "";
            String timeout = "";
            String url = "";

            try {
                id = jVideo.getString("_id");
                videoid = jVideo.getString("video_id");
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
