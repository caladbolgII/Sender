package cast.ucl.sender;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class text extends ActionBarActivity {
    public EditText myTextField;
    public DatePicker textdeadline;
    public Button myButton;
    public String deaddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        myTextField = (EditText) findViewById(R.id.text_message);
        textdeadline = (DatePicker) findViewById(R.id.deadline_text);
        ActionBar actionBar = getSupportActionBar();
        Drawable d=getResources().getDrawable(R.drawable.backicon);
        actionBar.setHomeAsUpIndicator(d);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff0047ab));
        actionBar.setTitle("MESSAGE SENDER");
        myButton = (Button) findViewById(R.id.buttontext);

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
        deaddate = Integer.toString(textdeadline.getDayOfMonth()) + Integer.toString(textdeadline.getMonth()) + Integer.toString(textdeadline.getYear());
         new Connection().execute();
        Context context = getApplicationContext();
        CharSequence text = "Text has been cast";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        go_back();
    }
    public void go_back() {
        Intent intent = new Intent(this, Selection.class);
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
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("action", "castText");
            jsonObject.accumulate("customText", myTextField.getText());
            jsonObject.accumulate("deadline", deaddate);

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost("http://192.168.1.102:8080");
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


}
