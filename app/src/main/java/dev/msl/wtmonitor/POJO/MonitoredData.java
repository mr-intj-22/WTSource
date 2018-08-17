package dev.msl.wtmonitor.POJO;

public class MonitoredData {
    private double Altitude = 0, Attack_Angle= 0, Temperature= 0,
            Wind_Speed= 0, Motor_Speed= 0, Humidity= 0, Target_Pitot_Speed= 0,
            Weight_1= 0, Weight_2= 0, Weight_3 = 0;

    public double getAltitude() {
        return Altitude;
    }

    public double getAttack_Angle() {
        return Attack_Angle;
    }

    public double getTemperature() {
        return Temperature;
    }

    public double getWind_Speed() {
        return Wind_Speed;
    }

    public double getMotor_Speed() {
        return Motor_Speed;
    }

    public double getHumidity() {
        return Humidity;
    }

    public double getTarget_Pitot_Speed() {
        return Target_Pitot_Speed;
    }

    public double getWeight_1() {
        return Weight_1;
    }

    public double getWeight_2() {
        return Weight_2;
    }

    public double getWeight_3() {
        return Weight_3;
    }

    public void setAltitude(double altitude) {
        Altitude = altitude;
    }

    public void setAttack_Angle(double attack_Angle) {
        Attack_Angle = attack_Angle;
    }

    public void setTemperature(double temperature) {
        Temperature = temperature;
    }

    public void setWind_Speed(double wind_Speed) {
        Wind_Speed = wind_Speed;
    }

    public void setMotor_Speed(double motor_Speed) {
        Motor_Speed = motor_Speed;
    }

    public void setHumidity(double humidity) {
        Humidity = humidity;
    }

    public void setTarget_Pitot_Speed(double target_Pitot_Speed) {
        Target_Pitot_Speed = target_Pitot_Speed;
    }

    public void setWeight_1(double weight_1) {
        Weight_1 = weight_1;
    }

    public void setWeight_2(double weight_2) {
        Weight_2 = weight_2;
    }

    public void setWeight_3(double weight_3) {
        Weight_3 = weight_3;
    }

    @Override
    public String toString() {
        return "ClassPojo [Altitude = " + Altitude + ", Attack_Angle = " + Attack_Angle + ", Weight_3 = " + Weight_3 + ", Temperature = " + Temperature + ", Wind_Speed = " + Wind_Speed + ", Motor_Speed = " + Motor_Speed + ", Humidity = " + Humidity + ", Target_Pitot_Speed = " + Target_Pitot_Speed + ", Weight_1 = " + Weight_1 + ", Weight_2 = " + Weight_2 + "]";
    }
}
