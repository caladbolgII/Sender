package cast.ucl.sender;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by LENOVO on 3/4/2015.
 */
public class Selection extends ActionBarActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        ActionBar actionBar = getSupportActionBar();
     //   actionBar.setDisplayShowTitleEnabled(true);
   //     actionBar.setDisplayShowCustomEnabled(true);

        Drawable d=getResources().getDrawable(R.drawable.icon);
        actionBar.setHomeAsUpIndicator(d);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff0047ab));
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
}
