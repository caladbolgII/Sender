package cast.ucl.sender;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    final Context context = this;
    private static final int REQUEST_CODE = 1;
    GlobalClass globalVariable;
    private GoogleApiClient mApiClient;
    private Cast.Listener mCastListener;
    private GoogleApiClient.ConnectionCallbacks mConnectionCallbacks;
    private ConnectionFailedListener mConnectionFailedListener;
    private HelloWorldChannel mHelloWorldChannel;
    private boolean mApplicationStarted;
    private boolean mWaitingForReconnect;
    private String mSessionId;
    Button layout,layout1, layout2, layout3;
    private static final String TAG = MainActivity.class
            .getSimpleName();

    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private MediaRouter.Callback mMediaRouterCallback;
    private MediaRouteButton mMediaRouteButton;
    private CastDevice mSelectedDevice;
    private int mRouteCount = 0;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set layout and actionbar layout of activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        globalVariable= (GlobalClass) getApplicationContext();
        globalVariable.setclick(0);
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) getSupportActionBar()
                .getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.actionbar_main, null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff2196f3));
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        //next button functions
        Button send = (Button) customActionBarView
                .findViewById(R.id.next);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                String layouttemp = "";

                if(globalVariable.getlayout() == "layout1") {
                    sendMessage("setLayout1");
                    Toast.makeText(getApplicationContext(), globalVariable.getlayout(),
                            Toast.LENGTH_LONG).show();
                    go_back();
                }
                else if(globalVariable.getlayout() == "layout2") {
                    sendMessage("setLayout2");
                    Toast.makeText(getApplicationContext(), globalVariable.getlayout(),
                            Toast.LENGTH_LONG).show();
                    go_back();
                }
                else if(globalVariable.getlayout() == "layout3") {
                    sendMessage("setLayout3");
                    Toast.makeText(getApplicationContext(), globalVariable.getlayout(),
                            Toast.LENGTH_LONG).show();
                    go_back();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please select a layout before proceeding",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        layout = (Button)findViewById(R.id.layout_button);
        layout1 = (Button)findViewById(R.id.l1);
        layout2 = (Button)findViewById(R.id.l2);
        layout3 = (Button)findViewById(R.id.l3);

        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        // Create a MediaRouteSelector for the type of routes your app supports
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(
                        CastMediaControlIntent.categoryForCast(getResources()
                                .getString(R.string.app_id))).build();
        // Create a MediaRouter callback for discovery events
        mMediaRouterCallback = new MyMediaRouterCallback();

        // Set the MediaRouteButton selector for device discovery.
        mMediaRouteButton = (MediaRouteButton) findViewById(R.id.media_route_button);
        mMediaRouteButton.setRouteSelector(mMediaRouteSelector);
        //misclick counter
        LinearLayout linear =(LinearLayout)customActionBarView.findViewById(R.id.action_layout);
        linear.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
               int i = globalVariable.getclick();
                i++;
                globalVariable.setclick(i);
            }
        });

        TextView title = (TextView)customActionBarView.findViewById(R.id.action_title);
        title.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                int i = globalVariable.getclick();
                i++;
                globalVariable.setclick(i);
            }
        });

        //check network availability

        if(isNetworkAvailable()){
            //proceed normally
        }

        else{
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Error: No network connection");
            alertDialog.setMessage("Your device is not connected to the internet. Please connect to the EEEI-TV network before restarting the application");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //System.exit(0);
                        }
                    });
            alertDialog.show();

        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void post(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
        String layouttemp = "";

        if(globalVariable.getlayout() == "layout1") {
            sendMessage("setLayout1");
//            Toast.makeText(getApplicationContext(), globalVariable.getlayout(),
//                    Toast.LENGTH_LONG).show();
            go_back();
        }
        else if(globalVariable.getlayout() == "layout2") {
            sendMessage("setLayout2");
//            Toast.makeText(getApplicationContext(), globalVariable.getlayout(),
//                    Toast.LENGTH_LONG).show();
            go_back();
        }
        else if(globalVariable.getlayout() == "layout3") {
            sendMessage("setLayout3");
//            Toast.makeText(getApplicationContext(), globalVariable.getlayout(),
//                    Toast.LENGTH_LONG).show();
            go_back();
        }
        else{
            Toast.makeText(getApplicationContext(), "Please select a layout before proceeding",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void layout_selector(View view){
        Intent intent = new Intent(view.getContext(), LayoutSelector.class);
        startActivity(intent);

    }
    public void set_layout1(View view){

        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
        globalVariable.setlayoutmsg("setLayout1");
        globalVariable.setlayout("layout1");
        layout.setBackgroundResource(R.drawable.screen);
        layout1.setBackgroundResource(R.drawable.layout1_sel);
        layout2.setBackgroundResource(R.drawable.layout2);
        layout3.setBackgroundResource(R.drawable.layout3);
        layout.setText("");

    }
    public void set_layout2(View view){
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
        globalVariable.setlayoutmsg("setLayout2");
        globalVariable.setlayout("layout2");
        layout.setBackgroundResource(R.drawable.screen2);
        layout1.setBackgroundResource(R.drawable.layout1);
        layout2.setBackgroundResource(R.drawable.layout2_sel);
        layout3.setBackgroundResource(R.drawable.layout3);

        layout.setText("");

    }
    public void set_layout3(View view){
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
        globalVariable.setlayoutmsg("setLayout3");
        globalVariable.setlayout("layout3");
        layout.setBackgroundResource(R.drawable.screen3);
        layout1.setBackgroundResource(R.drawable.layout1);
        layout2.setBackgroundResource(R.drawable.layout2);
        layout3.setBackgroundResource(R.drawable.layout3_sel);
        layout.setText("");

    }


    public void go_back() {
        Intent intent = new Intent(this, Selection.class);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0) {
                Log.d(TAG, matches.get(0));
                sendMessage(matches.get(0));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start media router discovery
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }

    @Override
    protected void onPause() {
        if (isFinishing()) {
            // End media router discovery
            mMediaRouter.removeCallback(mMediaRouterCallback);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        //
         //teardown();
        super.onDestroy();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
//        MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) MenuItemCompat
//               .getActionProvider(mediaRouteMenuItem);
//         //Set the MediaRouteActionProvider selector for device discovery.
//        mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    /**
     * Callback for MediaRouter events
     */
    private class MyMediaRouterCallback extends MediaRouter.Callback {

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            Log.d(TAG, "onRouteSelected");
            // Handle the user route selection.
            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());

            launchReceiver();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            Log.d(TAG, "onRouteUnselected: info=" + info);
            teardown();
            mSelectedDevice = null;
        }
    }

    /**
     * Start the receiver app
     */
    private void launchReceiver() {
        try {
            mCastListener = new Cast.Listener() {

                @Override
                public void onApplicationDisconnected(int errorCode) {
                    Log.d(TAG, "application has stopped");
                   teardown();
                }

            };
            // Connect to Google Play services
            mConnectionCallbacks = new ConnectionCallbacks();
            mConnectionFailedListener = new ConnectionFailedListener();
            Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
                    .builder(mSelectedDevice, mCastListener);
            mApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Cast.API, apiOptionsBuilder.build())
                    .addConnectionCallbacks(mConnectionCallbacks)
                    .addOnConnectionFailedListener(mConnectionFailedListener)
                    .build();

            mApiClient.connect();
            //Intent i = new Intent(MainActivity.this, LayoutSelector.class);
            //startActivity(i);
        } catch (Exception e) {
            Log.e(TAG, "Failed launchReceiver", e);
        }
    }

    /**
     * Google Play services callbacks
     */
    private class ConnectionCallbacks implements
            GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(Bundle connectionHint) {
            Log.d(TAG, "onConnected");

            if (mApiClient == null) {
                // We got disconnected while this runnable was pending
                // execution.
                return;
            }

            try {
                if (mWaitingForReconnect) {
                    mWaitingForReconnect = false;

                    // Check if the receiver app is still running
                    if ((connectionHint != null)
                            && connectionHint
                            .getBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING)) {
                        Log.d(TAG, "App  is no longer running");
                        //teardown();
                    } else {
                        // Re-create the custom message channel
                        try {
                            Cast.CastApi.setMessageReceivedCallbacks(
                                    mApiClient,
                                    mHelloWorldChannel.getNamespace(),
                                    mHelloWorldChannel);
                        } catch (IOException e) {
                            Log.e(TAG, "Exception while creating channel", e);
                        }
                    }
                } else {
                    // Launch the receiver app
                    Cast.CastApi
                            .launchApplication(mApiClient,
                                    getString(R.string.app_id), false)
                            .setResultCallback(
                                    new ResultCallback<Cast.ApplicationConnectionResult>() {
                                        @Override
                                        public void onResult(
                                                Cast.ApplicationConnectionResult result) {
                                            Status status = result.getStatus();
                                            Log.d(TAG,
                                                    "ApplicationConnectionResultCallback.onResult: statusCode"
                                                            + status.getStatusCode());
                                            if (status.isSuccess()) {
                                                ApplicationMetadata applicationMetadata = result
                                                        .getApplicationMetadata();
                                                mSessionId = result
                                                        .getSessionId();
                                                String applicationStatus = result
                                                        .getApplicationStatus();
                                                boolean wasLaunched = result
                                                        .getWasLaunched();
                                                Log.d(TAG,
                                                        "application name: "
                                                                + applicationMetadata
                                                                .getName()
                                                                + ", status: "
                                                                + applicationStatus
                                                                + ", sessionId: "
                                                                + mSessionId
                                                                + ", wasLaunched: "
                                                                + wasLaunched);
                                                mApplicationStarted = true;

                                                // Create the custom message
                                                // channel
                                                mHelloWorldChannel = new HelloWorldChannel();
                                                try {
                                                    Cast.CastApi
                                                            .setMessageReceivedCallbacks(
                                                                    mApiClient,
                                                                    mHelloWorldChannel
                                                                            .getNamespace(),
                                                                    mHelloWorldChannel);
                                                    Log.e(TAG, "Cast api callbacks set");
                                                } catch (IOException e) {
                                                    Log.e(TAG,
                                                            "Exception while creating channel",
                                                            e);
                                                }

                                                // set the initial instructions
                                                // on the receiver
                                                //sendMessage(getString(R.string.instructions));
                                            } else {
                                                Log.e(TAG,
                                                        "application could not launch");
                                                teardown();
                                            }
                                        }
                                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to launch application", e);
            }
        }

        @Override
        public void onConnectionSuspended(int cause) {
            Log.d(TAG, "onConnectionSuspended");
            mWaitingForReconnect = true;
        }
    }
    /**
     * Send a text message to the receiver
     *
     * @param message
     */
    private void sendMessage(String message) {
        if (mApiClient != null && mHelloWorldChannel != null) {
            try {
                Cast.CastApi.sendMessage(mApiClient,
                        mHelloWorldChannel.getNamespace(), message)
                        .setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status result) {
                                if (!result.isSuccess()) {
                                    Log.e(TAG, "Sending message failed");
                                }
//                                else Toast.makeText(MainActivity.this,"Message Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                Log.e(TAG, "Exception while sending message", e);
            }
        } else {
            //Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Google Play services callbacks
     */
    private class ConnectionFailedListener implements
            GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            Log.e(TAG, "onConnectionFailed ");

            teardown();
        }
    }

    /**
     * Tear down the connection to the receiver
     */
    private void teardown() {
        Log.d(TAG, "teardown");
        if (mApiClient != null) {
            if (mApplicationStarted) {
                if (mApiClient.isConnected()  || mApiClient.isConnecting()) {
                    try {
                        Cast.CastApi.stopApplication(mApiClient, mSessionId);
                        if (mHelloWorldChannel != null) {
                            Cast.CastApi.removeMessageReceivedCallbacks(
                                    mApiClient,
                                    mHelloWorldChannel.getNamespace());
                            mHelloWorldChannel = null;
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception while removing channel", e);
                    }
                    mApiClient.disconnect();
                }
                mApplicationStarted = false;
            }
            mApiClient = null;
        }
        mSelectedDevice = null;
        mWaitingForReconnect = false;
        mSessionId = null;
    }
    /**
     * Custom message channel
     */
    class HelloWorldChannel implements Cast.MessageReceivedCallback {

        /**
         * @return custom namespace
         */
        public String getNamespace() {
            return getString(R.string.namespace);
        }

        /*
         * Receive message from the receiver app
         */
        @Override
        public void onMessageReceived(CastDevice castDevice, String namespace,
                                      String message) {
            Log.d(TAG, "onMessageReceived: " + message);
        }

    }

    }

