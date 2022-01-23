package com.example.arnold.itsosgadda.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.handlers.DrawerCloser;
import com.example.arnold.itsosgadda.databinding.AfmLayoutBinding;
import com.example.arnold.itsosgadda.handlers.MapsLoader;
import com.example.arnold.itsosgadda.main.MainActivity;
import com.google.android.material.navigation.NavigationView;


@SuppressWarnings("FieldCanBeLocal")
public class AFMActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "AFMActivity";
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private WebView webViewAFM;
    private AfmLayoutBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = AfmLayoutBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            webViewAFM = findViewById(R.id.webViewAFM);
            webViewAFM.getSettings().setJavaScriptEnabled(true);
            webViewAFM.loadUrl("file:///android_asset/afm_html/afm.html");

            drawerLayout = binding.drawerLayout;
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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            DrawerCloser.closeDrawer(drawerLayout);
        } else if (id == R.id.nav_our_story) {
            startActivity(new Intent(getApplicationContext(), StoryActivity.class));
            DrawerCloser.closeDrawer(drawerLayout);
        } else if (id == R.id.nav_study_addresses) {
            startActivity(new Intent(getApplicationContext(), SpecStorySectionActivity.class));
            DrawerCloser.closeDrawer(drawerLayout);
        } else if (id == R.id.nav_e_registry_link) {
            startActivity(new Intent(getApplicationContext(), WebRegistryActivity.class));
            DrawerCloser.closeDrawer(drawerLayout);
        } else if (id == R.id.nav_feedback_to_staff) {
            startActivity(new Intent(getApplicationContext(), EmailSendingActivity.class));
            DrawerCloser.closeDrawer(drawerLayout);
        } else if (id == R.id.nav_findus) {
            startActivity(new Intent(getApplicationContext(), MapsLoader.class));
            DrawerCloser.closeDrawer(drawerLayout);
        } else if (id == R.id.nav_app_blog) {
            startActivity(new Intent(getApplicationContext(), RSSReaderActivity.class));
            DrawerCloser.closeDrawer(drawerLayout);
        } else if (id == R.id.dev_team) {
            builder = new AlertDialog.Builder(this);
            builder.setIcon(R.mipmap.icon_dev_team)
                    .setTitle(R.string.dev_team)
                    .setView(getLayoutInflater().inflate(R.layout.handler_dev_team, null))
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show().setCanceledOnTouchOutside(true);
            DrawerCloser.closeDrawer(drawerLayout);
        } else if (id == R.id.subscribe) {
            startActivity(new Intent(getApplicationContext(), SendBugCrashReport.class));
            DrawerCloser.closeDrawer(drawerLayout);
        }
        return true;
    }
}

