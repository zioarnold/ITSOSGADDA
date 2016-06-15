package com.example.arnold.itsosgadda.activities;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.handlers.NavigationDrawerFragment;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.view.View.OnClickListener;
import static com.example.arnold.itsosgadda.R.id.AFM_button;
import static com.example.arnold.itsosgadda.R.id.IT_TLC_button;
import static com.example.arnold.itsosgadda.R.id.LSA_button;
import static com.example.arnold.itsosgadda.R.id.MAT_button;
import static com.example.arnold.itsosgadda.R.id.about_app;
import static com.example.arnold.itsosgadda.handlers.NavigationDrawerFragment.NavigationDrawerCallbacks;


@SuppressWarnings("FieldCanBeLocal")
public class SpecStorySectionActivity extends Activity implements OnClickListener,
        NavigationDrawerCallbacks {

    private Button lyceumButton, it_tlcButton, economicalButton, matButton;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.spec_study_sections_layout);

            lyceumButton = (Button) findViewById(LSA_button);
            lyceumButton.setOnClickListener(this);

            it_tlcButton = (Button) findViewById(IT_TLC_button);
            it_tlcButton.setOnClickListener(this);

            economicalButton = (Button) findViewById(AFM_button);
            economicalButton.setOnClickListener(this);

            matButton = (Button) findViewById(MAT_button);
            matButton.setOnClickListener(this);

            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.navigation_drawer);

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
            Logger log = Logger.getLogger("SpecStorySectionActivity");
            log.warn(ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        } catch (Exception ex) {
            Logger log = Logger.getLogger("SpecStorySectionActivity");
            log.warn(ex.getMessage());
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        try {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                    .commit();
        } catch (Exception ex) {
            Logger log = Logger.getLogger("SpecStorySectionActivity");
            log.warn(ex.getMessage());
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
        } catch (Exception ex) {
            Logger log = Logger.getLogger("SpecStorySectionActivity");
            log.warn(ex.getMessage());
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        try {
            if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
                if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
            }
        } catch (Exception ex) {
            Logger log = Logger.getLogger("SpecStorySectionActivity");
            log.warn(ex.getMessage());
        }
        return super.onMenuOpened(featureId, menu);
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
            Logger log = Logger.getLogger("SpecStorySectionActivity");
            log.warn(ex.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }

    private void lyceumButtonClicked() {
        startActivity(new Intent(getApplicationContext(), LSActivity.class));
    }

    private void itTlcButtonClicked() {
        startActivity(new Intent(getApplicationContext(), ITActivity.class));
    }

    private void economicalButtonClicked() {
        startActivity(new Intent(getApplicationContext(), AFMActivity.class));
    }

    private void matButtonClicked() {
        startActivity(new Intent(getApplicationContext(), MATActivity.class));
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case LSA_button:
                    lyceumButtonClicked();
                    break;
                case IT_TLC_button:
                    itTlcButtonClicked();
                    break;
                case AFM_button:
                    economicalButtonClicked();
                    break;
                case MAT_button:
                    matButtonClicked();
                    break;
            }
        } catch (Exception ex) {
            Logger log = Logger.getLogger("SpecStorySectionActivity");
            log.warn(ex.getMessage());
        }
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

        public PlaceholderFragment() {
        }

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
                Logger log = Logger.getLogger("SpecStorySectionActivity");
                log.warn(ex.getMessage());
            }
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main_navitagion_drawer, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            /*((SpecStorySectionActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));*/
        }
    }
}
