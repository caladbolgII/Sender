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
public class ImageJSONParser 	{/** Receives a JSONObject and returns a list */
public ArrayList<ImageListModel> parse(JSONObject jObject){

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
    return getImages(jImages);
}


    private ArrayList<ImageListModel> getImages(JSONArray jImages){
        int imageCount =  jImages.length();
        // List<HashMap<String, String>> videoList = new ArrayList<HashMap<String,String>>();
        //HashMap<String, String> video = null;

        ArrayList<ImageListModel> imglist = new ArrayList<ImageListModel>();
        /** Taking each country, parses and adds to list object */
        for(int i=0; i<imageCount;i++){
            // for(int i=0; i<3;i++){

            try {
                /** Call getCountry with country JSON object to parse the country */
                //video = getVideo((JSONObject)jVideos.get(i));
                //videoList.add(video);
                ImageListModel listitem = new ImageListModel();
                JSONObject jImage;
                jImage = (JSONObject)jImages.get(i);
                String id =  "";
                String imgurl = "";
                String timeout = "";
                String holder = "";
                String title = "";
                String type  = "";
                String timeconv="";
                int conv;

                id = jImage.getString("_id");
                imgurl = jImage.getString("image_url");
                timeout = jImage.getString("time_out");
                //Log.e("hello", timeout);
                timeconv= timeout.substring(11, 13);
                conv = Integer.parseInt(timeconv);
                conv = conv+8;
                if (conv>24) conv = conv -24;
                else conv = conv;
                timeconv = Integer.toString(conv, 10);



                holder =  timeout.substring(11, 16);
                timeout = "Expires on: "+timeout.substring(0, 10)+" "+timeconv +timeout.substring(13,16); ;
                title = jImage.getString("title");
                type = jImage.getString("classification");

                listitem.setId(id);
                listitem.setUrl(imgurl);
                listitem.setTimeout(timeout);
                listitem.setTitle(title);
                listitem.setType(type);

                imglist.add(i,listitem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return imglist;
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
        String holder = "";

        try {
            id = jVideo.getString("_id");
            videoid = jVideo.getString("video_id");
            timeout = jVideo.getString("time_out");
            holder =  timeout.substring(11, 16);
            id = "Item:"+id;
            videoid = "Video ID: "+videoid;
            timeout = "Deadline: "+ timeout.substring(0, 10)+" "+holder;
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
