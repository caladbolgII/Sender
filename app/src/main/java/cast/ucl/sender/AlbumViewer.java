package cast.ucl.sender;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Caladbolg on 05/10/2015.
 */
public class AlbumViewer extends AppCompatActivity {
    TextView albumtitle;
    String request;
    public ListViewLoaderTask listViewLoaderTask;
    public AlbumViewer albumviewer;
    GridView photogrid;
    Resources res;
    FBImageQueueAdapter imagequeue;
    JSONObject reader;
    String jsonStr;
    ActionBar actionBar;
    private ArrayList<FBImageListModel> fbimageList = new ArrayList<FBImageListModel>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_album_view);
        albumviewer = this;
        photogrid = (GridView)findViewById(R.id.photo_album);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff2196f3));

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        request = "/"+id+"/"+"photos";
        Spannable text = new SpannableString(title);
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#e9e9e9")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        res =getResources();
        try {
            new image_connect().execute().get();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }



    }
    public void fetch_album() {

        try {
            new GraphRequest(

                    AccessToken.getCurrentAccessToken(),
                    request,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            String report;
                            JSONObject photos = null;
                            JSONArray list;
                            int count;
                            //report = "graph response:"+response.toString();
                            report = "";
            /* handle the result */
                            reader = response.getJSONObject();
                            JSONObject jsonObject = new JSONObject();
                            JSONArray array = new JSONArray();
                            jsonStr = reader.toString();

                            try {
                                list = reader.getJSONArray("data");
                                count = 0;

                                while (count != list.length()) {
                                    photos = list.getJSONObject(count);
                                    report = report + photos.toString();
                                    count++;
                                }
                            } catch (JSONException e) {

                            }

                            listViewLoaderTask = new ListViewLoaderTask();

                            try {
                                listViewLoaderTask.execute().get();
                                imagequeue = new FBImageQueueAdapter(albumviewer,fbimageList,res);
                                photogrid.setAdapter(imagequeue);
                                progressDialog.dismiss();
                            } catch (Exception e) {

                            }

                        }
                    }
            ).executeAsync();
        }
        catch (Exception e){

        }
    }
    public void onItemClick(int i) {
            String shareurl = "";
            shareurl = fbimageList.get(i).getUrl();
            Intent intent = new Intent(this,ImageSender.class);
            intent.putExtra("imgurl",shareurl);
            startActivity(intent);

    }

    private class ListViewLoaderTask extends AsyncTask{

        JSONObject jObject;

        /** Doing the parsing of xml data in a non-ui thread */
        @Override
        protected Object doInBackground(Object... arg0) {
            PhotoJSONParser fbimageJSONParser;
            fbimageJSONParser = new PhotoJSONParser();
            try{
                jObject = new JSONObject(jsonStr);
            }catch(Exception e){
                Log.d("JSON Exception1",e.toString());
            }
            fbimageList = new ArrayList<FBImageListModel>();
            try{
                /** Getting the parsed data as a List construct */

                fbimageList = fbimageJSONParser.parse(jObject);
                String hi  = "size:"+fbimageList.size();
//                Log.v("Listhi",hi );
//                Log.v("Listhi", fbimageList.get(0).getUrl());
//                Log.v("Listhi", fbimageList.get(1).getUrl());
//                Log.v("Listhi", fbimageList.get(2).getUrl());
//                Log.v("Listhi", fbimageList.get(3).getUrl());
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return null;

        }


    }

    private class image_connect extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(AlbumViewer.this, "Retrieving Facebook Images", "Please Wait ...");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        /**
         * Doing the parsing of xml data in a non-ui thread
         */
        @Override
        protected Void doInBackground(String... arg0) {
            fetch_album();
            return null;
        }

    }
}
