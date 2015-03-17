package cast.ucl.sender;

import com.dropbox.client2.session.Session;

/**
 * Created by LENOVO on 3/16/2015.
 */
public class Constants {

    public static final String OVERRIDEMSG = "File name with this name already exists.Do you want to replace this file?";
    final static public String DROPBOX_APP_KEY = "bt00vgtqek79ilm";
    final static public String DROPBOX_APP_SECRET = "2f62h0ktj4evzg6";
    public static boolean mLoggedIn = false;

    final static public Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;

    final static public String ACCOUNT_PREFS_NAME = "prefs";
    final static public String ACCESS_KEY_NAME = "ACCESS_KEY";
    final static public String ACCESS_SECRET_NAME = "ACCESS_SECRET";
}
