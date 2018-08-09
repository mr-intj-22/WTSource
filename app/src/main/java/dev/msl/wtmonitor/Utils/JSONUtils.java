package dev.msl.wtmonitor.Utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import dev.msl.wtmonitor.App;
import dev.msl.wtmonitor.POJO.MonitoredData;
import dev.msl.wtmonitor.POJO.SentData;

public class JSONUtils {

    public static MonitoredData fromJSON(String JSON) {
        return App.getGson().fromJson(JSON, MonitoredData.class);
    }

    public static String toJson(MonitoredData data) {
        return App.getGson().toJson(data) + "\r";
    }

    public static String getSampleJSON(Context c) {
        try {

            StringBuilder buf = new StringBuilder();
            InputStream json = c.getAssets().open("sample");
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }

            in.close();

            return buf.toString();

        } catch (Exception e) {
            Log.d("JSON", e.getMessage());
        }
        return "";
    }

}
