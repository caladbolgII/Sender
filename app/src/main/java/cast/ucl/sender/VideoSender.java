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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
 * Created by LENOVO on 4/23/2015.
 */
public class VideoSender extends ActionBarActivity {
    public TextView myTextField;
    public DatePicker textdeadline;
    public Button myButton;
    public Button searchButton;
    public String deaddate;
    public String command;
    public TimePicker deadtime;
    String responseStr;
    String url;
    GlobalClass globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_sender);
        myTextField = (TextView) findViewById(R.id.video_url);
        textdeadline = (DatePicker) findViewById(R.id.deadline_video);
        deadtime = (TimePicker)findViewById(R.id.vidtimePicker);
        deadtime.setIs24HourView(Boolean.TRUE);
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) getSupportActionBar()
                .getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.actionbar_sender, null);
        responseStr = "";
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);
        globalVariable= (GlobalClass) getApplicationContext();




        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff2196f3));
        Spannable text = new SpannableString("Cast to Screen");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#e9e9e9")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //actionBar.setTitle(text);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        TextView title = (TextView)customActionBarView.findViewById(R.id.action_title);
        title.setText(text);

        Button send = (Button) customActionBarView
                .findViewById(R.id.next);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                attempt_add_video(view);

            }
        });


        LinearLayout linear =(LinearLayout)customActionBarView.findViewById(R.id.layout_g);
        linear.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                int i = globalVariable.getclick();
                i++;
                globalVariable.setclick(i);
            }
        });
        LinearLayout linear1 =(LinearLayout)customActionBarView.findViewById(R.id.layout_b);
        linear1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                int i = globalVariable.getclick();
                i++;
                globalVariable.setclick(i);
            }
        });
        LinearLayout linear2 =(LinearLayout)findViewById(R.id.layout_1);
        linear2.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                int i = globalVariable.getclick();
                i++;
                globalVariable.setclick(i);
            }
        });
        LinearLayout linear3=(LinearLayout)findViewById(R.id.layout_2);
        linear3.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                int i = globalVariable.getclick();
                i++;
                globalVariable.setclick(i);
            }
        });
        LinearLayout linear4 =(LinearLayout)findViewById(R.id.layout_3);
        linear4.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                int i = globalVariable.getclick();
                i++;
                globalVariable.setclick(i);
            }
        });


    }


    public void attempt_add_video(View view){
        String month = "";
        String year = "";
        String day = "";
        String hour = "";
        String minutes = "";
        month = String.format("%02d",textdeadline.getMonth()+1);
        Log.e("month",Integer.toString(textdeadline.getMonth()));

        day = String.format("%02d", textdeadline.getDayOfMonth());
        year = Integer.toString(textdeadline.getYear());
        hour = Integer.toString(deadtime.getCurrentHour());
        minutes = Integer.toString(deadtime.getCurrentMinute());
        deaddate = month + " "+ day+ " " + year+ " "+hour+":"+minutes;
        command = Constants.action_add_video;
        if(myTextField.getText().toString().length()==0) {
            Context context = getApplicationContext();
            CharSequence text = "Invalid URL, please search again";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            try {
                new Connection().execute();
            } catch (Exception e) {
                Log.d("JSON Exception1", e.toString());
            }

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            globalVariable.setvideoresponse(responseStr);
            Context context = getApplicationContext();
            int click = globalVariable.getclick();
            CharSequence text = "Video has been added to queue," + "There have been:"+click+"Misclicks";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            globalVariable.setclick(0);
            go_back();
        }
    }

    public void go_back() {
        Intent intent = new Intent(this, QueueEdit.class);
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
            HttpPost httpost = new HttpPost(Constants.SERVER_ADDR_VIDEO);
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
            myTextField.setText("www.youtube.com/watch?v="+extras.getString("video"));
            url = extras.getString("video");

        }
        // Activity being restarted from stopped state
    }
    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            myTextField.setText("www.youtube.com/watch?v="+extras.getString("video"));
            url = extras.getString("video");

        }
        // Activity being restarted from stopped state
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            myTextField.setText("www.youtube.com/watch?v="+extras.getString("video"));
            url = extras.getString("video");

        }
    }


}