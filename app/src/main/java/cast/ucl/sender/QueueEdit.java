package cast.ucl.sender;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.ArrayList;

public class QueueEdit extends ActionBarActivity {
    VideoQueueAdapter adapter;
    String responseStr;
    final Context context = this;
    public QueueEdit  queue_activity= null;
    String videoItem = "";
    private ArrayList<VideoListModel> videoList = new ArrayList<VideoListModel>();
    public ListViewLoaderTask listViewLoaderTask;
    String jsonStr = "";
    Resources res;
    ProgressDialog progressDialog;
    String id;

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
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME
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
                listViewLoaderTask = new ListViewLoaderTask();
                jsonStr = "{ " +
                        " \"videoqueue\": " +responseStr + "} ";
                try{
                    listViewLoaderTask.execute(jsonStr);
                }
                catch (Exception e){

                }

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
        try {
            new video_connect().execute().get();
        }catch(Exception e){
            Log.d("Exception",e.toString());
        }
        listViewLoaderTask = new ListViewLoaderTask();
            jsonStr = "{ " +
                    " \"videoqueue\": " +responseStr + "} ";

        try{
            listViewLoaderTask.execute(jsonStr);
        }
        catch (Exception e){

        }


    }

    public void isEmpty(){
        Context context = getApplicationContext();
        CharSequence text = "Queue is Empty";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private class ListViewLoaderTask extends AsyncTask<String, String, Void>{

        JSONObject jObject;

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            ListView listView ;
            listView = (ListView)findViewById(R.id.videolist);
            adapter = new VideoQueueAdapter(queue_activity,videoList,res);

            if (videoList.isEmpty()) isEmpty();
            else listView.setAdapter(adapter);
            progressDialog.dismiss();
        }
        /** Doing the parsing of xml data in a non-ui thread */
        @Override
        protected Void doInBackground(String... arg0)  {
            VideoJSONParser videoJSONParser;
            videoJSONParser = new VideoJSONParser();
            try{
                jObject = new JSONObject(jsonStr);

            }catch(Exception e){
                Log.d("JSON Exception1",e.toString());
            }
            videoList = new ArrayList<VideoListModel>();
            try{
                /** Getting the parsed data as a List construct */
                videoList = videoJSONParser.parse(jObject);
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }



            return null;

        }

    }


    public void delete(){
        try {

            new video_delete().execute().get();
            try {
                new video_connect().execute().get();
            }catch(Exception e){
                Log.d("Exception", e.toString());
            }
            listViewLoaderTask = new ListViewLoaderTask();
            jsonStr = "{ " +
                    " \"videoqueue\": " +responseStr + "} ";
            try{
                listViewLoaderTask.execute(jsonStr);
            }
            catch (Exception e){

            }
        }catch(Exception e){
            Log.d("JSON Exception1",e.toString());
        }

    }

    private class video_delete extends AsyncTask<String, String, Void> {

        /** Doing the parsing of xml data in a non-ui thread */
        @Override
        protected Void doInBackground(String... arg0)  {
            connect1();
            return null;
        }

    }

    private void connect1(){
        try {
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("action","delete");

            jsonObject.accumulate("id", id);

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(Constants.SERVER_ADDR_VIDEO);
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
    private class video_connect extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(QueueEdit.this, "Retrieving Video Queue", "Please Wait ...");
        }
        @Override
        protected void onProgressUpdate(String...values){
            super.onProgressUpdate(values);

        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
        /** Doing the parsing of xml data in a non-ui thread */
        @Override
        protected Void doInBackground(String... arg0){
        connect();
            return null;
        }

    }

    private void connect(){
        try {
            // String json = "";
            InputStream inputStream = null;
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

        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
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

    public void onItemClick(int mPosition)
    {
        VideoListModel tempValues = (VideoListModel) videoList.get(mPosition);
        final CharSequence[] items = {"Edit", "Delete"};
       id = tempValues.getId();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Select Action");

        // set dialog message
        alertDialogBuilder
                .setMessage("Delete Video?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        delete();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


        Log.e("Delete id:", id);
    }
}
