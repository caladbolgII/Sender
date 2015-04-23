package cast.ucl.sender;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by LENOVO on 4/23/2015.
 */
public class VideoSender extends ActionBarActivity {
    public EditText myTextField;
    public DatePicker textdeadline;
    public Button myButton;
    public Button searchButton;
    public String deaddate;
    public String command;
    String responseStr;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_sender);
        myTextField = (EditText) findViewById(R.id.video_url);
        textdeadline = (DatePicker) findViewById(R.id.deadline_video);
        ActionBar actionBar = getSupportActionBar();
        Drawable d=getResources().getDrawable(R.drawable.back);
        actionBar.setHomeAsUpIndicator(d);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff262626));
        Spannable text = new SpannableString("Video Sender");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#ecf0f1")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        myButton = (Button) findViewById(R.id.button_castvid);
        searchButton = (Button) findViewById(R.id.button_search);
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
    public void attemptSend(View view){
        deaddate = Integer.toString(textdeadline.getYear())+ "-"+ Integer.toString(textdeadline.getMonth()) + "-" +Integer.toString(textdeadline.getDayOfMonth());
        command = Constants.action_cast_video;
        new Connection().execute();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.setvideoresponse(responseStr);

        Context context = getApplicationContext();
        CharSequence text = "Video has been cast";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        go_back();
    }
    public void attempt_add_video(View view){
        deaddate = Integer.toString(textdeadline.getYear())+ "-"+ Integer.toString(textdeadline.getMonth()) + "-" +Integer.toString(textdeadline.getDayOfMonth());
        command = Constants.action_add_video;
        new Connection().execute();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.setvideoresponse(responseStr);
        Context context = getApplicationContext();
        CharSequence text = "Video has been added to queue";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        go_back();
    }

    public void go_back() {
        Intent intent = new Intent(this, QueueEdit.class);
        startActivity(intent);
    }
    public void search_youtube(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
    private class Connection extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0){
            connect();
            return null;
        }

    }

    private void connect(){
        try {
            String json = "";
            InputStream inputStream = null;
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("action",command);
            jsonObject.accumulate("video_id", url);
            jsonObject.accumulate("deadline", deaddate);
            //DefaultHttpClient httpclient= HttpClientProvider.newInstance("string");
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(Constants.SERVER_ADDR3);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpost.setEntity(se);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httpost);
            // safeClose(httpclient);
            inputStream = response.getEntity().getContent();

            if(inputStream != null)
                responseStr = convertInputStreamToString(inputStream);
            else
                responseStr = "Did not work!";

        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        catch (JSONException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
    }
    public static void safeClose(HttpClient client)
    {
        if(client != null && client.getConnectionManager() != null)
        {
            client.getConnectionManager().shutdown();
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
    protected void onStart() {
        super.onStart();  // Always call the superclass method first
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            myTextField.setText("www.youtube.com/watch?v="+extras.getString("video"));
            url = extras.getString("video");
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            myTextField.setText(extras.getString("video"));
            url = extras.getString("video");

        }
        // Activity being restarted from stopped state
    }

}