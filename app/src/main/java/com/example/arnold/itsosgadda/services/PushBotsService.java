package com.example.arnold.itsosgadda.services;

import android.app.Application;

import com.pushbots.push.Pushbots;

public class PushBotsService extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Pushbots Library
        new Pushbots.Builder(this)
                .setFcmAppId("1:49102344225:android:d82c4f5e4b15006ceb5360")
                .setLogLevel(Pushbots.LOG_LEVEL.DEBUG)
                .setWebApiKey("AIzaSyA_BRjzHCDXggFpnVOBZotzBeUKVgmzghY")
                .setPushbotsAppId("61e3111729cfc82ec46f3ab4")
                .setProjectId("iissgadda-45d77")
                .setSenderId("49102344225")
                .build();
    }
}
