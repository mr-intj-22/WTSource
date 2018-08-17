package dev.msl.wtmonitor;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.google.gson.Gson;

public class App extends Application {

    private static Gson gson;
    private static BluetoothAdapter bluetoothAdapter;

    public static synchronized Gson getGson() {
        if (gson == null) gson = new Gson();
        return gson;
    }

    public static synchronized BluetoothAdapter getBluetoothAdapter() {
        if (bluetoothAdapter == null) bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter;
    }
}
