package cast.ucl.sender;

/**
 * Created by LENOVO on 2/20/2015.
 */

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;
import com.squareup.picasso.Picasso;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ImageSender extends ActionBarActivity implements View.OnClickListener {
// variable declarations
   // public TextView myTextField;
    public EditText TitleField;
    public DatePicker imagedeadline;
    private static final int TAKE_PHOTO = 1;
    private Button btnUpload, btnDownload;
    private final String DIR = "/";
    private File f;
    private boolean mLoggedIn, onResume;
    private DropboxAPI<AndroidAuthSession> mApi;
    public String deaddate= "";
    public String command;
    public TimePicker deadtime;
    public  Spinner spinner;
    public String type = "";
    public ImageView imgthumb;
    String url;
    String yy,mm,dd,hh,mi,MM;
    GlobalClass globalVariable;
    String timeString="";
    public TextView exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image);
        //myTextField = (TextView) findViewById(R.id.imagelink);
        TitleField = (EditText)findViewById(R.id.image_title);
        imgthumb = (ImageView)findViewById(R.id.imgthumb);
        //imagedeadline = (DatePicker) findViewById(R.id.deadline_image);
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) getSupportActionBar()
                .getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.actionbar_sender, null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff2196f3));
        Spannable text = new SpannableString("Message Sender");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#2196f3")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mm = "";
        mi = "";
        actionBar.setTitle(text);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        TextView title = (TextView)customActionBarView.findViewById(R.id.action_title);
        title.setText(text);

        Button send = (Button) customActionBarView
                .findViewById(R.id.next);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                if(mm.length() <1 || mi.length()<1){

                    CharSequence text = "Please set date and time";
                    int duration = Toast.LENGTH_SHORT;
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                attemptSend(view);
                }
            }
        });
        exp = (TextView) findViewById(R.id.expiry_image);
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);

        // checkAppKeySetup();
        setLoggedIn(false);

        //imagedeadline = (DatePicker) findViewById(R.id.deadline_image);
        //deadtime = (TimePicker)findViewById(R.id.imgtimePicker);
        //deadtime.setIs24HourView(Boolean.TRUE);
        spinner = (Spinner) findViewById(R.id.imgspinner);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classification_array, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        spinner.setSelection(4);
    }

    public void setdate(View view){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            // the callback received when the user "sets" the Date in the DatePickerDialog
            @Override
            public void onDateSet(DatePicker view, int yearSelected, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minutes = c.get(Calendar.MINUTE);

                if (hour == 0) {
                    timeString =  "AM";
                } else if (hour < 12) {
                    timeString =  "AM";
                } else if (hour == 12) {
                    timeString = "PM";
                } else {
                    timeString = "PM";
                }

                c.get(Calendar.AM_PM);
                MM = c.getDisplayName(c.MONTH, Calendar.LONG, Locale.US);
                mm = String.format("%02d", monthOfYear+1);
                dd = String.format("%02d", dayOfMonth);
                yy = Integer.toString(yearSelected);
                final TimePickerDialog timePickerDialog = new TimePickerDialog(ImageSender.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                        hh = Integer.toString(hours);
                        mi = Integer.toString(minutes);
                        Toast.makeText(getApplicationContext(), "Time selected is:  "+hh+":"+mi, Toast.LENGTH_SHORT).show();
                        exp.setText("  "+MM + " "+ dd+ ","+ " " + yy+" "+hh+":"+mi);
                    }

                }, hour, minutes, true);
                timePickerDialog.setTitle("Set Image Expiry Time");
                timePickerDialog.show();

                int monthSelected = monthOfYear;
                int daySelected = dayOfMonth;
                Toast.makeText(getApplicationContext(), "Date selected is:  "+daySelected+"-"+monthSelected+"-"+yearSelected, Toast.LENGTH_SHORT).show();
            }
        },mYear,mMonth,mDay);
        datePickerDialog.setTitle("Set Image Expiry Date");
        datePickerDialog.show();


    }


    public void attemptSend(View view){

        deaddate = mm + " "+ dd+ " " + yy+ " "+hh+":"+mi;
        command = Constants.action_add_image;
        if(TitleField.getText().toString().length()==0) {
            Context context = getApplicationContext();
            CharSequence text = "Please give a title to your post";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {
            try {
                new Connection().execute();
            } catch (Exception e) {
                Log.d("JSON Exception1", e.toString());
            }

            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            Context context = getApplicationContext();
            int click = globalVariable.getclick();
            CharSequence text = "Image has been added to queue," + "There have been: "+click+" Misclicks";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            globalVariable.setclick(0);
            go_back();
        }
    }


    public void go_back() {
        Intent intent = new Intent(this,ImageQueueEdit.class);
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
            jsonObject.accumulate("action", command);
            jsonObject.accumulate("image_url",url);
            jsonObject.accumulate("title",TitleField.getText());
            jsonObject.accumulate("classification",type);
            jsonObject.accumulate("deadline", deaddate);

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

    //dropbox functions here
    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(Constants.DROPBOX_APP_KEY,
                Constants.DROPBOX_APP_SECRET);
        AndroidAuthSession session;

        String[] stored = getKeys();
        if (stored != null) {
            AccessTokenPair accessToken = new AccessTokenPair(stored[0],
                    stored[1]);
            session = new AndroidAuthSession(appKeyPair, Constants.ACCESS_TYPE,
                    accessToken);
        } else {
            session = new AndroidAuthSession(appKeyPair, Constants.ACCESS_TYPE);
        }

        return session;
    }

    private String[] getKeys() {
        SharedPreferences prefs = getSharedPreferences(
                Constants.ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(Constants.ACCESS_KEY_NAME, null);
        String secret = prefs.getString(Constants.ACCESS_SECRET_NAME, null);
        if (key != null && secret != null) {
            String[] ret = new String[2];
            ret[0] = key;
            ret[1] = secret;
            return ret;
        } else {
            return null;
        }
    }
    private void logOut() {
        mApi.getSession().unlink();

        clearKeys();
    }

    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(
                Constants.ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    private void createDir() {
        File dir = new File(Utils.getPath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {
//				f = new File(Utils.getPath() + "/temp.jpg");
                if (Utils.isOnline(ImageSender.this)) {
                    mApi.getSession().startAuthentication(ImageSender.this);
                    onResume = true;
                } else {
                    Utils.showNetworkAlert(ImageSender.this);
                }
            }
        }
    }

    public void setLoggedIn(boolean loggedIn) {
        mLoggedIn = loggedIn;
        if (loggedIn) {
            UploadFile upload = new UploadFile(ImageSender.this, mApi, DIR, f);
            upload.execute();
            onResume = false;

        }
    }

    private void storeKeys(String key, String secret) {
        SharedPreferences prefs = getSharedPreferences(
                Constants.ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(Constants.ACCESS_KEY_NAME, key);
        edit.putString(Constants.ACCESS_SECRET_NAME, secret);
        edit.commit();
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

    @Override
    protected void onResume() {

        AndroidAuthSession session = mApi.getSession();

        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();

                TokenPair tokens = session.getAccessTokenPair();
                storeKeys(tokens.key, tokens.secret);
                setLoggedIn(onResume);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:"
                        + e.getLocalizedMessage());
            }
        }
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("imgurl");
//            myTextField.setText(extras.getString("imgurl"));
        }
    }
    public void onClick(View v) {
        if (v == btnDownload) {
            startActivity(new Intent(ImageSender.this, DropboxDownload.class));
        } else if (v == btnUpload) {
            createDir();
            if (mLoggedIn) {
                logOut();
            }
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            f = new File(Utils.getPath(),new Date().getTime()+".jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, TAKE_PHOTO);
        }
    }
    public void choosefb(View v) {

            startActivity(new Intent(ImageSender.this, ImageSelectorFB.class));


    }
    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("imgurl");
            Picasso.with(getApplicationContext()).load(url).resize(300,150).into(imgthumb);
//            myTextField.setText(extras.getString("imgurl"));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("imgurl");
            Picasso.with(getApplicationContext()).load(url).resize(300,150).into(imgthumb);
//            myTextField.setText(extras.getString("imgurl"));
        }
        // Activity being restarted from stopped state
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("imgurl");
            Picasso.with(getApplicationContext()).load(url).resize(300,150).into(imgthumb);
//            myTextField.setText(extras.getString("imgurl"));
        }

    }


}