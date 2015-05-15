package cast.ucl.sender;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by LENOVO on 3/17/2015.
 */

public class DropboxDownload extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private DropboxAPI.Entry fileSelected;
    private DropboxAPI.DropboxLink shareLink;
    public String shareurl;
    private DropboxAPI<AndroidAuthSession> mApi;
    private String DIR = "/";
    private ArrayList<DropboxAPI.Entry> files;
    private ArrayList<String> dir;
    private boolean isItemClicked = false;
    private ListView lvDropboxDownloadFilesList;
    private ProgressDialog pd;
    public static final String TAG = DropboxDownload.class.getSimpleName();
    ProgressDialog progressDialog;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                lvDropboxDownloadFilesList.setAdapter(new DownloadFileAdapter(
                        DropboxDownload.this, files));
                pd.dismiss();
            } else if (msg.what == 1) {
                Toast.makeText(DropboxDownload.this,
                        "File save at " + msg.obj.toString(), Toast.LENGTH_LONG)
                        .show();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.dropboxdownload);
        lvDropboxDownloadFilesList = (ListView) findViewById(R.id.lvDropboxDownloadFilesList);
        ActionBar actionBar = getSupportActionBar();
        Drawable d=getResources().getDrawable(R.drawable.back);
        actionBar.setHomeAsUpIndicator(d);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff262626));
        Spannable text = new SpannableString("SELECT IMAGE");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#ecf0f1")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        // btnDropboxDownloadDone = (Button)
        // findViewById(R.id.btnDropboxDownloadDone);
        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);

        checkAppKeySetup();
        // setLoggedIn(false);
        if (!Constants.mLoggedIn)
            mApi.getSession().startAuthentication(DropboxDownload.this);
        // else
        // setLoggedIn(mApi.getSession().isLinked());
        // click events
        // btnDropboxDownloadDone.setOnClickListener(this);
        lvDropboxDownloadFilesList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        fileSelected = files.get(arg2);

        if (fileSelected.isDir) {
            isItemClicked = true;
            DIR = dir.get(arg2);
            setLoggedIn(true);
        } else {

            new Sharelinker().execute();

        }
    }
    //sharelinker is an async task that gets true url of an image then passes it to an intent and opens the image casting activity
    private class Sharelinker extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(DropboxDownload.this, "Retrieving Dropbox URL", "Please Wait ...");
        }
        @Override
        protected void onProgressUpdate(String...values){
            super.onProgressUpdate(values);

        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            progressDialog.dismiss();
        }
        /** Doing the parsing of xml data in a non-ui thread */
        @Override
        protected Void doInBackground(String... arg0){
            getshareurl();
            return null;
        }
    }
    private void getshareurl() {
        try {
            DropboxAPI.DropboxLink shareLink = mApi.share(fileSelected.path);
            shareurl = getShareURL(shareLink.url).replaceFirst("https://www.dropbox", "https://dl.dropboxusercontent");
            //shareurl = getShareURL(shareLink.url);
            shareurl = shareurl.substring(0, shareurl.length() - 5);
            goback();
        } catch (DropboxException e) {
            e.printStackTrace();
        }
    }

    String getShareURL(String strURL) {
        URLConnection conn = null;
        String redirectedUrl = null;
        try {
            URL inputURL = new URL(strURL);
            conn = inputURL.openConnection();
            conn.connect();

            InputStream is = conn.getInputStream();
            System.out.println("Redirected URL: " + conn.getURL());
            redirectedUrl = conn.getURL().toString();
            //redirectedUrl =redirectedUrl.substring(0,  redirectedUrl.length() - 5);
            is.close();

        } catch (MalformedURLException e) {
            Log.d(TAG, "Please input a valid URL");
        } catch (IOException ioe) {
            Log.d(TAG, "Can not connect to the URL");
        }

        return redirectedUrl;
    }

    public void goback() {
        Intent i = new Intent(this,ImageSender.class);
        i.putExtra("imgurl",shareurl);
        startActivity(i);
    }

    private void checkAppKeySetup() {
        if (Constants.DROPBOX_APP_KEY.startsWith("CHANGE")
                || Constants.DROPBOX_APP_SECRET.startsWith("CHANGE")) {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            finish();
            return;
        }
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + Constants.DROPBOX_APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            showToast("URL scheme in your app's "
                    + "manifest is not set up correctly. You should have a "
                    + "com.dropbox.client2.android.AuthActivity with the "
                    + "scheme: " + scheme);
            finish();
        }
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        error.show();
    }

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

    public void setLoggedIn(final boolean loggedIn){
        pd = ProgressDialog.show(DropboxDownload.this, null,
                "Retrieving data...");
        new Thread(new Runnable() {

            @Override
            public void run() {
                Constants.mLoggedIn = loggedIn;
                if (loggedIn) {
                    int i = 0;
                    com.dropbox.client2.DropboxAPI.Entry dirent;
                    try {
                        dirent = mApi.metadata(DIR, 1000, null, true, null);
                        files = new ArrayList<com.dropbox.client2.DropboxAPI.Entry>();
                        dir = new ArrayList<String>();
                        for (com.dropbox.client2.DropboxAPI.Entry ent : dirent.contents) {
                            files.add(ent);
                            dir.add(new String(files.get(i++).path));
                        }
                        i = 0;
                        mHandler.sendEmptyMessage(0);
                    } catch (DropboxException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }

    @Override
    protected void onResume() {

        super.onResume();
        AndroidAuthSession session = mApi.getSession();

        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();

                TokenPair tokens = session.getAccessTokenPair();
                storeKeys(tokens.key, tokens.secret);
                setLoggedIn(true);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:"
                        + e.getLocalizedMessage());
            }
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
    /*
        private boolean downloadDropboxFile(DropboxAPI.Entry fileSelected) {// , String
            // localFilePath)
            // {
            File dir = new File(Utils.getPath());
            if (!dir.exists())
                dir.mkdirs();
            try {
                File localFile = new File(dir + "/" + fileSelected.fileName());
                if (!localFile.exists()) {
                    localFile.createNewFile();
                    copy(fileSelected, localFile);
                } else {
                    showFileExitsDialog(fileSelected, localFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        private void copy(final DropboxAPI.Entry fileSelected, final File localFile) {
            final ProgressDialog pd = ProgressDialog.show(DropboxDownload.this,
                    "Downloading...", "Please wait...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedInputStream br = null;
                    BufferedOutputStream bw = null;
                    DropboxAPI.DropboxInputStream fd;
                    try {
                        fd = mApi.getFileStream(fileSelected.path,
                                localFile.getPath());
                        br = new BufferedInputStream(fd);
                        bw = new BufferedOutputStream(new FileOutputStream(
                                localFile));
                        byte[] buffer = new byte[4096];
                        int read;
                        while (true) {
                            read = br.read(buffer);
                            if (read <= 0) {
                                break;
                            }
                            bw.write(buffer, 0, read);
                        }
                        pd.dismiss();
                        Message message = new Message();
                        message.obj = localFile.getAbsolutePath();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    } catch (DropboxException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (bw != null) {
                            try {
                                bw.close();
                                if (br != null) {
                                    br.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
        private void showFileExitsDialog(final DropboxAPI.Entry fileSelected,
                                         final File localFile) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DropboxDownload.this);
            alertBuilder.setMessage(Constants.OVERRIDEMSG);
            alertBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            copy(fileSelected, localFile);
                        }
                    });
            alertBuilder.setNegativeButton("Cancel", null);
            alertBuilder.create().show();
        }
    */
    @Override
    public void onBackPressed() {
        if (isItemClicked) {
            if (DIR.length() == 0) {
                // logOut();
                setResult(RESULT_OK);
                super.onBackPressed();
            } else {
                DIR = DIR.substring(0, DIR.lastIndexOf('/'));
                setLoggedIn(true);

            }
        } else {
            setResult(RESULT_OK);
            super.onBackPressed();
        }

    }

}