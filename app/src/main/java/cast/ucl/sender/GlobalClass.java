package cast.ucl.sender;

import android.app.Application;

/**
 * Created by LENOVO on 4/10/2015.
 */
public class GlobalClass extends Application {

    private String IP;
    private String videoresponse;
    private String imageresponse;
    private String textresponse;
    private String layout;

    public String getlayout() {

        return layout;
    }

    public void setlayout(String aName) {

        layout = aName;

    }

    public String getvideoresponse() {

        return videoresponse;
    }

    public void setvideoresponse(String aName) {

      videoresponse = aName;

    }
    public String getimageresponse() {

        return imageresponse;
    }

    public void setimageresponse(String aName) {

        imageresponse = aName;

    }
    public String gettextresponse() {

        return textresponse;
    }

    public void settextresponse(String aName) {

        textresponse = aName;

    }
    public String getName() {

        return IP;
    }

    public void setName(String aName) {

      IP = aName;

    }

}