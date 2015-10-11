package cast.ucl.sender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;


import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ImageSelectorFB extends Activity {

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private Button loginButton;
    private Button fetchpic;
    private TextView textViewName;
    private ProfilePictureView profilePictureView;
    private TextView json;
    public ListViewLoaderTask listViewLoaderTask;
    public ListView fblist;
    String jsonStr = "";
    List<HashMap<String, String>> buffer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_image_selector_fb);
        fblist = (ListView)findViewById(R.id.albumlist);
        json = (TextView) findViewById(R.id.textView);
        fetchpic = (Button) findViewById(R.id.getpic);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                updateProfile();
            }

            @Override
            public void onCancel() {
                updateProfile();
            }

            @Override
            public void onError(FacebookException e) {
                updateProfile();
            }
        });

        textViewName = (TextView) findViewById(R.id.textViewName);
        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePictureView);
        loginButton = (Button) findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken token = AccessToken.getCurrentAccessToken();
                if (token == null) {
                    LoginManager.getInstance().logInWithReadPermissions(ImageSelectorFB.this, Arrays.asList("public_profile", "user_friends", "email", "user_photos"));
                } else {
                    LoginManager.getInstance().logOut();
                }
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                updateProfile();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context;
        context = getApplicationContext();
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
           // Toast.makeText(context, token.toString(), Toast.LENGTH_LONG).show();
            Log.v("token", token.toString());
        }
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updateProfile() {
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            profilePictureView.setProfileId(profile.getId());
            textViewName.setText(profile.getName());
            loginButton.setText("Logout");

        } else {
            profilePictureView.setProfileId(null);
            textViewName.setText("not login yet");
            loginButton.setText("Login");
        }
    }

    public void fetch(View view) {
        Intent intent = new Intent(this,PhotoChooser.class);
        startActivity(intent);
//        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "/me/albums",
//                null,
//                HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse response) {
//                        String report;
//                        JSONObject album = null;
//                        JSONArray list;
//                        int count;
//                        //report = "graph response:"+response.toString();
//                        report = "";
//            /* handle the result */
//                        JSONObject reader = response.getJSONObject();
//                       // jsonStr = response.toString();
//
//                        try {
//                        list = reader.getJSONArray("data");
//                            count = 0;
//
//                            while(count != list.length()) {
//                                album = list.getJSONObject(count);
//                                report = report +album.toString();
//                                count++;
//                            }
//                        }
//                        catch (JSONException e){
//
//                        }
//
//                        //json.setText(report);
//                        //Toast.makeText(getApplicationContext(),report,Toast.LENGTH_LONG).show();
//                        jsonStr = "{ " +
//                                " \"data\": " + report + "} ";
//                        //json.setText(reader.toString());
//                        listViewLoaderTask = new ListViewLoaderTask();
//
//                        listViewLoaderTask.execute(reader.toString());
//
//                    }
//                }
//        ).executeAsync();
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

            /** Ids of views in listview_layout */
            int[] to = {R.id.album_id, R.id.album_name};

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
        }
    }
}