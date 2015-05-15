package cast.ucl.sender;

import android.app.AlertDialog;
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
import android.widget.AdapterView;
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

/**
 * Created by LENOVO on 3/6/2015.
 */
public class TextQueueEdit extends ActionBarActivity{

    String responseStr;
    String textid = "";
    String texttitle = "";
    String texttype = "";
    String textmessage = "";
    String texttimeout = "";
    private ListView textlist;
    public TextQueueEdit text_queue;
    private ArrayList<ImageListModel> imageList = new ArrayList<ImageListModel>();
    public ListViewLoaderTask listViewLoaderTask;
    String jsonStr = "";
    Resources res;
    List<HashMap<String, String>> buffer = null;
    AlertDialog alert;
    Context viewcontext;

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

        setContentView(R.layout.activity_txtcast);

       // Bundle bundle = getIntent().getExtras();
        //String var_from_prev_intent = bundle.getString("response");

        text_queue = this;

        Button addqueue = ( Button) customActionBarView
                .findViewById(R.id.add);
        addqueue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                Intent intent = new Intent(view.getContext(), text.class);
                startActivity(intent);

            }
        });
        Button refreshqueue = (Button)customActionBarView.findViewById(R.id.edit);
        refreshqueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                try {
                    new text_connect().execute().get();
                }catch(Exception e){
                    Log.d("Exception",e.toString());
                }

                //readFromFile();
                jsonStr = "{ " +
                        " \"textqueue\": " + responseStr + "} ";

                listViewLoaderTask = new ListViewLoaderTask();

                listViewLoaderTask.execute(jsonStr);
            }
        });
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
        Drawable d=getResources().getDrawable(R.drawable.back);
        actionBar.setHomeAsUpIndicator(d);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff262626));
        Spannable text = new SpannableString("Message Queue");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#ecf0f1")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        textlist = ( ListView ) findViewById(R.id.textlist);
        try {
            new text_connect().execute().get();
        }catch(Exception e){
            Log.d("Exception",e.toString());
        }

        //readFromFile();
        jsonStr = "{ " +
                " \"textqueue\": " + responseStr + "} ";

        listViewLoaderTask = new ListViewLoaderTask();

        listViewLoaderTask.execute(jsonStr);



    }

    public void isEmpty(){
        Context context = getApplicationContext();
        CharSequence text = "Queue is Empty";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    private class ListViewLoaderTask extends AsyncTask<String, Void, SimpleAdapter>{

        JSONObject jObject;
        /** Doing the parsing of xml data in a non-ui thread */
        @Override
        protected SimpleAdapter doInBackground(String... jsonStr) {
            try{
                jObject = new JSONObject(jsonStr[0]);
                TextJSONParser textJsonParser = new TextJSONParser();
                textJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("JSON Exception1",e.toString());
            }

            TextJSONParser textJsonParser = new TextJSONParser();

            List<HashMap<String, String>> texts = null;

            try{
                /** Getting the parsed data as a List construct */
                texts = textJsonParser.parse(jObject);
                buffer = texts;
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }

            /** Keys used in Hashmap */
            String[] from = { "_id","title","type","text","time_out"};

            /** Ids of views in listview_layout */
            int[] to = { R.id.item_id,R.id.text_title,R.id.text_type,R.id.message_item,R.id.item_timeout};

            /** Instantiating an adapter to store each items
             *  R.layout.listview_layout defines the layout of each item
             */
            SimpleAdapter adapter = null;

                adapter = new SimpleAdapter(getBaseContext(), texts, R.layout.activity_text_item, from, to);

            return adapter;
        }

        /** Invoked by the Android system on "doInBackground" is executed completely */
        /** This will be executed in ui thread */
        @Override
        protected void onPostExecute(SimpleAdapter adapter) {




            if (buffer.isEmpty()) isEmpty();
            else {


                textlist.setAdapter(adapter);
                textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        TextView text_id = (TextView) view.findViewById(R.id.item_id);
                        textid = text_id.getText().toString();
                        TextView text_timeout = (TextView) view.findViewById(R.id.item_timeout);
                        texttimeout = text_timeout.getText().toString();
                        TextView text_title = (TextView) view.findViewById(R.id.text_title);
                        texttitle = text_title.getText().toString();
                        TextView text_message = (TextView) view.findViewById(R.id.message_item);
                        textmessage = text_message.getText().toString();
                        TextView text_type = (TextView) view.findViewById(R.id.text_type);
                        texttype = text_type.getText().toString();
                        final CharSequence[] items = {"Edit Item", "Delete Item"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(text_queue);
                        builder.setTitle("Select Action");
                        viewcontext = view.getContext();
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // Do something with the selection
                                if (item == 0) {
                                    alert.dismiss();
                                    Intent intent = new Intent(viewcontext,TextEdit.class);
                                    intent.putExtra("id",textid);
                                    intent.putExtra("timeout",texttimeout);
                                    intent.putExtra("message",textmessage);
                                    intent.putExtra("title",texttitle);
                                    intent.putExtra("type",texttype);
                                    startActivity(intent);
                                }
                                else if (item == 1) {
                                    delete();
                                    alert.dismiss();
                                }
                            }
                        });
                        alert= builder.create();
                        alert.show();

                        //s delete();

                    }
                });
            }
        }
    }



    private class text_connect extends AsyncTask {

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
            URI website = new URI(Constants.SERVER_ADDR_TEXTS);
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

//            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
//            globalVariable.settextresponse(responseStr);

            // TextView httpresponse = (TextView) findViewById(R.id.http_queue);
            // httpresponse.setTexft(globalVariable.getimageresponse());

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

    public void delete(){
        try {

            new text_delete().execute().get();
            try {
                new text_connect().execute().get();
            }catch(Exception e){
                Log.d("Exception", e.toString());
            }
            listViewLoaderTask = new ListViewLoaderTask();
            jsonStr = "{ " +
                    " \"textqueue\": " +responseStr + "} ";
            try{
                listViewLoaderTask.execute(jsonStr);
            }
            catch (Exception e){

            }
        }catch(Exception e){
            Log.d("JSON Exception1",e.toString());
        }

    }

    private class text_delete extends AsyncTask<String, String, Void> {

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

            jsonObject.accumulate("id", textid);

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(Constants.SERVER_ADDR_TEXTS);
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
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        Bundle extras = getIntent().getExtras();
        try {
            new text_connect().execute().get();
        }catch(Exception e){
            Log.d("Exception",e.toString());
        }

        //readFromFile();
        jsonStr = "{ " +
                " \"textqueue\": " + responseStr + "} ";

        listViewLoaderTask = new ListViewLoaderTask();

        listViewLoaderTask.execute(jsonStr);
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
            new text_connect().execute().get();
        }catch(Exception e){
            Log.d("Exception",e.toString());
        }

        //readFromFile();
        jsonStr = "{ " +
                " \"textqueue\": " + responseStr + "} ";

        listViewLoaderTask = new ListViewLoaderTask();

        listViewLoaderTask.execute(jsonStr);
    }
}

