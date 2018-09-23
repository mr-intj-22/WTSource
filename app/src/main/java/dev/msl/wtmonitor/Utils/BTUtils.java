package dev.msl.wtmonitor.Utils;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

import dev.msl.wtmonitor.App;

public class BTUtils {

    private static boolean isBTSupported() {
        return App.getBluetoothAdapter() != null;
    }

    public static String getBTName() {
        if (isBTSupported())
            return App.getBluetoothAdapter().getName();
        return null;
    }

    public static boolean isBTEnabled() {
        return isBTSupported() && App.getBluetoothAdapter().isEnabled();
    }

    public static void enableBT() {
        App.getBluetoothAdapter().enable();
    }

    public static void disableBT() {
        App.getBluetoothAdapter().disable();
    }

    public static ArrayList<BluetoothDevice> getPairedDevices() {
        return new ArrayList<>(App.getBluetoothAdapter().getBondedDevices());
    }

}
