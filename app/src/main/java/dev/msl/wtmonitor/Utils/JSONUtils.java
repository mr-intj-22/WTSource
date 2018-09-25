package dev.msl.wtmonitor.Utils;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import dev.msl.wtmonitor.App;
import dev.msl.wtmonitor.POJO.MonitoredData;
import dev.msl.wtmonitor.POJO.Scenario;
import dev.msl.wtmonitor.POJO.SentData;

public class JSONUtils {

    public static MonitoredData fromJSON(String JSON) {
        try {
            return App.getGson().fromJson(JSON, MonitoredData.class);
        } catch (Exception e) {
            return new MonitoredData();
        }
    }

    public static ArrayList<Scenario> fromJSONArray(String JSON) {
        try {
            return App.getGson().fromJson(JSON, new TypeToken<List<Scenario>>() {
            }.getType());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static String toJson(SentData data) {
        return App.getGson().toJson(data) + "\r";
    }

}
