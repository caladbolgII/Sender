package cast.ucl.sender;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by LENOVO on 3/4/2015.
 */
public class Selection extends ActionBarActivity {
    GlobalClass globalVariable;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout left, right, left_top, left_bottom, right_top, right_bottom,whole;
    String layout_selected;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        globalVariable = (GlobalClass) getApplicationContext();
        VideoView left_pane = new VideoView(this);
        VideoView left_pane_top = new  VideoView(this);
        TextView left_pane_bottom = new TextView(this);
        TextView right_pane = new TextView(this);
        ImageView right_pane_top = new ImageView(this);
        TwitterView right_pane_bottom = new TwitterView(this);
        VideoView whole_pane = new VideoView(this);



        layout_selected = globalVariable.getlayout();
        setContentView(R.layout.acitivty_blank);

        whole = (LinearLayout)findViewById(R.id.whole);
        right = (LinearLayout)findViewById(R.id.column_right);
        right_top = (LinearLayout)findViewById(R.id.column_right_top);
        right_bottom = (LinearLayout)findViewById(R.id.column_right_bottom);
        left = (LinearLayout)findViewById(R.id.column_left);
        left_top = (LinearLayout)findViewById(R.id.column_left_top);
        left_bottom = (LinearLayout)findViewById(R.id.column_left_bottom);


        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        switch (layout_selected) {
            case "layout1":
                left_pane_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        open_video(view);
                    }
                });
                right_pane_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        open_image(view);
                    }
                });
                left_pane_bottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        open_sender(view);
                    }
                });
                right_pane_bottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        open_twitter(view);
                    }
                });
               left_pane_top.setLayoutParams(layoutParams);
               left_pane_bottom.setLayoutParams(layoutParams);
               right_pane_top.setLayoutParams(layoutParams);
               right_pane_bottom.setLayoutParams(layoutParams);

                right_top.addView(right_pane_top);
                right_bottom.addView(right_pane_bottom);
                left_top.addView(left_pane_top);
                left_bottom.addView(left_pane_bottom);
                break;
            case "layout2":
                left_pane.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        open_video(view);
                    }
                });
                right_pane.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        open_sender(view);
                    }
                });
                left_pane.setLayoutParams(layoutParams);
                right_pane.setLayoutParams(layoutParams);

                right.addView(right_pane);
                left.addView(left_pane);
                break;
            case"layout3":
                whole_pane.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        open_video(view);
                    }
                });
                whole.addView(whole_pane);
                break;
        }




        //   actionBar.setDisplayShowTitleEnabled(true);
        //     actionBar.setDisplayShowCustomEnabled(true);

        ActionBar actionBar = getSupportActionBar();


        LayoutInflater inflater = (LayoutInflater) getSupportActionBar()
                .getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.actionbar_selection, null);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP |ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Button layoutedit = ( Button) customActionBarView
                .findViewById(R.id.layout);
        layoutedit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
               Intent intent = new Intent(view.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);

        Drawable d = getResources().getDrawable(R.drawable.back);
        actionBar.setHomeAsUpIndicator(d);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff262626));
        Spannable text = new SpannableString("Tile Select");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#ecf0f1")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

    }



    public class VideoView extends View{
        Paint p;
        public VideoView(Context context) {
            super(context);

        }
        private int mWidth;
        private int mHeight;
        private float mAngle;

        @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
            mHeight = View.MeasureSpec.getSize(heightMeasureSpec);

            setMeasuredDimension(mWidth, mHeight);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
           // paint.setColor(Color.parseColor("#000000"));//bdc3c7
            paint.setColor(Color.parseColor("#ecf0f1"));
            int x = getWidth();
            int y = getHeight();
            paint.setStrokeWidth(3);
            canvas.drawRect(0, 0, x, y, paint);
            paint.setStrokeWidth(0);
            paint.setColor(Color.parseColor("#bdc3c7"));//bdc3c7
            canvas.drawRect(3, 3, x-3, y-3, paint );
            p=new Paint();
            Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.video_file);
            Bitmap e= BitmapFactory.decodeResource(getResources(), R.drawable.list_video);
            Bitmap n = b.createScaledBitmap(b,1*y/4,1*y/4,true);
            p.setColor(Color.RED);
            int cx = (mWidth - b.getWidth()) >> 1; // same as (...) / 2
            int cy = (mHeight - b.getHeight()) >> 1;
            int cxn = (mWidth - n.getWidth()) >> 1; // same as (...) / 2
            int cyn = (mHeight - n.getHeight()) >> 1;

            int cxe = (mWidth - e.getWidth()) >> 1; // same as (...) / 2
            int cye = (mHeight - e.getHeight()) >> 1;
            if (mAngle > 0) {
                canvas.rotate(mAngle, mWidth >> 1, mHeight >> 1);
            }
            if(globalVariable.getlayout()=="layout1")
            canvas.drawBitmap(b,cx, cy, p);
            else canvas.drawBitmap(n,cxn, cyn, p);

            paint.setColor(Color.BLACK);
            paint.setTextSize(27);
            canvas.drawText("Edit Video Queue", e.getWidth()+10, y-15, paint);
            canvas.drawBitmap(e,10, y-e.getHeight()-10, p);

        }
    }
    public class ImageView extends View{
        Paint p;
        public ImageView(Context context) {
            super(context);

        }
        private int mWidth;
        private int mHeight;
        private float mAngle;

        @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
            mHeight = View.MeasureSpec.getSize(heightMeasureSpec);

            setMeasuredDimension(mWidth, mHeight);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            // paint.setColor(Color.parseColor("#000000"));//bdc3c7
            paint.setColor(Color.parseColor("#ecf0f1"));
            int x = getWidth();
            int y = getHeight();
            paint.setStrokeWidth(3);
            canvas.drawRect(0, 0, x, y, paint);
            paint.setStrokeWidth(0);
            paint.setColor(Color.parseColor("#bdc3c7"));//bdc3c7
            canvas.drawRect(3, 3, x-3, y-3, paint );
            p=new Paint();
            Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.image);
            Bitmap e= BitmapFactory.decodeResource(getResources(), R.drawable.list_video);
            Bitmap r = b.createScaledBitmap(b,3*y/4,3*y/4,true);
            p.setColor(Color.RED);
            int cx = (mWidth - b.getWidth()) >> 1; // same as (...) / 2
            int cy = (mHeight - b.getHeight()) >> 1;

            if (mAngle > 0) {
                canvas.rotate(mAngle, mWidth >> 1, mHeight >> 1);
            }
            //if(globalVariable.getlayout()=="layout1")
            canvas.drawBitmap(b,cx, cy, p);
          //  else canvas.drawBitmap(r,cx, cy, p);



            paint.setColor(Color.BLACK);
            paint.setTextSize(27);
            canvas.drawText("Edit Image Queue", e.getWidth()+10, y-15, paint);
            canvas.drawBitmap(e,10, y-e.getHeight()-10, p);
        }
    }

    public class TextView extends View{
        Paint p;
        public TextView(Context context) {
            super(context);

        }
        private int mWidth;
        private int mHeight;
        private float mAngle;

        @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
            mHeight = View.MeasureSpec.getSize(heightMeasureSpec);

            setMeasuredDimension(mWidth, mHeight);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            // paint.setColor(Color.parseColor("#000000"));//bdc3c7
            paint.setColor(Color.parseColor("#ecf0f1"));
            int x = getWidth();
            int y = getHeight();
            paint.setStrokeWidth(3);
            canvas.drawRect(0, 0, x, y, paint);
            paint.setStrokeWidth(0);
            paint.setColor(Color.parseColor("#bdc3c7"));//bdc3c7
            canvas.drawRect(3, 3, x-3, y-3, paint );
            p=new Paint();
            Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.text);
            Bitmap e= BitmapFactory.decodeResource(getResources(), R.drawable.list_video);
            Bitmap r = b.createScaledBitmap(b,3*y/4,3*y/4,true);
            Bitmap n = b.createScaledBitmap(b,1*y/4,1*y/4,true);
            p.setColor(Color.RED);
            int cx = (mWidth - b.getWidth()) >> 1; // same as (...) / 2
            int cy = (mHeight - b.getHeight()) >> 1;
            int cxr = (mWidth - r.getWidth()) >> 1; // same as (...) / 2
            int cyr = (mHeight - r.getHeight()) >> 1;

            int cxn = (mWidth - n.getWidth()) >> 1; // same as (...) / 2
            int cyn = (mHeight - n.getHeight()) >> 1;
            if (mAngle > 0) {
                canvas.rotate(mAngle, mWidth >> 1, mHeight >> 1);
            }
            if(globalVariable.getlayout()=="layout1")
           canvas.drawBitmap(b,cx, cy, p);
            else canvas.drawBitmap(n,cxn, cyn, p);


            paint.setColor(Color.BLACK);
            paint.setTextSize(27);
            canvas.drawText("Edit Text Queue", e.getWidth()+10, y-15, paint);
            canvas.drawBitmap(e,10, y-e.getHeight()-10, p);
        }
    }

    public class TwitterView extends View{
        Paint p;
        public TwitterView(Context context) {
            super(context);

        }
        private int mWidth;
        private int mHeight;
        private float mAngle;

        @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
            mHeight = View.MeasureSpec.getSize(heightMeasureSpec);

            setMeasuredDimension(mWidth, mHeight);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            // paint.setColor(Color.parseColor("#000000"));//bdc3c7
            paint.setColor(Color.parseColor("#ecf0f1"));
            int x = getWidth();
            int y = getHeight();
            paint.setStrokeWidth(3);
            canvas.drawRect(0, 0, x, y, paint);
            paint.setStrokeWidth(0);
            paint.setColor(Color.parseColor("#bdc3c7"));//bdc3c7
            canvas.drawRect(3, 3, x-3, y-3, paint );
            p=new Paint();
            Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.twitter_icon);
            Bitmap r = b.createScaledBitmap(b,3*y/4,3*y/4,true);
            Bitmap e= BitmapFactory.decodeResource(getResources(), R.drawable.list_video);
            p.setColor(Color.RED);
            int cx = (mWidth - b.getWidth()) >> 1; // same as (...) / 2
            int cy = (mHeight - b.getHeight()) >> 1;

            int cxr = (mWidth - r.getWidth()) >> 1; // same as (...) / 2
            int cyr = (mHeight - r.getHeight()) >> 1;

            if (mAngle > 0) {
                canvas.rotate(mAngle, mWidth >> 1, mHeight >> 1);
            }
            if(globalVariable.getlayout()=="layout1")  canvas.drawBitmap(r,cxr, cyr, p);
            else canvas.drawBitmap(b,cx, cy, p);


            paint.setColor(Color.BLACK);
            paint.setTextSize(27);
            canvas.drawText("Twitter", e.getWidth()+10, y-15, paint);
            canvas.drawBitmap(e,10, y-e.getHeight()-10, p);
        }
    }

    public void open_twitter(View view){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=BBCAST_SEND"));
            startActivity(intent);

        }catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/#!/BBCAST_SEND")));
        }
    }

    public void open_image(View view) {
        Intent intent = new Intent(this,ImageQueueEdit.class);
        startActivity(intent);
    }
    public void open_sender(View view) {
        Intent intent = new Intent(this,TextQueueEdit.class);
        startActivity(intent);
    }
    public void open_video(View view) {
        Intent intent = new Intent(this,QueueEdit.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
