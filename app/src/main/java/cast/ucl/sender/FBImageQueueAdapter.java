package cast.ucl.sender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Caladbolg on 06/10/2015.
 */
public class FBImageQueueAdapter extends BaseAdapter implements View.OnClickListener {
    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    FBImageListModel img_list_item=null;
    int i=0;
    private Context context;

    /*************  CustomAdapter Constructor *****************/
    public FBImageQueueAdapter(Activity a, ArrayList d,Resources resLocal) {
        this.context = context;
        /********** Take passed val0ues **********/
        activity = a;
        data=d;
        res = resLocal;
        context = activity.getApplicationContext();
        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder to contain inflated xml file elements ***********/
    public static class ViewHolder{

        public TextView url;
        public TextView id;
        public ImageView thumbnail;

    }

    /*********** Depends upon data size called for each row , Create each ListView row ***********/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;

        if(vi==null){

            /********** Inflate tabitem.xml file for each row ( Defined below ) ************/
            vi = inflater.inflate(R.layout.activity_album_item, null);

            /******** View Holder Object to contain tabitem.xml file elements ************/
            holder=new ViewHolder();
            holder.thumbnail = (ImageView)vi.findViewById(R.id.fbphoto);
            holder.id = (TextView)vi.findViewById(R.id.photo_id);
            holder.url = (TextView)vi.findViewById(R.id.photo_url);

        }
        else{  holder=(ViewHolder)vi.getTag();}


        /************  Set holder with LayoutInflater ************/
        vi.setTag(holder);
        if(data.size()<=0)
        {
            holder.id.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            img_list_item=null;
            img_list_item = (FBImageListModel) data.get(position);

            /************  Set Model values in Holder elements ***********/
            String title = "";

            String img = img_list_item.getUrl();
            //img = "http://img.youtube.com/vi/"+ img+ "/0.jpg";
            //Log.v("url",img);
            Picasso.with(context).load(img).resize(300, 300).into(holder.thumbnail);

            holder.url.setText(img_list_item.getUrl());
            holder.id.setText(img_list_item.getId());
           // Log.v("url", img_list_item.getUrl());
            //Log.v("id", img_list_item.getId());
            /******** Set Item Click Listner for LayoutInflater for each row ***********/
            vi.setOnClickListener(new OnItemClickListener(position));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            AlbumViewer sct = (AlbumViewer)activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }
}




