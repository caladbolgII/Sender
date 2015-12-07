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
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
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
        VideoView right_pane_top2 = new  VideoView(this);
        TextView left_pane_bottom = new TextView(this);
         ChatView right_pane = new ChatView(this);
        ImageView right_pane_top = new ImageView(this);
        ImageView left_pane_top2 = new ImageView(this);
        ImageView left_pane2 = new ImageView(this);
        TwitterView right_pane_bottom = new TwitterView(this);
        VideoView whole_pane = new VideoView(this);
        ImageView whole_pane2 = new ImageView(this);


        layout_selected = globalVariable.getlayout();
        setContentView(R.layout.activity_blank);

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
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                        open_video(view);
                    }
                });
                right_pane_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                        open_image(view);
                    }
                });
                left_pane_bottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                        open_sender(view);
                    }
                });
                right_pane_bottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
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
                left_pane_top2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                        open_image(view);
                    }
                });
                right_pane_top2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                        open_video(view);
                    }
                });
                left_pane_bottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                        open_sender(view);
                    }
                });
                right_pane_bottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                        open_twitter(view);
                    }
                });
                left_pane_top2.setLayoutParams(layoutParams);
                left_pane_bottom.setLayoutParams(layoutParams);
                right_pane_top2.setLayoutParams(layoutParams);
                right_pane_bottom.setLayoutParams(layoutParams);

                right_top.addView(right_pane_top2);
                right_bottom.addView(right_pane_bottom);
                left_top.addView(left_pane_top2);
                left_bottom.addView(left_pane_bottom);
                break;
            case"layout3":
                whole_pane.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                        open_video(view);
                    }
                });
                whole.addView(whole_pane);
                break;
            case"layout4":
                whole_pane2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                        open_image(view);
                    }
                });
                whole.addView(whole_pane2);
                break;
        }

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff2196f3));
        Spannable text = new SpannableString("Tile Select");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#e9e9e9")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        actionBar.setDisplayShowTitleEnabled(true);



    }

    public View getActionBarView() {
        Window window = getWindow();
        View v = window.getDecorView();
        int resId = getResources().getIdentifier("action_bar_container", "id", "android");
        return v.findViewById(resId);
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
           // paint.setColor(Color.parseColor("#212121"));//bdc3c7
            paint.setColor(Color.parseColor("#303030"));
            int x = getWidth();
            int y = getHeight();
            paint.setStrokeWidth(3);
            canvas.drawRect(0, 0, x, y, paint);
            paint.setStrokeWidth(0);
            paint.setColor(Color.parseColor("#803533"));//556773
            if(globalVariable.getlayout()=="layout3")canvas.drawRect(15, 15, x - 15, y - 15, paint);
            else if(globalVariable.getlayout()=="layout2")canvas.drawRect(15, 15, x - 15, y - 15, paint);
            else  canvas.drawRect(15, 15, x, y - 15, paint);
            p=new Paint();
            Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.hq2);
            Bitmap c= BitmapFactory.decodeResource(getResources(), R.drawable.hq3);
            Bitmap e= BitmapFactory.decodeResource(getResources(), R.drawable.video_file1);
            Bitmap n = b.createScaledBitmap(b, x, 82 * y / 100, true);
            Bitmap o = b.createScaledBitmap(b,x-25,82*y/100,true);

            Bitmap r = c.createScaledBitmap(c,c.getWidth()+5,80*y/100,true);
            p.setColor(Color.RED);
            int cx = (mWidth - b.getWidth()) >> 1; // same as (...) / 2
            int cy = (mHeight - b.getHeight()) >> 1;
            int cxc = (mWidth - c.getWidth()) >> 1; // same as (...) / 2
            int cyc = (mHeight - c.getHeight()) >> 1;

            int cxe = (mWidth - e.getWidth()) >> 1; // same as (...) / 2
            int cye = (mHeight - e.getHeight()) >> 1;
            if (mAngle > 0) {
                canvas.rotate(mAngle, mWidth >> 1, mHeight >> 1);
            }
            if(globalVariable.getlayout()=="layout1") canvas.drawBitmap(n,cx, cy, p);
            //canvas.drawBitmap(r, cx-2, cy-42, p);
            else if(globalVariable.getlayout()=="layout2")canvas.drawBitmap(r, cxc-2, cyc-42, p);
            else if(globalVariable.getlayout()=="layout3")canvas.drawBitmap(o,cx/2-82, cy-62, p);
            else if(globalVariable.getlayout()=="layout4")canvas.drawBitmap(o,cx/2-82, cy-62, p);

            paint.setColor(Color.parseColor("#e9e9e9"));
            paint.setTextSize(37);
            canvas.drawText("Videos", 25, y-30, paint);
            //canvas.drawText("Videos", e.getWidth()+25, y-30, paint);
            canvas.drawBitmap(e,x-65, y-e.getHeight()-24, p);

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
            // paint.setColor(Color.parseColor("#212121"));//bdc3c7
            paint.setColor(Color.parseColor("#303030"));
            int x = getWidth();
            int y = getHeight();
            paint.setStrokeWidth(3);
            canvas.drawRect(0, 0, x, y, paint);
            paint.setStrokeWidth(0);
            paint.setColor(Color.parseColor("#33803b"));//556773
            if(globalVariable.getlayout() == "layout2")canvas.drawRect(15, 15, x, y - 15, paint);
            else  canvas.drawRect(15, 15, x-15, y - 15, paint);
            p=new Paint();
            Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.photography1);
            Bitmap c= BitmapFactory.decodeResource(getResources(), R.drawable.photography2);
            Bitmap n = c.createScaledBitmap(b, x, 82 * y / 100, true);
            Bitmap e= BitmapFactory.decodeResource(getResources(), R.drawable.pic1);
            Bitmap r = b.createScaledBitmap(b,b.getWidth()+5,80*y/100,true);
            p.setColor(Color.RED);
            int cx = (mWidth - b.getWidth()) >> 1; // same as (...) / 2
            int cy = (mHeight - b.getHeight()) >> 1;
            int cxc = (mWidth - c.getWidth()) >> 1; // same as (...) / 2
            int cyc = (mHeight - c.getHeight()) >> 1;
            if (mAngle > 0) {
                canvas.rotate(mAngle, mWidth >> 1, mHeight >> 1);
            }
            //if(globalVariable.getlayout()=="layout1")
            //canvas.drawBitmap(r, cx-2, cy-42, p);canvas.drawBitmap(n,cx, cy, p)
            if(globalVariable.getlayout()=="layout1") canvas.drawBitmap(r, cx-2, cy-42, p);
            else if(globalVariable.getlayout()=="layout2")canvas.drawBitmap(n,cxc, cyc, p);
            else if(globalVariable.getlayout()=="layout3")canvas.drawBitmap(r, cx-2, cy-42, p);
            else if(globalVariable.getlayout()=="layout4")canvas.drawBitmap(r, cx-2, cy-42, p);



            paint.setColor(Color.parseColor("#e9e9e9"));
            paint.setTextSize(37);
            canvas.drawText("Photos", 25, y - 30, paint);
            //canvas.drawText("Edit Image Queue", e.getWidth()+25, y-30, paint);
            canvas.drawBitmap(e,x-65, y-e.getHeight()-24, p);
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
            // paint.setColor(Color.parseColor("#212121"));//bdc3c7
            paint.setColor(Color.parseColor("#303030"));
            int x = getWidth();
            int y = getHeight();
            paint.setStrokeWidth(3);
            canvas.drawRect(0, 0, x, y, paint);
            paint.setStrokeWidth(0);
            paint.setColor(Color.parseColor("#805c33"));//556773
            canvas.drawRect(15, 0, x, y-15, paint);
            p=new Paint();
            Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.text1);
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
            //if(globalVariable.getlayout()=="layout1")
           canvas.drawBitmap(b,x-65, y-e.getHeight()-26,p);
           // else canvas.drawBitmap(n,cxn, cyn, p);


            paint.setColor(Color.parseColor("#e9e9e9"));
            paint.setTextSize(37);
            canvas.drawText("Text", 25, y - 30, paint);
            //canvas.drawBitmap(e,25, y-e.getHeight()-25, p);
            paint.setTextSize(40);
            int xPos = (canvas.getWidth() / 2);
            int yPos = (canvas.getHeight() / 2) ;
            canvas.drawText("Announcements", xPos-120, yPos, paint);
        }
    }
    public class ChatView extends View{
        Paint p;
        public ChatView(Context context) {
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
            // paint.setColor(Color.parseColor("#212121"));//bdc3c7
            paint.setColor(Color.parseColor("#303030"));
            int x = getWidth();
            int y = getHeight();
            paint.setStrokeWidth(3);
            canvas.drawRect(0, 0, x, y, paint);
            paint.setStrokeWidth(0);
            paint.setColor(Color.parseColor("#2b616b"));//556773
            canvas.drawRect(15, 15, x - 15, y - 15, paint);
            p=new Paint();
            Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.response);
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


            paint.setColor(Color.parseColor("#e9e9e9"));
            paint.setTextSize(37);
            canvas.drawText("Chatroom", 25, y - 30, paint);
            //canvas.drawBitmap(e,25, y-e.getHeight()-25, p);
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
            // paint.setColor(Color.parseColor("#212121"));//bdc3c7
            paint.setColor(Color.parseColor("#303030"));
            int x = getWidth();
            int y = getHeight();
            paint.setStrokeWidth(3);
            canvas.drawRect(0, 0, x, y, paint);
            paint.setStrokeWidth(0);
            paint.setColor(Color.parseColor("#373380"));//556773
            canvas.drawRect(15, 0, x - 15, y - 15, paint);
            p=new Paint();
            Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.twitter_icon1);
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
            if(globalVariable.getlayout()=="layout1")  canvas.drawBitmap(b,x-85, y-b.getHeight()-5, p);
            else canvas.drawBitmap(b,mWidth, cy, p);


            paint.setColor(Color.parseColor("#e9e9e9"));
            paint.setTextSize(37);
            canvas.drawText("Twitter", 25, y - 32, paint);
            paint.setTextSize(45);
            int xPos = (canvas.getWidth() / 2);
            int yPos = (canvas.getHeight() / 2) ;
            canvas.drawText("#BBCAST", xPos-105, yPos, paint);
            //canvas.drawBitmap(e,25, y-e.getHeight()-25, p);
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
    public void open_chat(View view) {
        Intent intent = new Intent(this,ChatActivity.class);
        startActivity(intent);
    }

}
