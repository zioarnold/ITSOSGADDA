package com.example.arnold.itsosgadda.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.arnold.itsosgadda.utilities.Log4jHelper;

import org.apache.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.Window.FEATURE_NO_TITLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static com.example.arnold.itsosgadda.R.layout.activity_main_screen;

public class MainScreenActivity extends Activity {
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(FEATURE_NO_TITLE);
            getWindow().setFlags(FLAG_FULLSCREEN,
                    FLAG_FULLSCREEN);
            setContentView(activity_main_screen);

            timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }, 3000);
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("MainScreenActivity");
            log.error("Error", ex);
        }
    }
}
