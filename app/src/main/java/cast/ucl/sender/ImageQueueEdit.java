package cast.ucl.sender;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by LENOVO on 3/6/2015.
 */
public class ImageQueueEdit extends ActionBarActivity{
    private ListView imagelist;
    String responseStr;
    ImageQueueAdapter imagequeue;
    public ImageQueueEdit image_queue;
    private ArrayList<ImageListModel> imageList = new ArrayList<ImageListModel>();
    public ListViewLoaderTask listViewLoaderTask;
    String jsonStr = "";
    Resources res;
    ProgressDialog progressDialog;
    String id;
    final Context context = this;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_imgcast);

        //setting up the actionbar
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) getSupportActionBar()
                .getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.actionbar, null);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP |ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Drawable d=getResources().getDrawable(R.drawable.back);
        actionBar.setHomeAsUpIndicator(d);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff161616));
        Spannable text = new SpannableString("Image Queue");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#3498db")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

        Button addqueue = ( Button) customActionBarView
                .findViewById(R.id.add);
        addqueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                Intent intent = new Intent(view.getContext(), ImageSender.class);
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
                    new image_connect().execute().get();
                }catch(Exception e){
                    Log.d("Exception",e.toString());
                }
                listViewLoaderTask = new ListViewLoaderTask();
                jsonStr = "{ " +
                        " \"imagequeue\": " +responseStr + "} ";

                try{
                    listViewLoaderTask.execute(jsonStr).get();
                }
                catch (Exception e){

                }
                imagelist = ( ListView ) findViewById(R.id.imglist);
                imagequeue = new ImageQueueAdapter(image_queue,imageList,res);
                if (imageList.isEmpty()) isEmpty();
                else imagelist.setAdapter(imagequeue);
            }
        });
        responseStr = "";
        res =getResources();
        image_queue = this;
        imagelist = (ListView)findViewById(R.id.videolist);


        try {
            new image_connect().execute().get();
        }catch(Exception e){
            Log.d("Exception",e.toString());
        }
        listViewLoaderTask = new ListViewLoaderTask();
        jsonStr = "{ " +
                " \"imagequeue\": " +responseStr + "} ";
        //Log.e("REPLY",jsonStr);
        try{
            listViewLoaderTask.execute(jsonStr).get();
        }
        catch (Exception e){

        }
        imagelist = ( ListView ) findViewById(R.id.imglist);
        imagequeue = new ImageQueueAdapter(image_queue,imageList,res);
        if (imageList.isEmpty()) isEmpty();
        else imagelist.setAdapter(imagequeue);


    }

    public void isEmpty(){
        Context context = getApplicationContext();
        CharSequence text = "Queue is Empty";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private class ListViewLoaderTask extends AsyncTask{

        JSONObject jObject;

        /** Doing the parsing of xml data in a non-ui thread */
        @Override
        protected Object doInBackground(Object... arg0) {
            ImageJSONParser imageJSONParser;
            imageJSONParser = new ImageJSONParser();
            try{
                jObject = new JSONObject(jsonStr);
            }catch(Exception e){
                Log.d("JSON Exception1",e.toString());
            }

            imageList = new ArrayList<ImageListModel>();
            try{
                /** Getting the parsed data as a List construct */
                imageList = imageJSONParser.parse(jObject);
                //Log.v("Listhi", "hello");
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return null;

        }

    }

    private class image_connect extends AsyncTask {

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
            DefaultHttpClient httpclient = new DefaultHttpClient();
            URI website = new URI(Constants.SERVER_ADDR_IMAGES);
            HttpGet request = new HttpGet();
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            inputStream = response.getEntity().getContent();
            if(inputStream != null)
               responseStr = convertInputStreamToString(inputStream);
            else
                responseStr = "Did not work!";
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            globalVariable.setimageresponse(responseStr);


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
    public void delete(){
        try {
            new image_delete().execute().get();
            try {
                new image_connect().execute().get();
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            listViewLoaderTask = new ListViewLoaderTask();
            jsonStr = "{ " +
                    " \"imagequeue\": " +responseStr + "} ";
            //Log.e("REPLY",jsonStr);
            try{
                listViewLoaderTask.execute(jsonStr).get();
            }
            catch (Exception e){

            }
            imagelist = ( ListView ) findViewById(R.id.imglist);
            imagequeue = new ImageQueueAdapter(image_queue,imageList,res);
            if (imageList.isEmpty()) isEmpty();
            else imagelist.setAdapter(imagequeue);
        }catch(Exception e){
            Log.d("JSON Exception1",e.toString());
        }

    }

    private class image_delete extends AsyncTask<String, String, Void> {

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
            HttpPost httpost = new HttpPost(Constants.SERVER_ADDR_IMAGES);
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
    public void onItemClick(int mPosition)
    {
        ImageListModel tempValues = (ImageListModel) imageList.get(mPosition);
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
    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        Bundle extras = getIntent().getExtras();
        try {
            new image_connect().execute().get();
        }catch(Exception e){
            Log.d("Exception",e.toString());
        }
        listViewLoaderTask = new ListViewLoaderTask();
        jsonStr = "{ " +
                " \"imagequeue\": " +responseStr + "} ";
        //Log.e("REPLY",jsonStr);
        try{
            listViewLoaderTask.execute(jsonStr).get();
        }
        catch (Exception e){

        }
        imagelist = ( ListView ) findViewById(R.id.imglist);
        imagequeue = new ImageQueueAdapter(image_queue,imageList,res);
        if (imageList.isEmpty()) isEmpty();
        else imagelist.setAdapter(imagequeue);
        // Activity being restarted from stopped state
    }
    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first
        Bundle extras = getIntent().getExtras();

        // Activity being restarted from stopped state
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        Bundle extras = getIntent().getExtras();
        try {
            new image_connect().execute().get();
        }catch(Exception e){
            Log.d("Exception",e.toString());
        }
        listViewLoaderTask = new ListViewLoaderTask();
        jsonStr = "{ " +
                " \"imagequeue\": " +responseStr + "} ";
        //Log.e("REPLY",jsonStr);
        try{
            listViewLoaderTask.execute(jsonStr).get();
        }
        catch (Exception e){

        }
        imagelist = ( ListView ) findViewById(R.id.imglist);
        imagequeue = new ImageQueueAdapter(image_queue,imageList,res);
        if (imageList.isEmpty()) isEmpty();
        else imagelist.setAdapter(imagequeue);
    }
}

