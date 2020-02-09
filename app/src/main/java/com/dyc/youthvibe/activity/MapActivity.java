package com.dyc.youthvibe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;



import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dyc.youthvibe.R;
import com.dyc.youthvibe.utils.GPSTracker;
import com.dyc.youthvibe.utils.PopUtil;
import com.dyc.youthvibe.utils.jsonLoader;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class MapActivity extends AppCompatActivity {


    Toolbar toolbar;

    private GoogleMap mMaped;

    Context context;

    MapView mMapView;
    private GoogleMap googleMap;
    FusedLocationProviderClient mFusedLocationProviderClient;
    LatLng latLng;
    String locName = "BLANK" , locEvent = "BLANK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initUI();


        setSupportActionBar(toolbar);

        mMapView.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setupListeners();




    }

    void checkInentData(){

        if (getIntent().hasExtra("LatLng")){

            String temp = getIntent().getStringExtra("LatLng");



           latLng =  new LatLng(Double.parseDouble((temp.substring(0,temp.indexOf(',')))),Double.parseDouble(temp.substring(temp.indexOf(',')+1)));

           locEvent =  getIntent().getStringExtra("loc");

            locName =  getIntent().getStringExtra("event");



        }


    }

    void goToLocation(){




        Drawable circleDrawable = ContextCompat.getDrawable(context, R.drawable.e2);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

        Log.d("TEST",latLng+" ");


        googleMap.addMarker(new MarkerOptions().position(latLng).title(locName).snippet(locEvent).icon(markerIcon));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(18).tilt(60).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.help) {

            // notify.startAnimation(anim);
            new PopUtil(this, 1).show();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); //Terminate the current Activity
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    void initUI() {


        context = this;
        toolbar = findViewById(R.id.toolbar);
        mMapView = findViewById(R.id.mapView);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    void setupListeners() {


        mMapView.onResume();

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {


            @Override
            public void onMapReady(GoogleMap mMap) {

                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                        context, R.raw.aubergine_google_map));

                googleMap = mMap;

                googleMap.setBuildingsEnabled(true);


                // For showing a move to my location button
                googleMap.setMyLocationEnabled(false);


                // For dropping a marker at a point on the Map

                LatLng temp = null;

//
//                String lat = "31.2520607";
//                String lng = "75.701942";


//
//
                checkInentData();


                JSONObject jsonObject;

                try {

                    jsonObject = new jsonLoader(context, "mapData.json").loadJSONFromAsset();





                    Iterator<String> iter = jsonObject.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        try {





                                temp = new LatLng(Float.parseFloat(jsonObject.getJSONObject(key).get("LAT").toString()), Float.parseFloat(jsonObject.getJSONObject(key).get("LNG").toString()));



                       if (!locEvent.toLowerCase().contains(key.toLowerCase()))
                                googleMap.addMarker(new MarkerOptions().position(temp).title(key));

                            Log.d("LOGGED2",locName);

                        } catch (JSONException e) {

                            Log.d("LOGGED",e.getMessage());

                        }
                    }




                }
                catch (Exception e){

                    //Show error dialog here


                }






               // googleMap.addMarker(new MarkerOptions().position(temp).title("SSDM Reception Area").snippet("LPU")).setIcon(markerIcon);


                if (!getIntent().hasExtra("LatLng")) {
                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(temp).zoom(17).tilt(60).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                else    goToLocation();
                mMaped = googleMap;

            }


        });


    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting DialogHelp Title
        alertDialog.setTitle("GPS is settings");

        // Setting DialogHelp Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }


    public void targetCurrentLocation(View view) {


        Log.d("TESTING","DATS");
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */


                LocationManager locationManager = (LocationManager) context
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
               Boolean isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
              Boolean  isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


                if (!isGPSEnabled && !isNetworkEnabled) {
                    showSettingsAlert();

                }

                else getDeviceLocation();

            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {

                Toast.makeText(context, "Permiission Error", Toast.LENGTH_SHORT).show();

            }
        }).check();
    }





    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void getDeviceLocation() {

        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

    if (latitude!=0 && longitude!=0) {

        Drawable circleDrawable = ContextCompat.getDrawable(context, R.drawable.my_loc_map);

        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

        LatLng target = new LatLng(latitude,
                longitude);

        mMaped.addMarker(new MarkerOptions().position(target).title("Current Location").snippet("You are currently here")).setIcon(markerIcon);


        CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.zoom(18);
        builder.tilt(60);
        builder.target(target);

        mMaped.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));


    }
    
    else Toast.makeText(context, "Please try again in few seconds.", Toast.LENGTH_SHORT).show();

        }



}
