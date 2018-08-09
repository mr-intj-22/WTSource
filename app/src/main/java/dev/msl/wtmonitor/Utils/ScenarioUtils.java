package dev.msl.wtmonitor.Utils;

import java.util.ArrayList;
import java.util.Collections;

import dev.msl.wtmonitor.POJO.Scenario;

public class ScenarioUtils {

    public static void sortScenarios(ArrayList<Scenario> scenarios) {
        Collections.sort(scenarios, Scenario.ASCENDING_COMPARATOR);
    }

    public static ArrayList<Scenario> combineScenarios(ArrayList<Scenario> scenarios) {
        ArrayList<Scenario> scenarioArrayList = new ArrayList<>();
        Collections.copy(scenarios, scenarioArrayList);
        int i = 1;
        while (i < scenarioArrayList.size()) {
            Scenario obj1 = scenarioArrayList.get(i - 1), obj2 = scenarioArrayList.get(i);
            if (obj1.getStart_time() == obj2.getStart_time()) {
                if (obj1.getMotor_speed() < 0 && obj2.getMotor_speed() >= 0) {
                    obj1.setMotor_speed(obj2.getMotor_speed());
                } else if (obj1.getMotor_angle() < 0 && obj2.getMotor_angle() >= 0) {
                    obj1.setMotor_angle(obj2.getMotor_angle());
                }
                scenarioArrayList.remove(i);
            } else {
                i++;
            }
        }
        sortScenarios(scenarioArrayList);
        return scenarioArrayList;
    }

}
