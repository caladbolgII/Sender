package cast.ucl.sender;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class QueueEdit extends ActionBarActivity {
    VideoQueueAdapter adapter;
    String responseStr;
    public ListView listView ;
    public QueueEdit  queue_activity= null;
    String videoItem = "";
    private ArrayList<VideoListModel> videoList = new ArrayList<VideoListModel>();
    SimpleYouTubeHelper help;
    public ListViewLoaderTask listViewLoaderTask;
    String jsonStr = "";
    Resources res;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) getSupportActionBar()
        .getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.actionbar, null);
        responseStr = "";
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP |ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        setContentView(R.layout.activity_vidcast);


       Button addqueue = ( Button) customActionBarView
                .findViewById(R.id.add);
        addqueue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                Intent intent = new Intent(view.getContext(), VideoSender.class);
                startActivity(intent);

            }
        });

        Button editqueue = ( Button) customActionBarView
                .findViewById(R.id.edit);
        editqueue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                try {
                    new video_connect().execute().get();
                }catch(Exception e){
                    Log.d("Exception",e.toString());
                }

                jsonStr = readFromFile();
                jsonStr = "{ " +
                        " \"videoqueue\": " + jsonStr + "} ";

                /** Start parsing xml data */

            }
        });
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        Drawable d=getResources().getDrawable(R.drawable.back);
        actionBar.setHomeAsUpIndicator(d);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff262626));
        actionBar.setTitle("VIDEO QUEUE");
        res =getResources();
        queue_activity = this;
        listView = (ListView)findViewById(R.id.videolist);


        try {
            new video_connect().execute().get();
        }catch(Exception e){
            Log.d("Exception",e.toString());
        }
        listViewLoaderTask = new ListViewLoaderTask();


        //jsonStr = readFromFile();
            jsonStr = "{ " +
                    " \"videoqueue\": " +responseStr + "} ";



        /** Start parsing xml data */
        //responseStr = globalVariable.getvideoresponse();
/*
        if (responseStr.length() <5){

        }
        else*/
        try{
            listViewLoaderTask.execute(jsonStr).get();
        }
        catch (Exception e){

        }
        listView = ( ListView ) findViewById(R.id.videolist);
        VideoListModel list1 = videoList.get(0);
        Log.e("List url 1", list1.getUrl());
        list1 = videoList.get(1);
        Log.e("List url 2", list1.getUrl());
        list1 = videoList.get(2);
        Log.e("List url 3", list1.getUrl());
//
//            /** Setting the adapter containing the country list to listview */
        adapter = new VideoQueueAdapter(this,videoList,res);

        listView.setAdapter(adapter);
    }

    private class ListViewLoaderTask extends AsyncTask{

        JSONObject jObject;

        /** Doing the parsing of xml data in a non-ui thread */
        @Override
        protected Object doInBackground(Object... arg0) {
            VideoJSONParser videoJSONParser;
            videoJSONParser = new VideoJSONParser();
            try{
                jObject = new JSONObject(jsonStr);

//                videoJSONParser.parse(jObject);
//
            }catch(Exception e){
                Log.d("JSON Exception1",e.toString());
            }
//
//            //VideoJSONParser videoJsonParser = new VideoJSONParser();
//
//            List<HashMap<String, String>> videos = null;

            videoList = new ArrayList<VideoListModel>();
            try{
                /** Getting the parsed data as a List construct */
                videoList = videoJSONParser.parse(jObject);
                Log.v("Listhi", "hello");
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }

            /** Keys used in Hashmap */
            //String[] from = { "_id","video_id","time_out"};

            /** Ids of views in listview_layout */
            //int[] to = { R.id.item_id,R.id.vid_url,R.id.item_timeout};

            /** Instantiating an adapter to store each items
             *  R.layout.listview_layout defines the layout of each item
             */

//           adapter = new ArrayAdapter<VideoListModel>(getApplicationContext(), R.layout.activity_video_item, videoList){
//                @Override
//                public View getView(int position, View convertView, ViewGroup parent) {
//                    if(convertView == null){
//                        convertView = getLayoutInflater().inflate(R.layout.activity_video_item, parent, false);
//                    }
//                    Log.e("Hee",videoList.size()+"");
//                    ImageView thumbnail = (ImageView)convertView.findViewById(R.id.vid_thumb);
//                    TextView title = (TextView)convertView.findViewById(R.id.vid_title);
//                    TextView url = (TextView)convertView.findViewById(R.id.vid_url);
//                    TextView deadline = (TextView)convertView.findViewById(R.id.item_timeout);
//                    TextView id = (TextView)convertView.findViewById(R.id.item_id);
//
//
//                    VideoListModel vid_list_item = videoList.get(position);
//                    //help.getImageUrlQuietly(vid_list_item.getUrl());
//                    //help.getTitleQuietly(vid_list_item.getUrl());
//                    String img = vid_list_item.getUrl();
//                    String hi="";
//                    /*
//                    try{
//                    URL embededURL = new URL("http://www.youtube.com/embed?url=" + img + "&format=json");
//                    hi = new JSONObject(IOUtils.toString(embededURL)).getString("title");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }*/
//                    img = "http://img.youtube.com/vi/"+ img+ "/0.jpg";
//
//                    Picasso.with(getApplicationContext()).load(img).resize(115, 115).into(thumbnail);
//                    title.setText(hi);
//                    url.setText(vid_list_item.getUrl());
//                    deadline.setText(vid_list_item.getTimeout());
//                    id.setText(vid_list_item.getId());
//                    VideoListModel item;
//                    /*
//                    item = videoList.get(0);
//                    Log.e("url",item.getUrl());
//                    item = videoList.get(1);
//                    Log.e("url",item.getUrl());
//                    item = videoList.get(2);
//                    Log.e("url",item.getUrl());
//                    */
//                    return convertView;
//                }
//            };

            return null;

        }

        /** Invoked by the Android system on "doInBackground" is executed completely */
        /** This will be executed in ui thread */
//        @Override
//        protected void onPostExecute(ArrayAdapter adapter) {
//
//            /** Getting a reference to listview of main.xml layout file */
//            listView = ( ListView ) findViewById(R.id.videolist);
//
//            /** Setting the adapter containing the country list to listview */
//            listView.setAdapter(adapter);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    TextView textView = (TextView) view.findViewById(R.id.item_id);
//                    videoItem = textView.getText().toString();
//
//                    delete();
//
//                }});
//
//        }
    }


    public void delete(){
        try {

            new video_delete().execute().get();
            try {
                new video_connect().execute().get();
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }

            String jsonStr ="";
            jsonStr = readFromFile();
            jsonStr = "{ " +
                    " \"videoqueue\": " + jsonStr + "} ";
            /** Start parsing xml data */
            //responseStr = globalVariable.getvideoresponse();


        }catch(Exception e){
            Log.d("JSON Exception1",e.toString());
        }

    }

    private class video_delete extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0){
            connect1();
            return null;
        }

    }

    private void connect1(){
        try {
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("action","deleteVideo");

            jsonObject.accumulate("id",videoItem.substring(5));

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(Constants.SERVER_ADDR_DELETE);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpost.setEntity(se);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            httpclient.execute(httpost);
        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        catch (JSONException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
    }
    private class video_connect extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0){
            connect();
            return null;
        }

    }

    private void connect(){
        try {
            // String json = "";
            InputStream inputStream = null;
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("action",Constants.action_get_video);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            URI website = new URI(Constants.SERVER_GET_VIDEO);
            HttpGet request = new HttpGet();
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            // receive response as inputStream
            inputStream = response.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                responseStr = convertInputStreamToString(inputStream);
            else
                responseStr = "Did not work!";
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            globalVariable.setvideoresponse(responseStr);


           // TextView httpresponse = (TextView) findViewById(R.id.http_queue);
           // httpresponse.setText(globalVariable.getimageresponse());





        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        catch (JSONException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        catch (URISyntaxException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.menu_text, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("file.txt", Context.MODE_WORLD_READABLE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("file.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void loadVideoQueue(){
        ArrayAdapter<VideoListModel> adapter = new ArrayAdapter<VideoListModel>(getApplicationContext(), R.layout.activity_video_item, videoList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.activity_video_item, parent, false);
                }
                ImageView thumbnail = (ImageView)convertView.findViewById(R.id.vid_thumb);
                TextView title = (TextView)convertView.findViewById(R.id.vid_title);
                TextView url = (TextView)convertView.findViewById(R.id.vid_url);
                TextView deadline = (TextView)convertView.findViewById(R.id.item_timeout);
                TextView id = (TextView)convertView.findViewById(R.id.item_id);


                VideoListModel vid_list_item = videoList.get(position);
                help.getImageUrlQuietly(vid_list_item.getUrl());
                help.getTitleQuietly(vid_list_item.getUrl());

                Picasso.with(getApplicationContext()).load(help.getImageUrlQuietly(vid_list_item.getUrl())).into(thumbnail);
                title.setText(help.getImageUrlQuietly(vid_list_item.getUrl()));
                url.setText(vid_list_item.getUrl());
                deadline.setText(vid_list_item.getTimeout());
                id.setText(vid_list_item.getId());

                return convertView;
            }
        };

        listView.setAdapter(adapter);
    }
    public class SimpleYouTubeHelper {

        public  String getImageUrlQuietly(String youtubeUrl) {
            try {
                if (youtubeUrl != null) {
                    return String.format("http://img.youtube.com/vi/%s/0.jpg", youtubeUrl);
                }
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
            }
            return null;
        }

        public  String getTitleQuietly(String youtubeUrl) {
            try {
                if (youtubeUrl != null) {
                    URL embededURL = new URL("http://www.youtube.com/embed?url=" + youtubeUrl + "&format=json");
                    return new JSONObject(IOUtils.toString(embededURL)).getString("title");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
