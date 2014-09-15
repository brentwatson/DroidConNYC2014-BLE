package com.droidcon.nyc.ble.part3;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.os.Bundle;
import android.util.Log;


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
        //UUID[] uuids = new UUID[]{UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),};
        //bluetoothAdapter.startLeScan(uuids, this);
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.d("BLEDEMO", String.format("device=[{%s}].  rssi=[{%s}].  scanRecord=[{%s}].  name=[{%s}].", device, rssi, scanRecord, device.getName()));
        if("estimote".equalsIgnoreCase(device.getName())) {
            Log.d("BLEDEMO", "Device UUID    " + device.getUuids());
            Log.d("BLEDEMO", "Device MAC ADD " + device.getAddress());
            Log.d("BLEDEMO", "Device MAJ VER " + device.getBluetoothClass().getMajorDeviceClass());
            Log.d("BLEDEMO", "Device MIN VER " + device.getBluetoothClass().getDeviceClass());
            Log.d("BLEDEMO", "Device NAME    " + device.getName());
        }
        Log.d("BLEDEMO", "-----------");
    }


    @Override
    protected void onStop() {
        super.onStop();
        bluetoothAdapter.stopLeScan(this);
    }

}
