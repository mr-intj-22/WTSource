package dev.msl.wtmonitor.POJO;

import java.util.Comparator;

public class Scenario {

    private int start_time = 0, duration = 0, motor_angle = -1, motor_speed = -1;

    public int getMotor_angle() {
        return motor_angle;
    }

    public void setMotor_angle(int motor_angle) {
        this.motor_angle = motor_angle;
    }

    public int getMotor_speed() {
        return motor_speed;
    }

    public void setMotor_speed(int motor_speed) {
        this.motor_speed = motor_speed;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotal() {
        return start_time + duration;
    }

    public static final Comparator<Scenario> ASCENDING_COMPARATOR = new Comparator<Scenario>() {
        // Overriding the compare method to sort the age
        public int compare(Scenario scenario1, Scenario scenario2) {
            return scenario1.getStart_time() - scenario2.getStart_time();
        }
    };
}
