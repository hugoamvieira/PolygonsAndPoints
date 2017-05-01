package io.github.hugoamvieira.polygonsandpoints;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(16.0f);
        mMap.setMaxZoomPreference(25.0f);

        // Received polygon
        if (getIntent().getSerializableExtra("Polygon") != null) {
            final Polygon polygon = (Polygon) getIntent().getSerializableExtra("Polygon");

            Point[] points = polygon.getPolygonPoints();
            LatLng[] latLngs = new LatLng[points.length];

            for (int i = 0; i < points.length; i++) {
                double lat = points[i].getLatitude();
                double lng = points[i].getLongitude();

                latLngs[i] = new LatLng(lat, lng);
            }

            com.google.android.gms.maps.model.Polygon polygonPath = mMap.addPolygon(new PolygonOptions().clickable(false).add(latLngs));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngs[0]));

        } else {
            // It's just a point
            final Point p = (Point) getIntent().getSerializableExtra("Point");
            LatLng pos = new LatLng(p.getLatitude(), p.getLongitude());

            mMap.addMarker(new MarkerOptions().position(pos).title(p.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        }
    }
}
