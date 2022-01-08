package com.example.arnold.itsosgadda.activities;


import static android.webkit.WebSettings.ZoomDensity.FAR;

import static com.example.arnold.itsosgadda.R.id.nav_home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.databinding.ERegistryLayoutBinding;
import com.example.arnold.itsosgadda.handlers.MapsLoader;
import com.example.arnold.itsosgadda.main.MainActivity;
import com.google.android.material.navigation.NavigationView;

@SuppressWarnings("FieldCanBeLocal")
public class WebRegistryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "WebRegistryActivity";
    private final WebRegistryActivity activity = this;
    private ERegistryLayoutBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private final String url = "https://web.spaggiari.eu/home/app/default/login.php?custcode=prit0007";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getWindow().requestFeature(Window.FEATURE_PROGRESS);

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
                        activity.setTitle(R.string.e_registry);
                }
            });

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    // Handle the error
                    Log.d(TAG, "Some error!");
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            webView.loadUrl(url);
            webView.canGoBack();

            DrawerLayout drawerLayout = binding.drawerLayout;
            NavigationView navigationView = binding.navView;
            navigationView.setNavigationItemSelectedListener(this);
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home,
                    R.id.nav_our_story,
                    R.id.nav_study_addresses,
                    R.id.nav_e_registry_link,
                    R.id.nav_feedback_to_staff,
                    R.id.nav_findus,
                    R.id.nav_app_blog
            ).setOpenableLayout(drawerLayout)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case nav_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.nav_our_story:
                startActivity(new Intent(getApplicationContext(), StoryActivity.class));
                break;
            case R.id.nav_study_addresses:
                startActivity(new Intent(getApplicationContext(), SpecStorySectionActivity.class));
                break;
            case R.id.nav_e_registry_link:
                startActivity(new Intent(getApplicationContext(), WebRegistryActivity.class));
                break;
            case R.id.nav_feedback_to_staff:
                startActivity(new Intent(getApplicationContext(), EmailSendingActivity.class));
                break;
            case R.id.nav_findus:
                startActivity(new Intent(getApplicationContext(), MapsLoader.class));
                break;
            case R.id.nav_app_blog:
                startActivity(new Intent(getApplicationContext(), RSSReaderActivity.class));
                break;
        }
        return false;
    }
}


