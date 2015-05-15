package cast.ucl.sender;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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
                    String holder = "";
                    String type  = "";
                    String timeconv="";
                    int conv;

                    id = jVideo.getString("_id");
                    videoid = jVideo.getString("video_id");
                    timeout = jVideo.getString("time_out");
                    timeconv= timeout.substring(11, 13);
                    conv = Integer.parseInt(timeconv);
                    conv = conv+8;
                    if (conv>24) conv = conv -24;
                    else conv = conv;
                    timeconv = Integer.toString(conv, 10);
                    holder =  timeout.substring(11, 16);
                    timeout = "Expires on: "+timeout.substring(0, 10)+" "+timeconv +timeout.substring(13,16); ;
                    listitem.setId(id);
                    listitem.setUrl(videoid);
                    listitem.setTimeout(timeout);
                    listitem.setTitle(getTitile(videoid));

                    vidlist.add(i,listitem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return vidlist;
        }

    public String getTitile(String videoId)
    {
        String json;
        String titile=null;

        HttpClient Client = new DefaultHttpClient();

        try{

            HttpGet httpget =     new HttpGet("https://www.googleapis.com/youtube/v3/videos?id="+videoId+"&key=AIzaSyAE72BTsKoJ6xI-2LWOV7TodIcFOBd7_AY&fields=items(snippet(title))&part=snippet");
            ResponseHandler<String> responseHandler  =     new BasicResponseHandler();
            json = Client.execute(httpget, responseHandler);
            JSONArray jVideos = null;
            JSONObject jVideo;

            JSONObject object  = new JSONObject(json);
            jVideos = object.getJSONArray("items");
            jVideo = (JSONObject)jVideos.get(0);
            JSONObject obj = jVideo.getJSONObject("snippet");

            titile = obj.getString("title");


        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return titile;
    }
}
