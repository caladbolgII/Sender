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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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


public class text extends ActionBarActivity {
    public EditText myTextField;
    public DatePicker textdeadline;
    public EditText TitleField;
    public Button myButton;
    public String deaddate;
    String responseStr;
    public String command;
    public TimePicker deadtime;
    public Spinner spinner;
    public String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_text);
        myTextField = (EditText) findViewById(R.id.text_message);
        textdeadline = (DatePicker) findViewById(R.id.deadline_text);
        TitleField = (EditText)findViewById(R.id.cast_text_title);
        ActionBar actionBar = getSupportActionBar();
        Drawable d=getResources().getDrawable(R.drawable.back);
        actionBar.setHomeAsUpIndicator(d);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff262626));
        Spannable text = new SpannableString("Message Sender");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#ecf0f1")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

        deadtime = (TimePicker)findViewById(R.id.txttimePicker);
        deadtime.setIs24HourView(Boolean.TRUE);
        spinner = (Spinner) findViewById(R.id.txtspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classification_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
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


    public void attempt_add_text(View view){

        String month = "";
        String year = "";
        String day = "";
        String hour = "";
        String minutes = "";
        month = String.format("%02d",textdeadline.getMonth()+1);
        // Log.e("month",Integer.toString(imagedeadline.getMonth()));
        command = "add";
        day = String.format("%02d", textdeadline.getDayOfMonth());
        year = Integer.toString(textdeadline.getYear());
        hour = Integer.toString(deadtime.getCurrentHour());
        minutes = Integer.toString(deadtime.getCurrentMinute());
        type = String.valueOf(spinner.getSelectedItem());
        deaddate = month + " "+ day+ " " + year+ " "+hour+":"+minutes;
        command = Constants.action_add_text;
        new Connection().execute();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.settextresponse(responseStr);
        Context context = getApplicationContext();
        CharSequence text = "Message has been added to Queue";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        go_back();
    }
    public void go_back() {
        Intent intent = new Intent(this, TextQueueEdit.class);
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
            jsonObject.accumulate("action",Constants.action_add_text);
            jsonObject.accumulate("text",myTextField.getText());
            jsonObject.accumulate("classification",type);
            jsonObject.accumulate("title", TitleField.getText());
            jsonObject.accumulate("deadline", deaddate);
            //DefaultHttpClient httpclient= HttpClientProvider.newInstance("string");
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(Constants.SERVER_ADDR_TEXTS);
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


}
