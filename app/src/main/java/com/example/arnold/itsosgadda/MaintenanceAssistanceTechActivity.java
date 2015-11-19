package com.example.arnold.itsosgadda;

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
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.arnold.itsosgadda.handlers.NavigationDrawerFragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.example.arnold.itsosgadda.R.id.about_app;


public class MaintenanceAssistanceTechActivity extends Activity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mech_elet_assist_tech_layout);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffeb3b")));
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        makeActionOverflowMenuShown();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
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
    }

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
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main_navitagion_drawer, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            ((MaintenanceAssistanceTechActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    Log.e("MyActivity", "onMenuOpened", e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @SuppressLint("InflateParams")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
                TextView tvDisplay = new TextView(this);
                final String data = "- vkontakte: https://vk.com/arnold.charyyev\n" +
                        "- facebook: https://www.facebook.com/schyzomaniac.mind\n" +
                        "- youtube: https://www.youtube.com/user/Perceus100\n";
                tvDisplay.setText(data);
                tvDisplay.setLinksClickable(true);
                tvDisplay.setAutoLinkMask(RESULT_OK);
                tvDisplay.setMovementMethod(LinkMovementMethod.getInstance());
                Linkify.addLinks(tvDisplay, Linkify.ALL);

                builder = new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.icon_subscribe_contact)
                        .setTitle(R.string.dev_contact)
                        .setView(tvDisplay)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                AlertDialog dialogAlert = builder.create();
                dialogAlert.dismiss();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
