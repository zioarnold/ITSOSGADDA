package com.example.arnold.itsosgadda.handlers;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.arnold.itsosgadda.main.MainActivity;
import com.example.arnold.itsosgadda.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationChangeListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffeb3b")));
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher)
                        .setTitle(R.string.no_network_enabled)
                        .setMessage(R.string.network_enabled)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.dismiss();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);
        if (myLocation != null) {
            myLocation.getLatitude();
            myLocation.getLongitude();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.mipmap.ic_launcher)
                    .setTitle(R.string.no_network_enabled)
                    .setMessage(R.string.network_enabled)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.dismiss();
        }
        LatLng myLatLng = new LatLng(44.692785, 10.102958);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.findus)
                .setMessage(R.string.enable_position_yes_no)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMap.setMyLocationEnabled(false);
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMap.setMyLocationEnabled(true);
                    }
                }).show().setCanceledOnTouchOutside(true);
        AlertDialog dialog = builder.create();
        dialog.dismiss();
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setOnMyLocationChangeListener(this);


        mMap.addMarker(new MarkerOptions().position(new LatLng(44.693793, 10.102023))
                .title("Fornovo di Taro Railway Station"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(44.692785, 10.102958))
                .title("You're almost here"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(44.693950, 10.106832))
                .title("Welcome to I.I.S.S. Carlo Emilio Gadda"));

        mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(44.693793, 10.102023))
                .add(new LatLng(44.692785, 10.102958))
                .add(new LatLng(44.693950, 10.106832))
                .width(5)
                .color(Color.RED));

    }

    @Override
    public void onMyLocationChange(Location location) {
        Criteria criteria = new Criteria();
        LocationManager locationManagerUpd = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManagerUpd.getBestProvider(criteria, true);
        Location myLocationUpd = locationManagerUpd.getLastKnownLocation(provider);
        myLocationUpd.getProvider();
    }
}
