package cast.ucl.sender;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Locale;


public class text extends ActionBarActivity {
    public EditText myTextField;
    public DatePicker textdeadline;
    public EditText TitleField;
    public Button myButton;
    public String deaddate;
    String responseStr;
    public String command;
    public TimePicker deadtime;
    public Spinner spinner;
    String timeString="";
    String yy,mm,dd,hh,mi,MM;
    public String type = "";
    GlobalClass globalVariable;
    public TextView exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_text);
        myTextField = (EditText) findViewById(R.id.text_message);
        TitleField = (EditText)findViewById(R.id.cast_text_title);
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) getSupportActionBar()
                .getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.actionbar_sender, null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME);
        exp = (TextView) findViewById(R.id.expiry_text);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xff2196f3));
        Spannable text = new SpannableString("Message Sender");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#2196f3")), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        mm = "";
        mi = "";
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        TextView title = (TextView)customActionBarView.findViewById(R.id.action_title);
        title.setText(text);

        Button send = (Button) customActionBarView
                .findViewById(R.id.next);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.button_click));
                if(mm.length() <1 || mi.length()<1){

                    CharSequence text = "Please set date and time";
                    int duration = Toast.LENGTH_SHORT;
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    attempt_add_text(view);
                }


            }
        });



        globalVariable= (GlobalClass) getApplicationContext();
        spinner = (Spinner) findViewById(R.id.txtspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classification_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        spinner.setSelection(4);

        LinearLayout linear1 = (LinearLayout)findViewById(R.id.linear);
        linear1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                int i = globalVariable.getclick();
                i++;
                globalVariable.setclick(i);
            }
        });
    }

    public void setdate(View view){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            // the callback received when the user "sets" the Date in the DatePickerDialog
            @Override
            public void onDateSet(DatePicker view, int yearSelected, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minutes = c.get(Calendar.MINUTE);

                if (hour == 0) {
                    timeString =  "AM";
                } else if (hour < 12) {
                    timeString =  "AM";
                } else if (hour == 12) {
                    timeString = "PM";
                } else {
                    timeString = "PM";
                }

                c.get(Calendar.AM_PM);
                MM = c.getDisplayName(c.MONTH, Calendar.LONG, Locale.US);
                mm = String.format("%02d", monthOfYear+1);
                dd = String.format("%02d", dayOfMonth);
                yy = Integer.toString(yearSelected);
                final TimePickerDialog timePickerDialog = new TimePickerDialog(text.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                        hh = Integer.toString(hours);
                        mi = Integer.toString(minutes);
                        Toast.makeText(getApplicationContext(), "Time selected is:  "+hh+":"+mi, Toast.LENGTH_SHORT).show();
                        exp.setText("  " + MM + " " + dd+ ","+ " " + yy+" "+hh+":"+mi);
                    }

                }, hour, minutes, true);
                timePickerDialog.setTitle("Set Message Expiry Time");
                timePickerDialog.show();

                int monthSelected = monthOfYear;
                int daySelected = dayOfMonth;
                Toast.makeText(getApplicationContext(), "Date selected is:  "+daySelected+"-"+monthSelected+"-"+yearSelected, Toast.LENGTH_SHORT).show();
            }
        },mYear,mMonth,mDay);
        datePickerDialog.setTitle("Set Message Expiry Date");
        datePickerDialog.show();


    }


    public void attempt_add_text(View view){
        deaddate = mm + " "+ dd+ " " + yy+ " "+hh+":"+mi;
        command = Constants.action_add_text;
        if(myTextField.getText().toString().length()==0) {
            Context context = getApplicationContext();
            CharSequence text = "Please type a message";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if(TitleField.getText().toString().length()==0) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter a title";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {
            try {
                new Connection().execute();
            } catch (Exception e) {
                Log.d("JSON Exception1", e.toString());
            }

            globalVariable.settextresponse(responseStr);
            Context context = getApplicationContext();
//            CharSequence text = "Message has been added to Queue";
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
            int click = globalVariable.getclick();
            CharSequence text = "Message has been added to queue," + "There have been: "+click+" Misclicks";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            globalVariable.setclick(0);
            go_back();
        }
    }
    public void go_back() {
        Intent intent = new Intent(this, TextQueueEdit.class);
        startActivity(intent);
    }
    private class Connection extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0){
            connect();
            return null;
        }

    }

    private void connect(){
        try {
            String json = "";
            InputStream inputStream = null;
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("action",Constants.action_add_text);
            jsonObject.accumulate("text",myTextField.getText());
            jsonObject.accumulate("classification",type);
            jsonObject.accumulate("title", TitleField.getText());
            jsonObject.accumulate("deadline", deaddate);
            //DefaultHttpClient httpclient= HttpClientProvider.newInstance("string");
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(Constants.SERVER_ADDR_TEXTS);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpost.setEntity(se);
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httpost);
         // safeClose(httpclient);
            inputStream = response.getEntity().getContent();

            if(inputStream != null)
                responseStr = convertInputStreamToString(inputStream);
            else
                responseStr = "Did not work!";

        } catch (ClientProtocolException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
        catch (JSONException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }
    }
    public static void safeClose(HttpClient client)
    {
        if(client != null && client.getConnectionManager() != null)
        {
            client.getConnectionManager().shutdown();
        }
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


}
