package cast.ucl.sender;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by LENOVO on 3/19/2015.
 */
public class ImageQueueAdapter extends BaseAdapter implements View.OnClickListener {
/*********** Declare Used Variables *********/
private Activity activity;
private ArrayList data;
private static LayoutInflater inflater=null;
public Resources res;
        ImageListModel img_list_item=null;
        int i=0;
private Context context;

/*************  CustomAdapter Constructor *****************/
public ImageQueueAdapter(Activity a, ArrayList d,Resources resLocal) {
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

        public TextView title;
        public TextView url;
        public TextView deadline;
        public TextView id;
        public ImageView thumbnail;
        public TextView type;

}

        /*********** Depends upon data size called for each row , Create each ListView row ***********/
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View vi=convertView;
                ViewHolder holder;

                if(vi==null){

                        /********** Inflate tabitem.xml file for each row ( Defined below ) ************/
                        vi = inflater.inflate(R.layout.activity_image_item, null);

                        /******** View Holder Object to contain tabitem.xml file elements ************/
                        holder=new ViewHolder();
                        holder.thumbnail = (ImageView)vi.findViewById(R.id.img_thumb);
                        holder.title = (TextView)vi.findViewById(R.id.img_title);
                        holder.url = (TextView)vi.findViewById(R.id.img_url);
                        holder.deadline = (TextView)vi.findViewById(R.id.item_timeout);
                        holder.id = (TextView)vi.findViewById(R.id.item_id);
                        holder.type = (TextView)vi.findViewById(R.id.img_class);
                }
                else{  holder=(ViewHolder)vi.getTag();}


                /************  Set holder with LayoutInflater ************/
                vi.setTag(holder);
                if(data.size()<=0)
                {
                        holder.title.setText("No Data");

                }
                else
                {
                        /***** Get each Model object from Arraylist ********/
                        img_list_item=null;
                        img_list_item = (ImageListModel) data.get(position);

                        /************  Set Model values in Holder elements ***********/
                        String title = "";

                        String img = img_list_item.getUrl();
                        //img = "http://img.youtube.com/vi/"+ img+ "/0.jpg";
                        Picasso.with(context).load(img).resize(115, 115).into(holder.thumbnail);

                        holder.title.setText(img_list_item.getTitle());
                        holder.url.setText(img_list_item.getUrl());
                        holder.deadline.setText(img_list_item.getTimeout());
                        holder.id.setText(img_list_item.getId());
                        holder.type.setText(img_list_item.getType());
                        /******** Set Item Click Listner for LayoutInflater for each row ***********/
                        vi.setOnClickListener(new OnItemClickListener(position));
                }
                return vi;
        }

        @Override
        public void onClick(View v) {
                Log.v("CustomAdapter", "=====Row button clicked");
        }

/********* Called when Item click in ListView ************/
private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
                mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
                ImageQueueEdit sct = (ImageQueueEdit)activity;
                sct.onItemClick(mPosition);

        }
}



}

