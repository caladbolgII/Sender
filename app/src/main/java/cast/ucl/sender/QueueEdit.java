package cast.ucl.sender;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
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
    public QueueEdit queue_activity = null;
    String videoItem = "";
    private ArrayList<VideoListModel> videoList = new ArrayList<VideoListModel>();
    public ListViewLoaderTask listViewLoaderTask;
    String jsonStr = "";
    Resources res;
    ProgressDialog progressDialog;
    String id;
    String vidid = "";
    String vidurl = "";
    String vidtimeout = "";
    private ListView textlist;
    AlertDialog alert;
    Context viewcontext;
    long t1 = 0;
    long t2 = 0;
    Button editqueue;
    AnimationDrawable d;

    @Override
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
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        setContentView(R.layout.activity_vidcast);


        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff2196f3));
        Spannable text = new SpannableString("Videos");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#e9e9e9")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

        TextView title = (TextView)customActionBarView.findViewById(R.id.action_title);
        title.setText(text);

        editqueue = (Button) customActionBarView
                .findViewById(R.id.edit);
        d=(AnimationDrawable)editqueue.getCompoundDrawables()[0];
        editqueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.start();
                int i = globalVariable.getclick();
                i++;
                globalVariable.setclick(i);
                try {
                    new video_connect().execute().get();
                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }
                listViewLoaderTask = new ListViewLoaderTask();
                jsonStr = "{ " +
                        " \"videoqueue\": " + responseStr + "} ";
                try {
                    listViewLoaderTask.execute(jsonStr);
                } catch (Exception e) {

                }

            }
        });


        res = getResources();
        queue_activity = this;
        try {
            new video_connect().execute().get();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        listViewLoaderTask = new ListViewLoaderTask();
        jsonStr = "{ " +
                " \"videoqueue\": " + responseStr + "} ";

        try {
            listViewLoaderTask.execute(jsonStr);
        } catch (Exception e) {

        }

        LinearLayout linear =(LinearLayout)customActionBarView.findViewById(R.id.action_queue);
        linear.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                int i = globalVariable.getclick();
                i++;
                globalVariable.setclick(i);
            }
        });

        TextView actiontitle = (TextView)customActionBarView.findViewById(R.id.action_title);
        actiontitle.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                int i = globalVariable.getclick();
                i++;
                globalVariable.setclick(i);
            }
        });


    }

    public void addonClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
        Intent intent = new Intent(view.getContext(), SearchActivity.class);
        startActivity(intent);

    }


    public void isEmpty() {
        Context context = getApplicationContext();
        CharSequence text = "Queue is Empty";
        int duration = Toast.LENGTH_SHORT;
        ListView listView;
        listView = (ListView) findViewById(R.id.videolist);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.emptyElement));
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    private class ListViewLoaderTask extends AsyncTask<String, String, Void> {

        JSONObject jObject;

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListView listView;
            listView = (ListView) findViewById(R.id.videolist);
            adapter = new VideoQueueAdapter(queue_activity, videoList, res);

            if (videoList.isEmpty()) isEmpty();
            else listView.setAdapter(adapter);
            progressDialog.dismiss();
            t2 = System.currentTimeMillis();
            String strlong = Long.toString(t2 - t1);
            Log.e("Time to execute", strlong);
            d.stop();
        }

        /**
         * Doing the parsing of xml data in a non-ui thread
         */
        @Override
        protected Void doInBackground(String... arg0) {
            VideoJSONParser videoJSONParser;
            videoJSONParser = new VideoJSONParser();
            try {
                jObject = new JSONObject(jsonStr);

            } catch (Exception e) {
                Log.d("JSON Exception1", e.toString());
            }
            videoList = new ArrayList<VideoListModel>();
            try {
                /** Getting the parsed data as a List construct */
                videoList = videoJSONParser.parse(jObject);
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }


            return null;

        }

    }


    public void delete() {
        try {

            new video_delete().execute().get();
            try {
                new video_connect().execute().get();
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            listViewLoaderTask = new ListViewLoaderTask();
            jsonStr = "{ " +
                    " \"videoqueue\": " + responseStr + "} ";
            try {
                listViewLoaderTask.execute(jsonStr);
            } catch (Exception e) {

            }
        } catch (Exception e) {
            Log.d("JSON Exception1", e.toString());
        }

    }

    private class video_delete extends AsyncTask<String, String, Void> {

        /**
         * Doing the parsing of xml data in a non-ui thread
         */
        @Override
        protected Void doInBackground(String... arg0) {
            connect1();
            return null;
        }

    }

    private void connect1() {
        try {
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("action", "delete");

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
        } catch (JSONException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
    }

    private class video_connect extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            t1 = System.currentTimeMillis();
            progressDialog = ProgressDialog.show(QueueEdit.this, "Retrieving Video Queue", "Please Wait ...");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        /**
         * Doing the parsing of xml data in a non-ui thread
         */
        @Override
        protected Void doInBackground(String... arg0) {
            connect();
            return null;
        }

    }

    private void connect() {
        try {
            // String json = "";
            InputStream inputStream = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            URI website = new URI(Constants.SERVER_GET_VIDEO);
            Log.e("server:", Constants.SERVER_GET_VIDEO);
            HttpGet request = new HttpGet();
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            // receive response as inputStream
            inputStream = response.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                responseStr = convertInputStreamToString(inputStream);
            else
                responseStr = "Did not work!";
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            globalVariable.setvideoresponse(responseStr);

        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (URISyntaxException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
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

    public void onItemClick(int mPosition, View view) {
        VideoListModel tempValues = (VideoListModel) videoList.get(mPosition);
        final CharSequence[] items = {"Edit", "Delete"};
        id = tempValues.getId();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        TextView text_id = (TextView) view.findViewById(R.id.item_id);
        vidid = text_id.getText().toString();
        TextView text_timeout = (TextView) view.findViewById(R.id.item_timeout);
        vidtimeout = text_timeout.getText().toString();
        TextView text_title = (TextView) view.findViewById(R.id.vid_url);
        vidurl = text_title.getText().toString();
        // set title
        alertDialogBuilder.setTitle("Select Action");
        viewcontext = view.getContext();
        alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if (item == 0) {
                    alert.dismiss();
                    Intent intent = new Intent(viewcontext, VideoEdit.class);
                    intent.putExtra("id", vidid);
                    intent.putExtra("timeout", vidtimeout);
                    intent.putExtra("url", vidurl);
                    startActivity(intent);
                } else if (item == 1) {
                    delete();
                    alert.dismiss();
                }
            }
        });
        alert = alertDialogBuilder.create();
        alert.show();


        Log.e("Delete id:", id);
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();  // Always call the superclass method first
//        Bundle extras = getIntent().getExtras();
//
//        try {
//            new video_connect().execute().get();
//        } catch (Exception e) {
//            Log.d("Exception", e.toString());
//        }
//        listViewLoaderTask = new ListViewLoaderTask();
//        jsonStr = "{ " +
//                " \"videoqueue\": " + responseStr + "} ";
//        try {
//            listViewLoaderTask.execute(jsonStr);
//        } catch (Exception e) {
//
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first
        Bundle extras = getIntent().getExtras();

        // Activity being restarted from stopped state
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);//must store the new intent unless getIntent() will return the old one
//        Bundle extras = getIntent().getExtras();
//
//        try {
//            new video_connect().execute().get();
//        } catch (Exception e) {
//            Log.d("Exception", e.toString());
//        }
//        listViewLoaderTask = new ListViewLoaderTask();
//        jsonStr = "{ " +
//                " \"videoqueue\": " + responseStr + "} ";
//        try {
//            listViewLoaderTask.execute(jsonStr);
//        } catch (Exception e) {
//
//        }
//
//    }
}