package dev.msl.wtmonitor.Utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

import dev.msl.wtmonitor.App;
import dev.msl.wtmonitor.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

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
//        return App.getBluetoothAdapter().isEnabled();
        return true;
    }

    public static void enableBT() {
//        App.getBluetoothAdapter().enable();
    }

    public static void disableBT() {
//        App.getBluetoothAdapter().disable();
    }

    public static ArrayList<BluetoothDevice> getPairedDevices() {
        return new ArrayList<>(App.getBluetoothAdapter().getBondedDevices());
    }

}
