package cast.ucl.sender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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

public class ImageSelectorFB extends AppCompatActivity {

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private Button loginButton;
    private Button fetchpic;
    private TextView textViewName;
    private ProfilePictureView profilePictureView;
    private TextView json;
    public ListView fblist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_image_selector_fb);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff2196f3));
        Spannable text = new SpannableString("Facebook Login");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#e9e9e9")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        json = (TextView) findViewById(R.id.textView);
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
            fetch();

        } else {
            profilePictureView.setProfileId(null);
            textViewName.setText("not login yet");
            loginButton.setText("Login");
        }
    }

    public void gotoalbum() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            Intent intent = new Intent(this,PhotoChooser.class);
            startActivity(intent);
        }

    }

    public void fetch() {
        Intent intent = new Intent(this,PhotoChooser.class);
        startActivity(intent);
    }

}