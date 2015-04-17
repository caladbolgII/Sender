package cast.ucl.sender;

import android.app.Application;

/**
 * Created by LENOVO on 4/10/2015.
 */
public class GlobalClass extends Application {

    private String IP;
    private String response;
    private String layout;

    public String getlayout() {

        return layout;
    }

    public void setlayout(String aName) {

        layout = aName;

    }

    public String getresponse() {

        return response;
    }

    public void setresponse(String aName) {

      response = aName;

    }

    public String getName() {

        return IP;
    }

    public void setName(String aName) {

      IP = aName;

    }

}