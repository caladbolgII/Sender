package cast.ucl.sender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Caladbolg on 04/10/2015.
 */
public class PhotoChooser extends Activity {
    public ListViewLoaderTask listViewLoaderTask;
    public ListView fblist;
    String jsonStr = "";
    List<HashMap<String, String>> buffer = null;
    String a_title;
    String a_id;
    Context viewcontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_fb_photopick);
        fblist = (ListView)findViewById(R.id.photo_grid);
        viewcontext = getApplicationContext();
        fetch2();

    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        fetch2();
    }
    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first
        fetch2();
        // Activity being restarted from stopped state
    }
    public void fetch2() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/albums",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        String report;
                        JSONObject album = null;
                        JSONArray list;
                        int count;
                        //report = "graph response:"+response.toString();
                        report = "";
            /* handle the result */
                        JSONObject reader = response.getJSONObject();
                        // jsonStr = response.toString();

                        try {
                            list = reader.getJSONArray("data");
                            count = 0;

                            while(count != list.length()) {
                                album = list.getJSONObject(count);
                                report = report +album.toString();
                                count++;
                            }
                        }
                        catch (JSONException e){

                        }

                        //json.setText(report);
                        //Toast.makeText(getApplicationContext(),report,Toast.LENGTH_LONG).show();
                        jsonStr = "{ " +
                                " \"data\": " + report + "} ";
                        //json.setText(reader.toString());
                        listViewLoaderTask = new ListViewLoaderTask();

                        listViewLoaderTask.execute(reader.toString());

                    }
                }
        ).executeAsync();
    }

    private class ListViewLoaderTask extends AsyncTask<String, Void, SimpleAdapter> {

        JSONObject jObject;

        /**
         * Doing the parsing of xml data in a non-ui thread
         */
        @Override
        protected SimpleAdapter doInBackground(String... jsonStr) {
            try {
                jObject = new JSONObject(jsonStr[0]);
                //Log.v("log",jsonStr[0]);
                FBJSONParser fbJsonParser = new FBJSONParser();
                fbJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("JSON Exception1", e.toString());
            }

            FBJSONParser fbJsonParser = new FBJSONParser();

            List<HashMap<String, String>> fbs = null;

            try {
                /** Getting the parsed data as a List construct */
                fbs = fbJsonParser.parse(jObject);
                buffer = fbs;
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }

            /** Keys used in Hashmap */
            String[] from = {"id", "name"};
            //String[] from = {"name"};
            /** Ids of views in listview_layout */
            int[] to = {R.id.album_id, R.id.album_name};
            //int[] to = { R.id.album_name};
            /** Instantiating an adapter to store each items
             *  R.layout.listview_layout defines the layout of each item
             */
            SimpleAdapter adapter = null;

            adapter = new SimpleAdapter(getBaseContext(), fbs, R.layout.activity_fb_item, from, to);

            return adapter;
        }
        @Override
        protected void onPostExecute(SimpleAdapter adapter) {
            fblist.setAdapter(adapter);
            fblist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    TextView album_id = (TextView) view.findViewById(R.id.album_id);
                    a_id = album_id.getText().toString();
                    TextView album_title = (TextView) view.findViewById(R.id.album_name);
                    a_title = album_title.getText().toString();
                    Intent intent = new Intent(viewcontext,AlbumViewer.class);
                    intent.putExtra("title",a_title);
                    intent.putExtra("id",a_id);
                    startActivity(intent);
                }

            });
        }
    }

}
