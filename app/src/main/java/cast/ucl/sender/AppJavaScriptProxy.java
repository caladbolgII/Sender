package cast.ucl.sender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

public class AppJavaScriptProxy {

    private Activity activity = null;
    Context mContext;
    public AppJavaScriptProxy(Activity activity, Context context) {
        this.activity = activity;
        mContext = context;
    }

    @JavascriptInterface
    public void videoqueue() {
        Intent intent = new Intent(mContext, QueueEdit.class);
        activity.startActivity(intent);
    }
    @JavascriptInterface
    public void imagequeue() {
        Intent intent = new Intent(mContext, ImageQueueEdit.class);
        activity.startActivity(intent);
    }
    @JavascriptInterface
    public void textqueue() {
        Intent intent = new Intent(mContext, TextQueueEdit.class);
        activity.startActivity(intent);
    }
}
