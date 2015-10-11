package cast.ucl.sender;

/**
 * Created by Caladbolg on 06/10/2015.
 */
public class FBImageListModel {
    private String Id = "";
    private String Url = "";


    /**
     * ******** Set Methods *****************
     */



    public void setUrl(String Url) {
        this.Url = Url;
    }

    public void setId(String Id) {
        this.Id = Id;
    }


    /**
     * ******** Get Methods ***************
     */



    public String getUrl() {
        return this.Url;
    }

    public String getId() {
        return this.Id;
    }


}