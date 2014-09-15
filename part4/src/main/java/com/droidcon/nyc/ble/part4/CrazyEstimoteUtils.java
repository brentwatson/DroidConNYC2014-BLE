package com.droidcon.nyc.ble.part4;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.droidcon.nyc.ble.part4.utils.HashCode;

/**
 * Copyright (c) 2014 Percolate Industries Inc. All rights reserved.
 * Project:
 *
 * @author brent
 */
public class CrazyEstimoteUtils {

    public static void decodeLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {

        final String scanRecordAsHex = HashCode.fromBytes(scanRecord).toString();
        int i = 0;
        while (i < scanRecord.length) {
            final int payloadLength = unsignedByteToInt(scanRecord[i]);
            if (payloadLength == 0) {
                break;
            }
            if (i + 1 >= scanRecord.length) {
                break;
            }
            if (unsignedByteToInt(scanRecord[i + 1]) != 255) {
                i += payloadLength;
                ++i;
            }
            else {
                if (payloadLength != 26) {
                    return;
                }
                if (unsignedByteToInt(scanRecord[i + 2]) == 76 && unsignedByteToInt(scanRecord[i + 3]) == 0 && unsignedByteToInt(scanRecord[i + 4]) == 2 && unsignedByteToInt(scanRecord[i + 5]) == 21) {
                    final String proximityUUID = String.format("%s-%s-%s-%s-%s", scanRecordAsHex.substring(18, 26), scanRecordAsHex.substring(26, 30), scanRecordAsHex.substring(30, 34), scanRecordAsHex.substring(34, 38), scanRecordAsHex.substring(38, 50));
                    final int major = unsignedByteToInt(scanRecord[i + 22]) * 256 + unsignedByteToInt(scanRecord[i + 23]);
                    final int minor = unsignedByteToInt(scanRecord[i + 24]) * 256 + unsignedByteToInt(scanRecord[i + 25]);
                    final int measuredPower = scanRecord[i + 26];

                    Log.d("BLEDEMO", "Estimote proximityUUID: " + proximityUUID);
                    Log.d("BLEDEMO", "Estimote major: " + major);
                    Log.d("BLEDEMO", "Estimote minor: " +  minor);
                    Log.d("BLEDEMO", "Estimote measuredPower: " + measuredPower);

                    return;
                }
            }
        }
    }

    private static int unsignedByteToInt(final byte value) {
        return value & 0xFF;
    }

}
