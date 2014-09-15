package com.droidcon.nyc.ble.part4;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

public class MainActivity extends Activity implements BluetoothAdapter.LeScanCallback {

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init BluetoothAdapter
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        //Start scanning
        bluetoothAdapter.startLeScan(this); // All the things!
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.d("BLEDEMO", String.format("device=[{%s}].  rssi=[{%s}].  scanRecord=[{%s}].  name=[{%s}].", device, rssi, Arrays.toString(scanRecord), device.getName()));
        if("estimote".equalsIgnoreCase(device.getName())) {
            CrazyEstimoteUtils.decodeLeScan(device, rssi, scanRecord);
        }
        Log.d("BLEDEMO", "-----------");
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetoothAdapter.stopLeScan(this);
    }

}
