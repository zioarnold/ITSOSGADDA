package com.example.arnold.itsosgadda.handlers;

import static android.view.Window.FEATURE_ACTION_BAR;
import static com.example.arnold.itsosgadda.R.id.about_app;
import static com.example.arnold.itsosgadda.R.id.container;
import static com.example.arnold.itsosgadda.R.layout.fragment_main_navitagion_drawer;
import static com.example.arnold.itsosgadda.R.menu.main_menu;
import static com.example.arnold.itsosgadda.R.string.ok;
import static com.example.arnold.itsosgadda.handlers.NavigationDrawerFragment.NavigationDrawerCallbacks;
import static java.lang.Boolean.TYPE;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.activities.SendBugCrashReport;
import com.example.arnold.itsosgadda.main.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class MapsLoader extends FragmentActivity implements NavigationDrawerCallbacks, OnMapReadyCallback {

    private static final String TAG = "MapsLoader";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private final LatLng myLatLng = new LatLng(44.693950, 10.106832);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffeb3b")));
        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        setUpMapIfNeeded();
        makeActionOverflowMenuShown();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        try {
            if (featureId == FEATURE_ACTION_BAR && menu != null) {
                if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                    @SuppressLint("PrivateApi") Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
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
                            .setPositiveButton(ok, (dialog, which) -> dialog.dismiss()).show().setCanceledOnTouchOutside(true);
                    break;
                case about_app:
                    builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.icon_about)
                            .setTitle(R.string.created_for)
                            .setView(getLayoutInflater().inflate(R.layout.handler_version_app, null))
                            .setPositiveButton(ok, (dialog, which) -> dialog.dismiss())
                            .show().setCanceledOnTouchOutside(true);
                    AlertDialog dialog = builder.create();
                    dialog.dismiss();
                    break;
                case R.id.subscribe:
                    startActivity(new Intent(getApplicationContext(), SendBugCrashReport.class));
                    break;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
        return false;
    }

    private void makeActionOverflowMenuShown() {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            @SuppressLint("PrivateApi") Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            Objects.requireNonNull(mapFragment).getMapAsync(this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                onMapReady(mMap);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher)
                        .setTitle(R.string.no_network_enabled)
                        .setMessage(R.string.network_enabled)
                        .setPositiveButton(R.string.yes, (dialog, which) -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
                AlertDialog dialog = builder.create();

                dialog.dismiss();
            }
        }
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.addPolyline(new PolylineOptions()
                .add(myLatLng)
                .width(5)
                .visible(true)
                .color(Color.RED));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
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
            Log.d(TAG, ex.getMessage());
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
            try {
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage());
            }
            return fragment;
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
}
