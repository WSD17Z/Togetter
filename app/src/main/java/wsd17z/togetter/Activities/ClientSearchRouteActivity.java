package wsd17z.togetter.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import wsd17z.togetter.MapsModules.DirectionFinder;
import wsd17z.togetter.MapsModules.DirectionFinderListener;
import wsd17z.togetter.MapsModules.Route;
import wsd17z.togetter.R;

public class ClientSearchRouteActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> mMapMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_search_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
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
        if (canAccessLocation()) {

        }
        else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1264);
        }
        if (canAccessLocation()) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }
    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (mMapMarkers != null) {
            for (Marker marker : mMapMarkers) {
                marker.remove();
            }
            mMapMarkers.clear();
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
            destinationMarkers.clear();
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
            polylinePaths.clear();
        }
    }

    @Override
    public void onDirectionFinderSuccess(Route route) {
        progressDialog.dismiss();

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
        polylinePaths.add(mMap.addPolyline(polylineOptions));
    }
}
