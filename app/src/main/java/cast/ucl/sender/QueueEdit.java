package cast.ucl.sender;

import android.content.Intent;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueueEdit extends ActionBarActivity {
    String responseStr;
    public TextView debugger;
    public ListView listView ;
    public SimpleAdapter simpleAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) getSupportActionBar()
        .getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.actionbar, null);

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
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        Drawable d=getResources().getDrawable(R.drawable.back);
        actionBar.setHomeAsUpIndicator(d);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff262626));
        actionBar.setTitle("VIDEO QUEUE");
        debugger =   (TextView)findViewById(R.id.debugview);
        listView = (ListView) findViewById(R.id.videolist);
        try {
            new video_connect().execute().get();
        }
        catch (Exception e){
            
        }
        simpleAdapter = new SimpleAdapter(this, videoqueue, android.R.layout.simple_list_item_1, new String[] {"video"}, new int[] {android.R.id.text1});
        listView.setAdapter(simpleAdapter);


    }

    public void delete(View view){

        new video_delete().execute();


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
            jsonObject.accumulate("id", "5538a9dcc8bee1d996cfd0e6");

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(Constants.SERVER_ADDR2);
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
            URI website = new URI("http://192.168.1.102:8080/getVideos");
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

            debugger.setText(responseStr);
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


    List<Map<String,String>> videoqueue = new ArrayList<Map<String,String>>();
    private void initList(){

        try{
            JSONObject jsonResponse = new JSONObject(responseStr);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("video");

            for(int i = 0; i<jsonMainNode.length();i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String url = jsonChildNode.optString("video_id");
                String deadline = jsonChildNode.optString("time_out");
                String outPut = url + "deadline:" +deadline;
                videoqueue.add(createvideo("video", outPut));
            }
        }
        catch(JSONException e){
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap<String, String> createvideo(String name,String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
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


}
