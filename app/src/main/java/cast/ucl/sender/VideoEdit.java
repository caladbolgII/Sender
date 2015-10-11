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
import android.widget.TextView;
import android.widget.TimePicker;
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
 * Created by LENOVO on 5/18/2015.
 */
public class VideoEdit extends ActionBarActivity {
    public EditText myTextField;
    public DatePicker textdeadline;
    public Button myButton;
    public Button searchButton;
    public String deaddate;
    public String command;
    public TimePicker deadtime;
    String responseStr;
    String url;
    public TextView idtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_edit);
        myTextField = (EditText) findViewById(R.id.video_url);
        textdeadline = (DatePicker) findViewById(R.id.deadline_video);
        deadtime = (TimePicker) findViewById(R.id.vidtimePicker);
        deadtime.setIs24HourView(Boolean.TRUE);
        idtext = (TextView)findViewById(R.id.videoitemid);
        ActionBar actionBar = getSupportActionBar();
        Drawable d = getResources().getDrawable(R.drawable.back);
        actionBar.setHomeAsUpIndicator(d);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff161616));
        Spannable text = new SpannableString("Video Editor");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#3498db")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
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

    public void attempt_edit_video(View view) {
        String month = "";
        String year = "";
        String day = "";
        String hour = "";
        String minutes = "";
        month = String.format("%02d", textdeadline.getMonth() + 1);
        Log.e("month", Integer.toString(textdeadline.getMonth()));

        day = String.format("%02d", textdeadline.getDayOfMonth());
        year = Integer.toString(textdeadline.getYear());
        hour = Integer.toString(deadtime.getCurrentHour());
        minutes = Integer.toString(deadtime.getCurrentMinute());
        deaddate = month + " " + day + " " + year + " " + hour + ":" + minutes;
        command = Constants.action_add_video;
        if (myTextField.getText().toString().length() == 0) {
            Context context = getApplicationContext();
            CharSequence text = "Invalid URL, please search again";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            try {
                new Connection().execute();
            } catch (Exception e) {
                Log.d("JSON Exception1", e.toString());
            }

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            globalVariable.setvideoresponse(responseStr);
            Context context = getApplicationContext();
            CharSequence text = "Video has been edited";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            go_back();
        }
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
        protected Object doInBackground(Object... arg0) {
            connect();
            return null;
        }

    }

    private void connect() {
        try {
            String json = "";
            InputStream inputStream = null;
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("action", "edit");
            jsonObject.accumulate("video_id", url);
            jsonObject.accumulate("id",idtext.getText());
            jsonObject.accumulate("deadline", deaddate);
            //DefaultHttpClient httpclient= HttpClientProvider.newInstance("string");
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(Constants.SERVER_ADDR_VIDEO);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpost.setEntity(se);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httpost);
            // safeClose(httpclient);
            inputStream = response.getEntity().getContent();

            if (inputStream != null)
                responseStr = convertInputStreamToString(inputStream);
            else
                responseStr = "Did not work!";

        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (JSONException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
    }

    public static void safeClose(HttpClient client) {
        if (client != null && client.getConnectionManager() != null) {
            client.getConnectionManager().shutdown();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
                String id = "";
                String vidurl = "";
                String deadline = "";
                String year = "";
                String month = "";
                String day = "";
                String hour = "";
                String min = "";


                id = extras.getString("id");
                vidurl = extras.getString("url");
                deadline = extras.getString("timeout");
                year = deadline.substring(12, 15);
                month = deadline.substring(17, 18);
                day = deadline.substring(20, 21);
                hour = deadline.substring(23, 24);
                min = deadline.substring(26, 27);

                textdeadline.updateDate(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                deadtime.setCurrentHour(Integer.parseInt(hour));
                deadtime.setCurrentMinute(Integer.parseInt(min));
                idtext.setText(id);
                myTextField.setText("www.youtube.com/watch?v=" + vidurl);
                url = vidurl;

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("video").isEmpty()) {
                String id = "";
                String vidurl = "";
                String deadline = "";
                String year = "";
                String month = "";
                String day = "";
                String hour = "";
                String min = "";


                id = extras.getString("id");
                vidurl = extras.getString("url");
                deadline = extras.getString("timeout");
                year = deadline.substring(12, 15);
                month = deadline.substring(17, 18);
                day = deadline.substring(20, 21);
                hour = deadline.substring(23, 24);
                min = deadline.substring(26, 27);

                textdeadline.updateDate(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                deadtime.setCurrentHour(Integer.parseInt(hour));
                deadtime.setCurrentMinute(Integer.parseInt(min));
                idtext.setText(id);
                myTextField.setText("www.youtube.com/watch?v=" + vidurl);
                url = vidurl;
            }
            else{
                myTextField.setText("www.youtube.com/watch?v=" + extras.getString("video"));
                url = extras.getString("video");
            }

        }
        // Activity being restarted from stopped state
    }


}


