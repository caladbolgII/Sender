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
    // RECEIVER API's
    //ADD
    final static public String action_add_video = "addVideo";
    final static public String action_add_image = "addImage";
    final static public String action_add_text = "addMessage";
    final static public String action_cast_video = "castVideo";
    final static public String action_cast_image = "castImage";
    final static public String action_cast_text = "castMessage";
    final static public String action_add_text2 = "test2";
    final static public String action_delete_video = "deleteVideo";
    final static public String action_delete_image = "deleteImage";
    final static public String action_delete_text = "deleteText";
    //CHANGE
    final static public String action_change_video = "changeVideo";
    final static public String action_change_image = "changeImage";
    final static public String action_change_text = "changeText";
    //GET
    final static public String action_get_video = "getVideoQueue";
    final static public String action_get_image = "getImageQueue";
    final static public String action_get_text = "getTextQueue";
    final static public String action_follow_twitter = "twitterFollow";
    //IP address and port
    //ADD
    final static public String SERVER = "http://192.168.1.4:8080";

    final static public String SERVER_ADDR_DELETE = SERVER+"/deleteMedia";
    final static public String SERVER_ADDR_ADD = SERVER+"/addMedia";
    final static public String SERVER_ADDR_CAST = SERVER+ "/castMedia";
    final static public String SERVER_ADDR_LAYOUT = SERVER+"/setLayout";
    final static public String SERVER_GET_VIDEO = SERVER+"/getVideos";

}
