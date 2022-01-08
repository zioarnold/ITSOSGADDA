package com.example.arnold.itsosgadda.main;

import static com.example.arnold.itsosgadda.R.id.about_app;
import static com.example.arnold.itsosgadda.R.id.app_blog;
import static com.example.arnold.itsosgadda.R.id.button_show_comunications;
import static com.example.arnold.itsosgadda.R.id.e_registryId;
import static com.example.arnold.itsosgadda.R.id.feedback;
import static com.example.arnold.itsosgadda.R.id.findus;
import static com.example.arnold.itsosgadda.R.id.nav_home;
import static com.example.arnold.itsosgadda.R.id.specSectionButtonId;
import static com.example.arnold.itsosgadda.R.id.storyButton;
import static com.example.arnold.itsosgadda.R.menu.main_menu;
import static com.example.arnold.itsosgadda.R.string.ok;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Pushbots.sharedInstance().init(this);
            Pushbots.sharedInstance().register();
            Pushbots.sharedInstance().setPushEnabled(true);
            Pushbots.sharedInstance().setRegStatus(false);
            Pushbots.sharedInstance().unregister();


            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            storyButtonMainBody = (Button) findViewById(storyButton);
            storyButtonMainBody.setOnClickListener(this);

            specSectButton = (Button) findViewById(specSectionButtonId);
            specSectButton.setOnClickListener(this);

            webRegistryButton = (Button) findViewById(e_registryId);
            webRegistryButton.setOnClickListener(this);

            feedBackButton = (Button) findViewById(feedback);
            feedBackButton.setOnClickListener(this);

            findUsButton = (Button) findViewById(findus);
            findUsButton.setOnClickListener(this);

            communicationButton = (Button) findViewById(button_show_comunications);
            communicationButton.setOnClickListener(this);

            rssFeedReader = (Button) findViewById(app_blog);
            rssFeedReader.setOnClickListener(this);

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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            getMenuInflater().inflate(main_menu, menu);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
        return super.onCreateOptionsMenu(menu);
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
            startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "eu.spaggiari.classeviva"));
            startActivity(intent);
        }
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
                    builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.icon_dev_team)
                            .setTitle(R.string.dev_team)
                            .setView(getLayoutInflater().inflate(R.layout.handler_dev_team, null))
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show().setCanceledOnTouchOutside(true);
                    break;
                case about_app:
                    builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.icon_about)
                            .setTitle(R.string.created_for)
                            .setView(getLayoutInflater().inflate(R.layout.handler_version_app, null))
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .show().setCanceledOnTouchOutside(true);
                    dialog = builder.create();
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

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case storyButton:
                    storyButtonClicked();
                    break;
                case specSectionButtonId:
                    specSectButtonClicked();
                    break;
                case e_registryId:
                    webRegistryButtonClicked();
                    break;
                case feedback:
                    feedbackMailToButtonClicked();
                    break;
                case button_show_comunications:
                    builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.ic_launcher)
                            .setTitle(R.string.created_for)
                            .setMessage(R.string.feature_under_work)
                            .show().setCanceledOnTouchOutside(true);
                    dialog = builder.create();
                    dialog.dismiss();
//                    communicationButtonClicked();
                    break;
                case app_blog:
                    rssNewsButtonClicked();
                    break;
                case findus:
                    builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.ic_launcher)
                            .setTitle(R.string.created_for)
                            .setMessage(R.string.reaching_from_fornovo_FS)
                            .setPositiveButton(ok, (dialog, which) -> startActivity(new Intent(getApplicationContext(),
                                    MapsLoader.class)))
                            .show().setCanceledOnTouchOutside(true);
                    dialog = builder.create();
                    dialog.dismiss();
                    break;
            }
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
