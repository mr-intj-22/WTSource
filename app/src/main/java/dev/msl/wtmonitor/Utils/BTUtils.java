package dev.msl.wtmonitor.Utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.lang.reflect.Array;
import java.util.ArrayList;

import dev.msl.wtmonitor.App;

public class BTUtils {

    public static boolean isBTSupported() {
        return App.getBluetoothAdapter() != null;
    }

    public static String getBTName() {
        if (isBTSupported())
            return App.getBluetoothAdapter().getName();
        return null;
    }

    public static boolean isBTEnabled() {
        return App.getBluetoothAdapter().isEnabled();
    }

    public static void enableBT(Activity activity, int requestCode) {
        Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBTIntent, requestCode);
    }

    public static ArrayList getPairedDevices(){
        return new ArrayList<>(App.getBluetoothAdapter().getBondedDevices());
    }

}
