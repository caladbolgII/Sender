package cast.ucl.sender;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchActivity extends ActionBarActivity {

    private SearchView searchInput;
    private ListView videosFound;
    SearchActivity search;
    private Handler handler;
    GlobalClass globalVariable;

    private List<VideoItem> searchResults;
    public String yt_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        search = this;
        globalVariable= (GlobalClass) getApplicationContext();
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) getSupportActionBar()
                .getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.actionbar_youtube, null);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP| ActionBar.DISPLAY_SHOW_TITLE);
//        Button layoutedit = ( Button) customActionBarView
//                .findViewById(R.id.layout);
//        layoutedit.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
//                Intent intent = new Intent(view.getContext(),MainActivity.class);
//                startActivity(intent);
//            }
//        });
//        Drawable d=getResources().getDrawable(R.drawable.back);
//        actionBar.setHomeAsUpIndicator(d);
        Spannable text = new SpannableString("Youtube Search");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#e9e9e9")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff2196f3));
//        Spannable text = new SpannableString("Video Search");
//        text.setSpan(new ForegroundColorSpan(Color.parseColor("#f44336")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        actionBar.setTitle(text);
        searchInput = (SearchView) customActionBarView.findViewById(R.id.search_input);
        videosFound = (ListView) findViewById(R.id.videos_found);

        handler = new Handler();

        addClickListener();
        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                searchOnYoutube(searchInput.getQuery().toString());
                View view = search.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        LinearLayout linear =(LinearLayout)customActionBarView.findViewById(R.id.action_search);
        linear.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                int i = globalVariable.getclick();
                i++;
                globalVariable.setclick(i);
            }
        });

    }
    private void addClickListener(){
        videosFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

                yt_id = searchResults.get(pos).getId();
                go_back();
            }

        });
    }

    public void go_back() {
        Intent intent = new Intent(this, VideoSender.class);
        intent.putExtra("video",yt_id);
        startActivity(intent);
    }

    private void searchOnYoutube(final String keywords){
        new Thread(){
            public void run(){
                YoutubeConnector yc = new YoutubeConnector(SearchActivity.this);
                searchResults = yc.search(keywords);
                handler.post(new Runnable(){
                    public void run(){
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    private void updateVideosFound(){
        ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getApplicationContext(), R.layout.video_item, searchResults){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.video_item, parent, false);
                }
                ImageView thumbnail = (ImageView)convertView.findViewById(R.id.video_thumbnail);
                TextView title = (TextView)convertView.findViewById(R.id.video_title);
                TextView description = (TextView)convertView.findViewById(R.id.video_description);

                VideoItem searchResult = searchResults.get(position);

                Picasso.with(getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                title.setText(searchResult.getTitle());
                description.setText(searchResult.getDescription());
                return convertView;
            }
        };

        videosFound.setAdapter(adapter);
    }

  }
