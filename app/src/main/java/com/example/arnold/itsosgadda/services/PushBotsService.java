package com.example.arnold.itsosgadda.services;

import android.app.Application;

import com.example.arnold.itsosgadda.R;
import com.pushbots.push.Pushbots;

public class PushBotsService extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Pushbots Library
        new Pushbots.Builder(this)
                .setFcmAppId(getResources().getString(R.string.FcmAppId))
                .setLogLevel(Pushbots.LOG_LEVEL.DEBUG)
                .setWebApiKey(getResources().getString(R.string.WebApiKey))
                .setPushbotsAppId(getResources().getString(R.string.PushbotsAppId))
                .setProjectId(getResources().getString(R.string.ProjectId))
                .setSenderId(getResources().getString(R.string.SenderId))
                .build();
    }
}
