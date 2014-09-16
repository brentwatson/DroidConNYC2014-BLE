package com.droidcon.nyc.ble.part2;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.List;


public class MainActivity extends Activity {

    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);

    private BeaconManager beaconManager = new BeaconManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                if(beacons != null){
                    for(Beacon beacon : beacons){
                        final Utils.Proximity proximity = Utils.computeProximity(beacon);
                        Log.d("BLEDEMO", "Proximity of " + beacon.getMajor() + " - " + beacon.getMinor() + " is: " + proximity);
                        //Will be one of: Utils.Proximity.IMMEDIATE, NEAR, FAR, or UNKNOWN

                        //double meters = Utils.computeAccuracy(beacon);//JavaDocs: Returns distance in meters based on beacon's RSSI and measured power.
                        //Log.d("BLEDEMO", "Proximity of " + beacon.getMajor() + " - " + beacon.getMinor() + " is: " + meters + " meters.");
                    }
                    Log.d("BLEDEMO", "-------");
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                } catch (RemoteException e) {
                    Log.e("BLEDEMO", "Cannot start ranging", e);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.e("BLEDEMO", "Cannot stop but it does not matter now", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        beaconManager.disconnect();
    }
}
