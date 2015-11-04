package cast.ucl.sender;

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
import android.view.View;
import android.view.WindowManager;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by LENOVO on 4/10/2015.
 */
public class LayoutSelector extends ActionBarActivity {
private String layout_selected;
    GlobalClass globalVariable;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_layout_selector);

        ActionBar actionBar = getSupportActionBar();
       globalVariable= (GlobalClass) getApplicationContext();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff262626));
        Spannable text = new SpannableString("Layout Selector");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#3498db")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

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
            jsonObject.accumulate("action","setLayout");
            jsonObject.accumulate("layout", layout_selected);

            DefaultHttpClient httpclient = new DefaultHttpClient();
           // DefaultHttpClient httpclient= HttpClientProvider.newInstance("string");
            HttpPost httpost = new HttpPost(Constants.SERVER_ADDR_LAYOUT);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpost.setEntity(se);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            httpclient.execute(httpost);
           // safeClose(httpclient);
            //httpclient.getConnectionManager().shutdown();
        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        catch (JSONException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
    }


    public void set_layout1(View view){
        try {
        layout_selected = "layout1";
        new Connection().execute().get();
        globalVariable.setlayout("layout1");
        go_back();
        }catch(Exception e){
            Log.d("Exception",e.toString());
        }
    }
    public void set_layout2(View view){
        try {
            layout_selected = "layout2";
            new Connection().execute().get();
            globalVariable.setlayout("layout2");
            go_back();
        }catch(Exception e){
        Log.d("Exception",e.toString());
        }
    }
    public void set_layout3(View view){
     try {
        layout_selected = "layout3";
        new Connection().execute().get();
        globalVariable.setlayout("layout3");
        go_back();
     }catch(Exception e){
        Log.d("Exception",e.toString());
     }
    }

    public void go_back() {
        Intent intent = new Intent(this, Selection.class);
        startActivity(intent);
    }

    public static void safeClose(HttpClient client)
    {
        if(client != null && client.getConnectionManager() != null)
        {
            client.getConnectionManager().shutdown();
        }
    }
}
