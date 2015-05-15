package cast.ucl.sender;

/**
 * Created by LENOVO on 5/13/2015.
 */
public class ImageListModel {
    private String Image = "";
    private String Url = "";
    private String Id = "";
    private String Timeout = "";
    private String Title = "";
    private String Type = "";

    /**
     * ******** Set Methods *****************
     */


    public void setImage(String Image) {
        this.Image = Image;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public void setTimeout(String Timeout) {
        this.Timeout = Timeout;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setType(String Type) {
        this.Type =Type;
    }

    /**
     * ******** Get Methods ***************
     */


    public String getImage() {
        return this.Image;
    }

    public String getUrl() {
        return this.Url;
    }

    public String getId() {
        return this.Id;
    }

    public String getTimeout() {
        return this.Timeout;
    }

    public String getTitle() {
        return this.Title;
    }

    public String getType() {
        return this.Type;
    }
}