package com.example.arnold.itsosgadda.main;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.activities.ComActivity;
import com.example.arnold.itsosgadda.activities.EmailSendingActivity;
import com.example.arnold.itsosgadda.activities.SendBugCrashReport;
import com.example.arnold.itsosgadda.activities.SpecStorySectionActivity;
import com.example.arnold.itsosgadda.activities.StoryActivity;
import com.example.arnold.itsosgadda.activities.WebRegistryActivity;
import com.example.arnold.itsosgadda.handlers.MapsLoader;
import com.example.arnold.itsosgadda.handlers.NavigationDrawerFragment;
import com.example.arnold.itsosgadda.utilities.Log4jHelper;
import com.pushbots.push.Pushbots;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.view.View.OnClickListener;
import static android.view.Window.FEATURE_ACTION_BAR;
import static com.example.arnold.itsosgadda.R.id.about_app;
import static com.example.arnold.itsosgadda.R.id.button_show_comunications;
import static com.example.arnold.itsosgadda.R.id.container;
import static com.example.arnold.itsosgadda.R.id.e_registryId;
import static com.example.arnold.itsosgadda.R.id.feedback;
import static com.example.arnold.itsosgadda.R.id.findus;
import static com.example.arnold.itsosgadda.R.id.photoGallery;
import static com.example.arnold.itsosgadda.R.id.specSectionButtonId;
import static com.example.arnold.itsosgadda.R.id.storyButton;
import static com.example.arnold.itsosgadda.R.layout.activity_main;
import static com.example.arnold.itsosgadda.R.layout.fragment_main_navitagion_drawer;
import static com.example.arnold.itsosgadda.R.menu.main_menu;
import static com.example.arnold.itsosgadda.handlers.NavigationDrawerFragment.NavigationDrawerCallbacks;
import static java.lang.Boolean.TYPE;

public class MainActivity extends Activity implements OnClickListener,
        NavigationDrawerCallbacks {
    private Button storyButtonMainBody, specSectButton, webRegistryButton, feedBackButton,
            findUsButton, communicationButton, bPhotoGallery;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Pushbots.sharedInstance().init(this);
            Pushbots.sharedInstance().register();
            Pushbots.sharedInstance().setPushEnabled(true);

            setContentView(activity_main);

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

            communicationButton = (Button) findViewById(R.id.button_show_comunications);
            communicationButton.setOnClickListener(this);

            bPhotoGallery = (Button) findViewById(R.id.photoGallery);
            bPhotoGallery.setOnClickListener(this);

            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.navigation_drawer);
            mTitle = getTitle();

            // Set up the drawer.
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));

            ActionBar actionBar = getActionBar();
            assert actionBar != null;
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffeb3b")));

            makeActionOverflowMenuShown();
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("MainActivity");
            log.error("Error", ex);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        try {
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(container, PlaceholderFragment.newInstance(position + 1))
                    .commit();
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("MainActivity");
            log.error("Error", ex);
        }
    }

    /*public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.en_lang);
                break;
            case 2:
                mTitle = getString(R.string.ru_lang);
                break;
            case 3:
                mTitle = getString(R.string.it_lang);
                break;
        }
    }*/

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        //noinspection deprecation
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            try {
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
            } catch (Exception ex) {
                Logger log = Log4jHelper.getLogger("MainActivity");
                log.error("Error", ex);
            }
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(fragment_main_navitagion_drawer, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            /*((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));*/
        }
    }

    private void makeActionOverflowMenuShown() {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            Log.d(null, e.getLocalizedMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            getMenuInflater().inflate(main_menu, menu);
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("MainActivity");
            log.error("Error", ex);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        try {
            if (featureId == FEATURE_ACTION_BAR && menu != null) {
                if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
            }
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("MainActivity");
            log.error("Error", ex);
        }
        return super.onMenuOpened(featureId, menu);
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
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
                    builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.icon_subscribe_contact)
                            .setTitle(R.string.dev_contact)
                            .setView(getLayoutInflater().inflate(R.layout.contact_to_developer, null))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    dialog = builder.create();
                    dialog.dismiss();
                    break;
                case R.id.crash_report:
                    startActivity(new Intent(getApplicationContext(), SendBugCrashReport.class));
                    break;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("MainActivity");
            log.error("Error", ex);
        }
        return false;
    }

    private void storyButtonClicked() {
        startActivity(new Intent(getApplicationContext(), StoryActivity.class));
    }

    private void specSectButtonClicked() {
        startActivity(new Intent(getApplicationContext(), SpecStorySectionActivity.class));
    }

    private void webRegistryButtonClicked() {
        startActivity(new Intent(getApplicationContext(), WebRegistryActivity.class));
    }


    private void feedbackMailToButtonClicked() {
        startActivity(new Intent(getApplicationContext(), EmailSendingActivity.class));
    }

    private void communicationButtonClicked() {
        startActivity(new Intent(getApplicationContext(), ComActivity.class));
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
                    communicationButtonClicked();
                    break;
                case findus:
                    builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.ic_launcher)
                            .setTitle(R.string.created_for)
                            .setMessage(R.string.reaching_from_fornovo_FS)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getApplicationContext(),
                                            MapsLoader.class));
                                }
                            })
                            .show().setCanceledOnTouchOutside(true);
                    dialog = builder.create();
                    dialog.dismiss();
                    break;
                case photoGallery:
                    builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.ic_launcher)
                            .setTitle(R.string.created_for)
                            .setMessage(R.string.still_working)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show().setCanceledOnTouchOutside(true);
                    dialog = builder.create();
                    dialog.dismiss();
                    break;
            }
        } catch (Exception ex) {
            Logger log = Log4jHelper.getLogger("MainActivity");
            log.error("Error", ex);
        }
    }

    /*@Override
    public void onStart() {
        super.onStart();
        Logger log = Log4jHelper.getLogger("MainActivity");
        log.warn("Activity started");
        Log.v("Main", "++ ON START ++");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("Main", "+ ON RESUME +");
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger log = Log4jHelper.getLogger("MainActivity");
        log.warn("Activity paused");
        Log.v("Main", "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
        Logger log = Log4jHelper.getLogger("MainActivity");
        log.warn("Activity finished");
        Log.v("Main", "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger log = Log4jHelper.getLogger("MainActivity");
        log.warn("Activity destroyed");
        Log.v("Main", "- ON DESTROY -");
    }*/
}