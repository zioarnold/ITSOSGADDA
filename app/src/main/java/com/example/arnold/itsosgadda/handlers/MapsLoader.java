package com.example.arnold.itsosgadda.handlers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
import com.example.arnold.itsosgadda.databinding.ActivityMapsBinding;
import com.example.arnold.itsosgadda.main.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;

import org.apache.log4j.Logger;

import java.util.Objects;

@SuppressWarnings("FieldCanBeLocal")
public class MapsLoader extends AppCompatActivity implements OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MapsLoader";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private final LatLng myLatLng = new LatLng(44.693950, 10.106832);

    private ActivityMapsBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = ActivityMapsBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

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
            ).setOpenableLayout(drawerLayout).build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            setUpMapIfNeeded();
        } catch (Exception exception) {
            Log.d(TAG, exception.getMessage());
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
            getMenuInflater().inflate(R.menu.main_menu, menu);
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
                builder = new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher)
                        .setTitle(R.string.no_network_enabled)
                        .setMessage(R.string.network_enabled)
                        .setPositiveButton(R.string.yes, (dialog, which) -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
                dialog = builder.create();
                dialog.dismiss();
            }
        }
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions()
        .position(myLatLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
    }

    @SuppressLint("InflateParams")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        try {
            int id = item.getItemId();
            if (id == R.id.dev_team) {
                builder = new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.icon_dev_team)
                        .setTitle(R.string.dev_team)
                        .setView(getLayoutInflater().inflate(R.layout.handler_dev_team, null))
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show().setCanceledOnTouchOutside(true);
            } else if (id == R.id.subscribe) {
                startActivity(new Intent(getApplicationContext(), SendBugCrashReport.class));
            }
        } catch (Exception ex) {
            Logger log = Logger.getLogger("AFMActivity");
            log.warn(ex.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else if (id == R.id.nav_our_story) {
            startActivity(new Intent(getApplicationContext(), StoryActivity.class));
        } else if (id == R.id.nav_study_addresses) {
            startActivity(new Intent(getApplicationContext(), SpecStorySectionActivity.class));
        } else if (id == R.id.nav_e_registry_link) {
            startActivity(new Intent(getApplicationContext(), WebRegistryActivity.class));
        } else if (id == R.id.nav_feedback_to_staff) {
            startActivity(new Intent(getApplicationContext(), EmailSendingActivity.class));
        } else if (id == R.id.nav_findus) {
            startActivity(new Intent(getApplicationContext(), MapsLoader.class));
        } else if (id == R.id.nav_app_blog) {
            startActivity(new Intent(getApplicationContext(), RSSReaderActivity.class));
        }
        return true;
    }
}
