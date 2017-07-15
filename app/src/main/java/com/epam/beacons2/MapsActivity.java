package com.epam.beacons2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, BluetoothAdapter.LeScanCallback{

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private GroundOverlayOptions groundOverlayOptions;
    private GroundOverlayOptions groundOverlayOptions2;
    private LatLng epamLoc = new LatLng(59.9851017, 30.3097383);
    private LatLng epamLoc0 = new LatLng(0, 0);
    private double latitude = 0.0;
    private double longitude = 0.0;

    private BluetoothAdapter mBTAdapter;
    private boolean mIsScanning;
    private ArrayList <ScannedDevice> scannedDevices = new ArrayList<>();

    @Override
//    @SuppressWarnings("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        mFusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            Log.d(MapsActivity.class.getSimpleName(), "herehere"  + latitude);
//                            longitude = location.getLongitude();
//                            Log.d(MapsActivity.class.getSimpleName(), "herehere " + longitude);
//                        }
//                    }
//                });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        LatLng epamLoc = new LatLng(59.9851017, 30.3097383);

        groundOverlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.map))
                .position(epamLoc, 110f, 80f);

        groundOverlayOptions2 = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.map2))
                .position(epamLoc, 110f, 80f);

        init();
        startScan();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    @SuppressWarnings("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setBuildingsEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setMapType(0);

        // Add a marker in Sydney and move the camera
        LatLng currentLoc = new LatLng(latitude, longitude);

        // LatLng southwest, LatLng northeast
       LatLngBounds epam = new LatLngBounds(epamLoc, epamLoc);

        mMap.addMarker(new MarkerOptions().position(epamLoc).title("Marker in Current Location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(epam.getCenter(), 0));
        mMap.setLatLngBoundsForCameraTarget(epam);

        mMap.setMyLocationEnabled(true);

        final GroundOverlay imageOverlay = mMap.addGroundOverlay(groundOverlayOptions);
        imageOverlay.setClickable(true);

        mMap.setMinZoomPreference(19.0f);
        mMap.setMaxZoomPreference(30.0f);

        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(59.9851017, 30.3097383),
                        new LatLng(59.9851217, 30.3097483),
                        new LatLng(59.9851217, 30.3096483),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(59.9851017, 30.3096383),
                        new LatLng(60, 30.3096383)
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
//            invalidateOptionsMenu();
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
        stopScan();
    }

    private void startScan() {
        if ((mBTAdapter != null) && (!mIsScanning)) {
            mBTAdapter.startLeScan(this);
            mIsScanning = true;
        }
    }

    private void stopScan() {
        if (mBTAdapter != null) {
            mBTAdapter.stopLeScan(this);
        }
        mIsScanning = false;
    }

    @Override
    public void onLeScan(final BluetoothDevice newDeivce, final int newRssi,
                         final byte[] newScanRecord) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                update(newDeivce, newRssi, newScanRecord);
                showProximity();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
        }

        // sort by RSSI
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
        if (scannedDevices.size()>0){
            for (ScannedDevice sc : scannedDevices){
                if (sc.getIBeacon() != null) {
                    Log.d(MapsActivity.class.getSimpleName(), "iBeacon, proximity: " + sc.getIBeacon().getProximity());
                }
            }
        }
    }
}
