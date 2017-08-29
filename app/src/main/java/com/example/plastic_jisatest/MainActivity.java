package com.example.plastic_jisatest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ClipData;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    DrawRect drawView;
    LinearLayout linearLayout,droplayout;
    float startDegress =-90;
    float endDegress = 0;
    TextView time ;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        linearLayout = (LinearLayout)findViewById(R.id.parentView);
        droplayout = (LinearLayout)findViewById(R.id.drop);
      button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Code here executes on main thread after user presses button
//                new  Webservice().execute();
//            }
//        });

        time = (TextView)findViewById(R.id.time);
        linearLayout.setRotation(90);
        final Handler ha=new Handler();

        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar c = Calendar.getInstance();
                time.setText(c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND));

            }
            public void onFinish() {

            }
        };
        newtimer.start();

        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                startDegress+=90;

                endDegress+=90;
                RotateAnimation anim = new RotateAnimation(startDegress, endDegress, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                anim.setInterpolator(new LinearInterpolator());
                anim.setRepeatCount(0);
                anim.setFillAfter(true);
                anim.setDuration(200);
                linearLayout.startAnimation(anim);

                ha.postDelayed(this, 1000);
            }
        }, 1000);


        linearLayout.setOnTouchListener(new MyTouchListener());
        droplayout.setOnDragListener(new MyDragListener());



    }
    class MyDragListener implements View.OnDragListener {
//        Drawable enterShape = getResources().getDrawable(
//                R.drawable.shape_droptarget);
//        Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    ViewGroup.LayoutParams params = droplayout.getLayoutParams();
// Changes the height and width to the specified *pixels*
                    params.height = 500;
                    droplayout.setLayoutParams(params);
                    View view1 = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view1.getParent();
                    owner.removeView(view1);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.e("stpes","1");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.e("stpes","2");
                    break;
                case DragEvent.ACTION_DROP:
                    Log.e("stpes","3");

                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner1 = (ViewGroup) view.getParent();
                    //owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.e("stpes","4");

                    // v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }

    }

    final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                //  view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }
    public  class Webservice extends AsyncTask<Void,Integer, String> {


        String a = "jisa";
        @Override
        protected String doInBackground(Void... params) {
            try{
                //Make the Http connection so we can retrieve the time
                HttpClient httpclient = new DefaultHttpClient();
                // I am using yahoos api to get the time
                HttpResponse response = httpclient.execute(new
                        HttpGet("http://developer.yahooapis.com/TimeService/V1/getTime?appid=YahooDemo"));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    // The response is an xml file and i have stored it in a string
                    String responseString = out.toString();
                    Log.d("Response", responseString);
                    //We have to parse the xml file using any parser, but since i have to
                    //take just one value i have deviced a shortcut to retrieve it
                    int x = responseString.indexOf("<Timestamp>");
                    int y = responseString.indexOf("</Timestamp>");
                    //I am using the x + "<Timestamp>" because x alone gives only the start value
                    Log.d("Response", responseString.substring(x + "<Timestamp>".length(),y) );
                    String timestamp =  responseString.substring(x + "<Timestamp>".length(),y);
                    // The time returned is in UNIX format so i need to multiply it by 1000 to use it
                    Date d = new Date(Long.parseLong(timestamp) * 1000);

//                time.setText(d.toString());
                    Log.d("Response", d.toString() );
                    return d.toString() ;
                } else{
                    //Closes the connection.d
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            }catch (ClientProtocolException e) {
                Log.d("Response", e.getMessage());
            }catch (IOException e) {
                Log.d("Response", e.getMessage());

            }
            Log.d("bj", a);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}
