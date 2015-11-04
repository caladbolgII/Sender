package cast.ucl.sender;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Caladbolg on 05/10/2015.
 */
public class PhotoJSONParser {/** Receives a JSONObject and returns a list */
String imgurl = "";

public ArrayList<FBImageListModel> parse(JSONObject jObject){


    JSONArray jImages = null;
    try {
        /** Retrieves all the elements in the 'countries' array */
        jImages = jObject.getJSONArray("data");
    } catch (JSONException e) {
        e.printStackTrace();
    }
    /** Invoking getCountries with the array of json object
     * where each json object represent a country
     */
    return getImages(jImages);
}


    private ArrayList<FBImageListModel> getImages(JSONArray jImages){
        int imageCount =  jImages.length();
        ArrayList<FBImageListModel> imglist = new ArrayList<FBImageListModel>();

        // List<HashMap<String, String>> videoList = new ArrayList<HashMap<String,String>>();
        //HashMap<String, String> video = null;


        /** Taking each country, parses and adds to list object */
        for(int i=0; i<imageCount;i++){
            // for(int i=0; i<3;i++){
            FBImageListModel listitem = new FBImageListModel();
            try {
                /** Call getCountry with country JSON object to parse the country */
                //video = getVideo((JSONObject)jVideos.get(i));
                //videoList.add(video);

                JSONObject jImage;
                jImage = (JSONObject)jImages.get(i);
                String id =  "";

                id = jImage.getString("id");

                Bundle params = new Bundle();
                params.putString("fields", "images");
//                try{
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        id,
                        params,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                JSONObject jsonObject;
                                JSONObject jsonObject1;
                                JSONArray array;
                                jsonObject = response.getJSONObject();
                                try {
                                array = jsonObject.getJSONArray("images");
                                    //Log.v(-"response",array.toString());
                                    jsonObject1 = array.getJSONObject(2);
                                    //Log.v("response",jsonObject1.toString());
                                    imgurl = jsonObject1.getString("source");
                                    //Log.v("response1",imgurl);
                                }
                                catch (JSONException e){
                                    Log.e("JSON","error");
                                }
                            }
                        }
                ).executeAndWait();
//                }
//                catch (InterruptedException e){
//
//                }
//                catch (ExecutionException e){
//
//                }
                //Log.e("id", id);
                //Log.e("url",imgurl);
                String integ = " in is"+i;
                Log.v("response",id);
                Log.v("integer",integ);
                Log.v("response2",imgurl);

                listitem.setId(id);
                listitem.setUrl(imgurl);


                imglist.add(i, listitem);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
       Log.v("imagelist", imglist.get(0).getId());
        Log.v("imagelist", imglist.get(1).getId());
        Log.v("imagelist",imglist.get(2).getId());
        Log.v("imagelist",imglist.get(3).getId());
        return imglist;
    }


}

