package com.example.arnold.itsosgadda.activities;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.arnold.itsosgadda.R;

import static android.webkit.WebSettings.ZoomDensity.FAR;

public class WebRegistryActivity extends Activity {
    final WebRegistryActivity activity = this;
    final String url = "https://web.spaggiari.eu/home/app/default/login.php?custcode=prit0007";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        //Shows me status bar progress
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffeb3b")));
        setContentView(R.layout.e_registry_layout);
        WebView webView = (WebView) findViewById(R.id.webView_Web_registry);
        //Enables JS
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //noinspection deprecation
        webView.getSettings().setDefaultZoom(FAR);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setTitle(R.string.loading_progress);
                activity.setProgress(progress * 100);

                if (progress == 100)
                    activity.setTitle(R.string.app_name);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Handle the error
                Log.i("WEB_VIEW_SPAGGIARI", "Error code: " + errorCode);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
    }
}


