package com.epam.beacons2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.epam.beacons2.dijkstra.engine.DijkstraAlgorithm;
import com.epam.beacons2.dijkstra.manager.Graph;
import com.epam.beacons2.dijkstra.model.Vertex;
import com.epam.beacons2.util.ScannedDevice;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class MockActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView proximity;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private GroundOverlayOptions groundOverlayOptions;
    private GroundOverlayOptions groundOverlayOptions2;
    private LatLng epamLocReal = new LatLng(59.9851017, 30.3097383);
    private LatLng epamLoc0 = new LatLng(0, 0);
    private LatLng epamLoc1 = new LatLng(-0.0001, -0.0001);
    private LatLng epamLoc2 = new LatLng(0.0001, 0.0001);
    private LatLng epamLoc3 = new LatLng(-0.0002, -0.0002);
    private LatLng epamLoc4 = new LatLng(-0.0001, -0.0001);
    private LatLng epamLoc5 = new LatLng(0.0006, -0.0006);
    private LatLng epamLoc6 = new LatLng(0.0004, 0.0004);
    private LatLng epamLoc7 = new LatLng(-0.0004, -0.0004);
    private LatLng epamLoc8 = new LatLng(0.0004, -0.0003);
    private LatLng epamLoc9 = new LatLng(-0.0003, -0.0003);
    private LatLng epamLoc10 = new LatLng(0.0005, 0.0005);

    private double latitude = 0.0;
    private double longitude = 0.0;

    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothAdapter mBTAdapter;
    private boolean mIsScanning;
    private ArrayList<ScannedDevice> scannedDevices = new ArrayList<>();

    private final static String PROXIMITY = "iBeacon, proximity: ";
    private final static String ACCURACY = "iBeacon, accuracy: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        proximity = (TextView) findViewById(R.id.proximity);

        groundOverlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.map))
                .position(epamLoc0, 110f, 80f);

        groundOverlayOptions2 = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.map2))
                .position(epamLocReal, 110f, 80f);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    @SuppressWarnings("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);

//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        mFusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//                    }
//                });

        // LatLng southwest, LatLng northeast
        LatLngBounds epamBounds = new LatLngBounds(epamLoc0, epamLoc0);
        LatLngBounds epamCameraBounds = new LatLngBounds(epamLoc1, epamLoc2);

        mMap.addMarker(new MarkerOptions().position(epamLoc1).title("Marker in Current Location"));
        mMap.addMarker(new MarkerOptions().position(epamLoc2).title("Marker in Current Location"));
        mMap.addMarker(new MarkerOptions().position(epamLoc3).title("Marker in Current Location"));
        mMap.addMarker(new MarkerOptions().position(epamLoc4).title("Marker in Current Location"));
        mMap.addMarker(new MarkerOptions().position(epamLoc5).title("Marker in Current Location"));
        mMap.addMarker(new MarkerOptions().position(epamLoc6).title("Marker in Current Location"));
        mMap.addMarker(new MarkerOptions().position(epamLoc7).title("Marker in Current Location"));
        mMap.addMarker(new MarkerOptions().position(epamLoc8).title("Marker in Current Location"));
        mMap.addMarker(new MarkerOptions().position(epamLoc9).title("Marker in Current Location"));
        mMap.addMarker(new MarkerOptions().position(epamLoc10).title("Marker in Current Location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(epamBounds.getCenter(), 0));
        mMap.setLatLngBoundsForCameraTarget(epamCameraBounds);

        mMap.setMyLocationEnabled(true);

        final GroundOverlay imageOverlay = mMap.addGroundOverlay(groundOverlayOptions);
        imageOverlay.setClickable(true);

        mMap.setMinZoomPreference(20.5f);
        mMap.setMaxZoomPreference(30.0f);

        DijkstraAlgorithm algorithm = new DijkstraAlgorithm(createGraph());
        algorithm.execute(1);
        List <Vertex> vertexList = algorithm.getPath(9);


        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        vertexList.get(0).getLatLng(),
                        vertexList.get(1).getLatLng(),
                        vertexList.get(2).getLatLng()
//                        vertexList.get(3).getLatLng()
//                        vertexList.get(4).getLatLng(),
//                        vertexList.get(5).getLatLng(),
//                        vertexList.get(6).getLatLng(),
//                        vertexList.get(7).getLatLng(),
//                        vertexList.get(8).getLatLng(),
//                        vertexList.get(9).getLatLng()
                ));

        mMap.setOnGroundOverlayClickListener(new GoogleMap.OnGroundOverlayClickListener() {
            @Override
            public void onGroundOverlayClick(GroundOverlay groundOverlay) {
                Context context = getApplicationContext();
                String text = "Hello!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

//                GroundOverlay imageOverlay2 = mMap.addGroundOverlay(groundOverlayOptions2);

            }
        });
    }

    public Graph createGraph() {
        Graph graph = new Graph();

        graph.addVertex(new Vertex(1, "", 0, 0)).
                addVertex(new Vertex(2, "", -0.0001, -0.0001)).
                addVertex(new Vertex(3, "", -0.0002, -0.0002)).
                addVertex(new Vertex(4, "", -0.0001, -0.0001)).
                addVertex(new Vertex(5, "", 0.0006, -0.0006)).
                addVertex(new Vertex(6, "", 0.0004, 0.0004)).
                addVertex(new Vertex(7, "", -0.0004, -0.0004)).
                addVertex(new Vertex(8, "", 0.0004, -0.0003)).
                addVertex(new Vertex(9, "", -0.0003, -0.0003)).
                addVertex(new Vertex(10, "", 0.0005, 0.0005));

        graph.addEdge(1, 1, 85).
                addEdge(1, 2, 217).
                addEdge(1, 4, 173).
                addEdge(2, 6, 186).
                addEdge(2, 7, 103).
                addEdge(3, 7, 183).
                addEdge(5, 8, 250).
                addEdge(8, 9, 84).
                addEdge(7, 9, 167).
                addEdge(4, 9, 502).
                addEdge(9, 10, 40).
                addEdge(1, 10, 600);

        return graph;
    }
}