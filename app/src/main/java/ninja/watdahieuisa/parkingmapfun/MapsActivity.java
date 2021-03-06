package ninja.watdahieuisa.parkingmapfun;

import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap;
    private Marker mPositionMarker;
    private ArrayList<LatLng> pointsList = new ArrayList<LatLng>();
    private static final String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        //Showing Status
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mMap = mapFragment.getMap();

            //mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, false);

            if(bestProvider != null && !bestProvider.equals(""))
            {
                try {
                    Location location = locationManager.getLastKnownLocation(bestProvider);
                    locationManager.requestLocationUpdates(bestProvider, 20000, 1, this);
                    if (location != null) {
                        onLocationChanged(location);
                    }
                }
                catch (SecurityException se)
                {
                    Toast.makeText(getBaseContext(),"Sorry, Enable Location Security.", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Log.e(TAG,"No provider could be found");
            }



            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    drawMarker(point);
                    pointsList.add(point);

                    Toast.makeText(getBaseContext(), "Marker is added to the map", Toast.LENGTH_LONG).show();
                }
            });
        }
        if(savedInstanceState != null)
        {
            if(savedInstanceState.containsKey("points")){
                pointsList = savedInstanceState.getParcelableArrayList("points");
                if(pointsList != null)
                {
                    for(int i=0;i<pointsList.size(); i++)
                    {
                        drawMarker(pointsList.get(i));
                    }
                }
            }
        }
    }

    private void drawMarker(LatLng point) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title("Lat: " + point.latitude + "," + "Lng: " + point.longitude);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

//    public void animateMarker(final Marker marker, final Location location) {
//
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        final LatLng startLatLng = marker.getPosition();
//        final double startRotation = marker.getRotation();
//        final long duration = 500;
//
//        final Interpolator interpolator = new LinearInterpolator();
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed
//                        / duration);
//
//                double lng = t * location.getLongitude() + (1 - t)
//                        * startLatLng.longitude;
//                double lat = t * location.getLatitude() + (1 - t)
//                        * startLatLng.latitude;
//
//                float rotation = (float) (t * location.getBearing() + (1 - t)
//                        * startRotation);
//
//                marker.setPosition(new LatLng(lat, lng));
//                marker.setRotation(rotation);
//
//                if (t < 1.0) {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16);
//                }
//            }
//        });
//    }

    private void drawCar(LatLng point)
    {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.car);
        MarkerOptions markerOptions = new MarkerOptions().position(point)
                .icon(icon)
                .flat(true)
                .anchor(0.5f,0.5f);
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList("points", pointsList);

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();


        if (location != null)
        {
            if (mPositionMarker == null) {
                mPositionMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                        .flat(true)
                        .anchor(0.5f, 0.5f));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
