package cast.ucl.sender;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;
import java.util.Date;

/**
 * Created by Caladbolg on 28/10/2015.
 */
public class ImageServiceSelector extends AppCompatActivity {

    ImageServiceSelector imss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        ActionBar actionBar = getSupportActionBar();

        setContentView(R.layout.activity_image_service_selector);


        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff2196f3));
        Spannable text = new SpannableString("Photo Services");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#e9e9e9")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

    }

    public void choosedropbox(View v) {
        Intent intent = new Intent(v.getContext(), DropboxDownload.class);
        startActivity(intent);

    }
    public void choosefb(View v) {

        Intent intent = new Intent(v.getContext(), ImageSelectorFB.class);
        startActivity(intent);


    }

}
