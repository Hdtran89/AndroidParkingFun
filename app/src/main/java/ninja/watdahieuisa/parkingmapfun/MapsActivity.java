package ninja.watdahieuisa.parkingmapfun;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private ArrayList<LatLng> pointsList = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        //Showing Status
        if(status != ConnectionResult.SUCCESS){
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,this, requestCode);
            dialog.show();
        }
        else {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mMap = mapFragment.getMap();

            mMap.setMyLocationEnabled(true);

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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList("points",pointsList);

        super.onSaveInstanceState(outState);
    }

}
