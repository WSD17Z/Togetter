package wsd17z.togetter.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import wsd17z.togetter.Driver.IUserService;
import wsd17z.togetter.MapsModules.DirectionFinder;
import wsd17z.togetter.MapsModules.DirectionFinderListener;
import wsd17z.togetter.MapsModules.GMapsLauncher;
import wsd17z.togetter.MapsModules.Route;
import wsd17z.togetter.R;
import wsd17z.togetter.Utils;

public class DriverSetRoadActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    private EditText mDestinationText;
    private Button mGoBtn;
    private List<Marker> mMapMarkers = new ArrayList<>();
    private List<Marker> mDestinationMarkers = new ArrayList<>();
    private List<Polyline> mPolylinePaths = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng mCurrentLocation;
    private LatLng mDestinationLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_set_road);

        // get location services provider to access current location
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // get destination textbox
        mDestinationText = (EditText) findViewById(R.id.etDestination);

        // set up button listeners
        findViewById(R.id.btnFindPath).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        mGoBtn = findViewById(R.id.btnGo);
        mGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String destination = mDestinationText.getText().toString();

                IFuture<IUserService> userFuture = MainActivity.getPlatform().getService(MainActivity.getPlatform().getPlatformId(), IUserService.class);
                userFuture.addResultListener(new IResultListener<IUserService>() {
                    @Override
                    public void exceptionOccurred(Exception exception) {
                        Log.d("ERR", exception.toString());
                    }

                    @Override
                    public void resultAvailable(IUserService result) {
                        result.setEndPoints(mCurrentLocation.latitude, mCurrentLocation.longitude,
                                mDestinationLocation.latitude, mDestinationLocation.longitude);
                        startActivity(GMapsLauncher.getNavigationIntent(Utils.latLngToString(mCurrentLocation), destination));
                    }
                });

            }
        });

    }

    private void getDeviceLocation() {
        try {
            if (Utils.canAccessLocation(getApplicationContext())) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location lastKnownLocation = (Location)task.getResult();
                            mCurrentLocation = new LatLng(lastKnownLocation.getLatitude(),
                                lastKnownLocation.getLongitude());
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void sendRequest() {
        String origin = Utils.latLngToString(mCurrentLocation);
        String destination = mDestinationText.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Current location cannot be accessed!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setTrafficEnabled(true);
        if (!Utils.canAccessLocation(getApplicationContext())) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1264);
        }
        if (Utils.canAccessLocation(getApplicationContext())) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            getDeviceLocation();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        mGoBtn.setEnabled(false);
        mProgressDialog = ProgressDialog.show(this, "Please wait!",
                "Finding direction...", true);

        if (mMapMarkers != null) {
            for (Marker marker : mMapMarkers) {
                marker.remove();
            }
            mMapMarkers.clear();
        }

        if (mDestinationMarkers != null) {
            for (Marker marker : mDestinationMarkers) {
                marker.remove();
            }
            mDestinationMarkers.clear();
        }

        if (mPolylinePaths != null) {
            for (Polyline polyline: mPolylinePaths) {
                polyline.remove();
            }
            mPolylinePaths.clear();
        }
    }

    @Override
    public void onDirectionFinderSuccess(Route route) {
        mProgressDialog.dismiss();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
        ((TextView) findViewById(R.id.tvDuration)).setText(
                String.format(Locale.getDefault(), "%1$d min", route.duration.value / 60)
        );
        ((TextView) findViewById(R.id.tvDistance)).setText(
                String.format(Locale.getDefault(), "%1$.1f km", route.distance.value / 1000f)
        );

        mMapMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title(route.startAddress)
                        .position(route.startLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                )
        );

        for (int i = 0; i < route.legs.size() - 1; ++i) {
            mMapMarkers.add(mMap.addMarker(new MarkerOptions()
                            .title(route.legs.get(i).endAddress)
                            .position(route.legs.get(i).endLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
            );
        }

        mMapMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title(route.endAddress)
                        .position(route.endLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                )
        );

        PolylineOptions polylineOptions = new PolylineOptions().
                geodesic(true).
                color(Color.BLUE).
                width(10);

        for (LatLng point : route.points) {
            polylineOptions.add(point);
        }
        mPolylinePaths.add(mMap.addPolyline(polylineOptions));
        mDestinationLocation = route.endLocation;
        mGoBtn.setEnabled(true);
    }
}
