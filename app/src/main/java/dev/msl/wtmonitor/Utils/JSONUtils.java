package dev.msl.wtmonitor.Utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import dev.msl.wtmonitor.App;
import dev.msl.wtmonitor.POJO.MonitoredData;
import dev.msl.wtmonitor.POJO.Scenario;
import dev.msl.wtmonitor.POJO.SentData;

public class JSONUtils {

    public static MonitoredData fromJSON(String JSON) {
        try {
            return App.getGson().fromJson(JSON, MonitoredData.class);
        } catch (Exception e) {
            Log.d("JSON", e.getMessage());
            return new MonitoredData();
        }
    }

    public static Scenario scenarioFromJSON(String JSON) {
        try {
            return App.getGson().fromJson(JSON, Scenario.class);
        } catch (Exception e) {
            Log.d("JSON", e.getMessage());
            return new Scenario();
        }
    }

    public static String toJson(SentData data) {
        return App.getGson().toJson(data) + "\r";
    }

}
