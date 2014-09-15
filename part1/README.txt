1.
Jar: https://github.com/Estimote/Android-SDK/blob/master/EstimoteSDK/estimote-sdk-preview.jar

2.
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>

3.
<service android:name="com.estimote.sdk.service.BeaconService" android:exported="false"/>

4. Activity:

  private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
  private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);

  private BeaconManager beaconManager = new BeaconManager(context);

  // Should be invoked in #onCreate.
  beaconManager.setRangingListener(new BeaconManager.RangingListener() {
    @Override public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
      Log.d(TAG, "Ranged beacons: " + beacons);
    }
  });

  // Should be invoked in #onStart.
  beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
    @Override public void onServiceReady() {
      try {
        beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
      } catch (RemoteException e) {
        Log.e(TAG, "Cannot start ranging", e);
      }
    }
  });

  // Should be invoked in #onStop.
  try {
    beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
  } catch (RemoteException e) {
    Log.e(TAG, "Cannot stop but it does not matter now", e);
  }

  // When no longer needed. Should be invoked in #onDestroy.
  beaconManager.disconnect();
