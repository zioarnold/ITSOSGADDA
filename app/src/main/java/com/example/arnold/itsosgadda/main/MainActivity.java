package com.example.arnold.itsosgadda.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.activities.EmailSendingActivity;
import com.example.arnold.itsosgadda.activities.RSSReaderActivity;
import com.example.arnold.itsosgadda.activities.SendBugCrashReport;
import com.example.arnold.itsosgadda.activities.SpecStorySectionActivity;
import com.example.arnold.itsosgadda.activities.StoryActivity;
import com.example.arnold.itsosgadda.activities.WebRegistryActivity;
import com.example.arnold.itsosgadda.handlers.DrawerCloser;
import com.example.arnold.itsosgadda.databinding.ActivityMainBinding;
import com.example.arnold.itsosgadda.handlers.MapsLoader;
import com.google.android.material.navigation.NavigationView;
import com.pushbots.push.Pushbots;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private Button storyButtonMainBody,
            specSectButton,
            webRegistryButton,
            feedBackButton,
            findUsButton,
            communicationButton,
            rssFeedReader;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Intent intent;
    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
//            ActionBar actionBar = getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.setDisplayHomeAsUpEnabled(true);
//                actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
//            }
            Intent intent = getIntent();
            Pushbots.setEmail(intent.getStringExtra("USER_EMAIL"));
            Pushbots.setName(intent.getStringExtra("USER_NAME"));

            storyButtonMainBody = findViewById(R.id.storyButton);
            storyButtonMainBody.setOnClickListener(this);

            specSectButton = findViewById(R.id.specSectionButtonId);
            specSectButton.setOnClickListener(this);

            webRegistryButton = findViewById(R.id.e_registryId);
            webRegistryButton.setOnClickListener(this);

            feedBackButton = findViewById(R.id.feedback);
            feedBackButton.setOnClickListener(this);

            findUsButton = findViewById(R.id.findus);
            findUsButton.setOnClickListener(this);

            communicationButton = findViewById(R.id.button_show_comunications);
            communicationButton.setOnClickListener(this);

            rssFeedReader = findViewById(R.id.app_blog);
            rssFeedReader.setOnClickListener(this);

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

    private void storyButtonClicked() {
        startActivity(new Intent(getApplicationContext(), StoryActivity.class));
    }

    private void specSectButtonClicked() {
        startActivity(new Intent(getApplicationContext(), SpecStorySectionActivity.class));
    }

    private void webRegistryButtonClicked() {
        intent = getPackageManager().getLaunchIntentForPackage("eu.spaggiari.classeviva");
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "eu.spaggiari.classeviva"));
        }
        startActivity(intent);
    }

    private void feedbackMailToButtonClicked() {
        startActivity(new Intent(getApplicationContext(), EmailSendingActivity.class));
    }

    private void communicationButtonClicked() {
//        startActivity(new Intent(getApplicationContext(), ComWebActivity.class));

    }

    private void rssNewsButtonClicked() {
        startActivity(new Intent(getApplicationContext(), RSSReaderActivity.class));
    }

    @Override
    public void onClick(View v) {
        try {
            int id = v.getId();
            if (id == R.id.storyButton) {
                storyButtonClicked();
            } else if (id == R.id.specSectionButtonId) {
                specSectButtonClicked();
            } else if (id == R.id.e_registryId) {
                webRegistryButtonClicked();
            } else if (id == R.id.feedback) {
                feedbackMailToButtonClicked();
            } else if (id == R.id.button_show_comunications) {
                builder = new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher)
                        .setTitle(R.string.created_for)
                        .setMessage(R.string.feature_under_work)
                        .show().setCanceledOnTouchOutside(true);
                dialog = builder.create();
                dialog.dismiss();
//                    communicationButtonClicked();
            } else if (id == R.id.app_blog) {
                rssNewsButtonClicked();
            } else if (id == R.id.findus) {
                builder = new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher)
                        .setTitle(R.string.created_for)
                        .setMessage(R.string.reaching_from_fornovo_FS)
                        .setPositiveButton(R.string.ok, (dialog, which) -> startActivity(new Intent(getApplicationContext(),
                                MapsLoader.class)))
                        .show().setCanceledOnTouchOutside(true);
                dialog = builder.create();
                dialog.dismiss();
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (toggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        if (item.getItemId() == android.R.id.home) {
//            drawerLayout.openDrawer(GravityCompat.START);
//        }
//        return super.onOptionsItemSelected(item);
//    }

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

    @Override
    public void onBackPressed() {
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.created_for)
                .setMessage(R.string.confirm_exit)
                .show().setCanceledOnTouchOutside(true);
        dialog = builder.create();
        dialog.dismiss();
    }
}
