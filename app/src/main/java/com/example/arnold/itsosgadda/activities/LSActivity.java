package com.example.arnold.itsosgadda.activities;

import static com.example.arnold.itsosgadda.R.id.about_app;
import static com.example.arnold.itsosgadda.R.id.nav_home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import com.example.arnold.itsosgadda.databinding.LyceumLayoutBinding;
import com.example.arnold.itsosgadda.handlers.MapsLoader;
import com.example.arnold.itsosgadda.main.MainActivity;
import com.google.android.material.navigation.NavigationView;


@SuppressWarnings("FieldCanBeLocal")
public class LSActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "LSActivity";
    private WebView webViewLS;
    private LyceumLayoutBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = LyceumLayoutBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            webViewLS = (WebView) findViewById(R.id.webViewLS);
            webViewLS.getSettings().setJavaScriptEnabled(true);
            webViewLS.loadUrl("file:///android_asset/ls_html/ls.html");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("InflateParams")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        try {
            int id = item.getItemId();
            switch (id) {
                case R.id.dev_team:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.icon_dev_team)
                            .setTitle(R.string.dev_team)
                            .setView(getLayoutInflater().inflate(R.layout.handler_dev_team, null))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show().setCanceledOnTouchOutside(true);
                    break;
                case about_app:
                    builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.icon_about)
                            .setTitle(R.string.created_for)
                            .setView(getLayoutInflater().inflate(R.layout.handler_version_app, null))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show().setCanceledOnTouchOutside(true);
                    AlertDialog dialog = builder.create();
                    dialog.dismiss();
                    break;
                case R.id.subscribe:
                    startActivity(new Intent(getApplicationContext(), SendBugCrashReport.class));
                    break;
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }
}
