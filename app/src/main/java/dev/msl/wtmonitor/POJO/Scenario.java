package dev.msl.wtmonitor.POJO;

public class Scenario {

    private int duration = 0, motor_angle = 0, motor_speed = 0;

    public int getMotor_angle() {
        return motor_angle;
    }

    public Scenario setMotor_angle(int motor_angle) {
        this.motor_angle = motor_angle;
        return this;
    }

    public int getMotor_speed() {
        return motor_speed;
    }

    public Scenario setMotor_speed(int motor_speed) {
        this.motor_speed = motor_speed;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Scenario setDuration(int duration) {
        this.duration = duration;
        return this;
    }
}
