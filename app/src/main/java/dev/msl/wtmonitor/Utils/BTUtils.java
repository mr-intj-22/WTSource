package dev.msl.wtmonitor.Utils;

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

    public static void enableBT() {
        App.getBluetoothAdapter().enable();
    }

    public static void disableBT() {
        App.getBluetoothAdapter().disable();
    }

    public static ArrayList getPairedDevices() {
        return new ArrayList<>(App.getBluetoothAdapter().getBondedDevices());
    }

}
