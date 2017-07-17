package com.epam.beacons2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.epam.beacons2.util.BleUtil;
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
import java.util.Collections;
import java.util.Comparator;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, BluetoothAdapter.LeScanCallback {

    private TextView proximity;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private GroundOverlayOptions groundOverlayOptions;
    private GroundOverlayOptions groundOverlayOptions2;
    private LatLng epamLocReal = new LatLng(59.9851017, 30.3097383);
    private LatLng epamLoc0 = new LatLng(0, 0);
    private LatLng epamLocSouthWest = new LatLng(-0.0001, -0.0001);
    private LatLng epamLocNorthEast = new LatLng(0.0001, 0.0001);
    private LatLng epamLocTest1 = new LatLng(0.0002, 0.0002);

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

        init();

        if ((mBTAdapter != null) && (!mBTAdapter.isEnabled())) {
            Toast.makeText(this, R.string.bt_not_enabled, Toast.LENGTH_SHORT).show();
        } else startScan();
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
        LatLngBounds epamCameraBounds = new LatLngBounds(epamLocSouthWest, epamLocNorthEast);

        mMap.addMarker(new MarkerOptions().position(epamLoc0).title("Marker in Current Location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(epamBounds.getCenter(), 0));
        mMap.setLatLngBoundsForCameraTarget(epamCameraBounds);

        mMap.setMyLocationEnabled(true);

        final GroundOverlay imageOverlay = mMap.addGroundOverlay(groundOverlayOptions);
        imageOverlay.setClickable(true);

        mMap.setMinZoomPreference(20.5f);
        mMap.setMaxZoomPreference(30.0f);

        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        epamLocNorthEast,
                        epamLocSouthWest,
                        new LatLng(-0.0001, 0.0001)
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

    @Override
    protected void onResume() {
        super.onResume();

        if ((mBTAdapter != null) && (!mBTAdapter.isEnabled())) {
            Toast.makeText(this, R.string.bt_not_enabled, Toast.LENGTH_SHORT).show();
        } else startScan();
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopScan();
    }

    private void init() {
        // BLE check
        if (!BleUtil.isBLESupported(this)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        BluetoothManager manager = BleUtil.getManager(this);

        if (manager != null) {
            mBTAdapter = manager.getAdapter();

        }
        if (mBTAdapter == null) {
            Toast.makeText(this, R.string.bt_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        bluetoothLeScanner = mBTAdapter.getBluetoothLeScanner();
        stopScan();
    }

    private void startScan() {
        if ((mBTAdapter != null) && (!mIsScanning)) {
//            mBTAdapter.startLeScan(this);
            bluetoothLeScanner.startScan(new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                            update(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes());
                            showProximity();

                            LatLng newLatLng = getLocationByTrilateration(epamLocTest1, 10,
                                    epamLocNorthEast, 20,
                                    epamLocSouthWest, 40);

                            Log.d(MockActivity.class.getSimpleName(), "scannedDevices" + scannedDevices.size());
                            mMap.addMarker(new MarkerOptions().position(newLatLng).title("Marker in Second Location"));

            }
            });
            mIsScanning = true;
        }
    }

    private void stopScan() {
        if (mBTAdapter != null) {
            bluetoothLeScanner.stopScan(new ScanCallback(){
            });
//            mBTAdapter.stopLeScan(this);
        }
        mIsScanning = false;
    }

    @Override
    public void onLeScan(final BluetoothDevice newDevice, final int newRssi,
                         final byte[] newScanRecord) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                update(newDevice, newRssi, newScanRecord);
                showProximity();

                LatLng newLatLng = getLocationByTrilateration(epamLocTest1, 10,
                        epamLocNorthEast, 20,
                        epamLocSouthWest, 40);

                mMap.addMarker(new MarkerOptions().position(newLatLng).title("Marker in Second Location"));

            }
        }, 100);
    }

    public void update(BluetoothDevice newDevice, int rssi, byte[] scanRecord) {
        if ((newDevice == null) || (newDevice.getAddress() == null)) {
            return;
        }

        boolean contains = false;
        for (ScannedDevice device : scannedDevices) {
            if (newDevice.getAddress().equals(device.getDevice().getAddress())) {

                contains = true;
                // update
                device.setRssi(rssi);
                device.setScanRecord(scanRecord);
                break;
            }
        }
        if (!contains) {
            // add new BluetoothDevice
            scannedDevices.add(new ScannedDevice(newDevice, rssi, scanRecord));
            Log.d(MockActivity.class.getSimpleName(), "hellohello");
        }

        // sort by distance
        Collections.sort(scannedDevices, new Comparator<ScannedDevice>() {
            @Override
            public int compare(ScannedDevice lhs, ScannedDevice rhs) {
                if (lhs.getRssi() == 0) {
                    return 1;
                } else if (rhs.getRssi() == 0) {
                    return -1;
                }
                if (lhs.getRssi() > rhs.getRssi()) {
                    return -1;
                } else if (lhs.getRssi() < rhs.getRssi()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    public void showProximity() {
        final StringBuilder builder = new StringBuilder();
        if (scannedDevices.size() > 0) {
            for (ScannedDevice sc : scannedDevices) {
                if (sc.getIBeacon() != null) {
                    builder.append(PROXIMITY).append(sc.getIBeacon().getProximity())
                            .append("\n").append(ACCURACY).append(sc.getIBeacon().getAccuracy()).append("\n");
                    proximity.setText(builder.toString());
                }
            }
        }
    }

    public LatLng getLocationByTrilateration(
            LatLng location1, double distance1,
            LatLng location2, double distance2,
            LatLng location3, double distance3) {

        //DECLARE VARIABLES
        LatLng newLocation1 = new LatLng(location1.latitude * 100000, location1.longitude * 100000);
        LatLng newLocation2 = new LatLng(location2.latitude * 100000, location2.longitude * 100000);
        LatLng newLocation3 = new LatLng(location3.latitude * 100000, location3.longitude * 100000);

        double[] P1 = new double[2];
        double[] P2 = new double[2];
        double[] P3 = new double[2];
        double[] ex = new double[2];
        double[] ey = new double[2];
        double[] p3p1 = new double[2];
        double jval = 0;
        double temp = 0;
        double ival = 0;
        double p3p1i = 0;
        double triptx;
        double tripty;
        double xval;
        double yval;
        double t1;
        double t2;
        double t3;
        double t;
        double exx;
        double d;
        double eyy;

        //TRANSALTE POINTS TO VECTORS
        //POINT 1
        P1[0] = newLocation1.latitude;
        P1[1] = newLocation1.longitude;
        //POINT 2
        P2[0] = newLocation2.latitude;
        P2[1] = newLocation2.longitude;
        //POINT 3
        P3[0] = newLocation3.latitude;
        P3[1] = newLocation3.longitude;

        //TRANSFORM THE METERS VALUE FOR THE MAP UNIT
        //DISTANCE BETWEEN POINT 1 AND MY LOCATION
//        distance1 = (distance1 / 100000);
        //DISTANCE BETWEEN POINT 2 AND MY LOCATION
//        distance2 = (distance2 / 100000);
        //DISTANCE BETWEEN POINT 3 AND MY LOCATION
//        distance3 = (distance3 / 100000);

        for (int i = 0; i < P1.length; i++) {
            t1 = P2[i];
            t2 = P1[i];
            t = t1 - t2;
            temp += (t * t);
        }
        d = Math.sqrt(temp);
        for (int i = 0; i < P1.length; i++) {
            t1 = P2[i];
            t2 = P1[i];
            exx = (t1 - t2) / (Math.sqrt(temp));
            ex[i] = exx;
        }
        for (int i = 0; i < P3.length; i++) {
            t1 = P3[i];
            t2 = P1[i];
            t3 = t1 - t2;
            p3p1[i] = t3;
        }
        for (int i = 0; i < ex.length; i++) {
            t1 = ex[i];
            t2 = p3p1[i];
            ival += (t1 * t2);
        }
        for (int i = 0; i < P3.length; i++) {
            t1 = P3[i];
            t2 = P1[i];
            t3 = ex[i] * ival;
            t = t1 - t2 - t3;
            p3p1i += (t * t);
        }
        for (int i = 0; i < P3.length; i++) {
            t1 = P3[i];
            t2 = P1[i];
            t3 = ex[i] * ival;
            eyy = (t1 - t2 - t3) / Math.sqrt(p3p1i);
            ey[i] = eyy;
        }
        for (int i = 0; i < ey.length; i++) {
            t1 = ey[i];
            t2 = p3p1[i];
            jval += (t1 * t2);
        }
        xval = (Math.pow(distance1, 2) - Math.pow(distance2, 2) + Math.pow(d, 2)) / (2 * d);
        yval = ((Math.pow(distance1, 2) - Math.pow(distance3, 2) + Math.pow(ival, 2) + Math.pow(jval, 2)) / (2 * jval)) - ((ival / jval) * xval);

        t1 = newLocation1.latitude;
        t2 = ex[0] * xval;
        t3 = ey[0] * yval;
        triptx = t1 + t2 + t3;

        t1 = newLocation1.longitude;
        t2 = ex[1] * xval;
        t3 = ey[1] * yval;
        tripty = t1 + t2 + t3;
        return new LatLng(triptx / 100000, tripty / 100000);
    }

//    public double[] getLocationByTrilateration2(
//            LatLng location1, double distance1,
//            LatLng location2, double distance2,
//            LatLng location3, double distance3) {
//
//        double x0 = cos(location1.longitude) * cos(location1.latitude);
//        double y0 = sin(location1.longitude) * cos(location1.latitude);
//        double z0 = sin(location1.latitude);
//        double x1 = cos(location2.longitude) * cos(location2.latitude);
//        double y1 = sin(location2.longitude) * cos(location2.latitude);
//        double z1 = sin(location2.latitude);
//        double x2 = cos(location3.longitude) * cos(location3.latitude);
//        double y2 = sin(location3.longitude) * cos(location3.latitude);
//        double z2 = sin(location3.latitude);
//
//        double[][] positions = new double[][] { { x0, y0 }, { x1, y1 }, { x2, y2 } };
//        double[] distances = new double[] { distance1, distance2, distance3 };
//
//        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
//        LeastSquaresOptimizer.Optimum optimum = solver.solve();
//
//        double[] calculatedPosition = optimum.getPoint().toArray();
//        return  calculatedPosition;
//    }
}
