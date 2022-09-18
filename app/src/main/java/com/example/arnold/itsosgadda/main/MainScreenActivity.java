package com.example.arnold.itsosgadda.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.arnold.itsosgadda.activities.YouTubeActivity;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.Window.FEATURE_NO_TITLE;
import static com.example.arnold.itsosgadda.R.layout.activity_main_screen;

@SuppressWarnings("FieldCanBeLocal")
public class MainScreenActivity extends Activity {
    private static final String TAG = "MainScreenActivity";
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(FEATURE_NO_TITLE);
            setContentView(activity_main_screen);

            timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    startActivity(new Intent(getApplicationContext(), YouTubeActivity.class));
                }
            }, 2000);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}