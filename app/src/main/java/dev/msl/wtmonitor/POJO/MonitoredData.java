package dev.msl.wtmonitor.POJO;

public class MonitoredData {
    private int Altitude, Attack_Angle, Temperature,
            Wind_Speed, Motor_Speed, Humidity, Target_Pitot_Speed,
            Weight_1, Weight_2, Weight_3;

    public int getAltitude() {
        return Altitude;
    }

    public void setAltitude(int Altitude) {
        this.Altitude = Altitude;
    }

    public int getAttack_Angle() {
        return Attack_Angle;
    }

    public void setAttack_Angle(int Attack_Angle) {
        this.Attack_Angle = Attack_Angle;
    }

    public int getTemperature() {
        return Temperature;
    }

    public void setTemperature(int Temperature) {
        this.Temperature = Temperature;
    }

    public int getWind_Speed() {
        return Wind_Speed;
    }

    public void setWind_Speed(int Wind_Speed) {
        this.Wind_Speed = Wind_Speed;
    }

    public int getMotor_Speed() {
        return Motor_Speed;
    }

    public void setMotor_Speed(int Motor_Speed) {
        this.Motor_Speed = Motor_Speed;
    }

    public int getHumidity() {
        return Humidity;
    }

    public void setHumidity(int Humidity) {
        this.Humidity = Humidity;
    }

    public int getTarget_Pitot_Speed() {
        return Target_Pitot_Speed;
    }

    public void setTarget_Pitot_Speed(int Target_Pitot_Speed) {
        this.Target_Pitot_Speed = Target_Pitot_Speed;
    }

    public int getWeight_1() {
        return Weight_1;
    }

    public void setWeight_1(int Weight_1) {
        this.Weight_1 = Weight_1;
    }

    public int getWeight_2() {
        return Weight_2;
    }

    public void setWeight_2(int Weight_2) {
        this.Weight_2 = Weight_2;
    }

    public int getWeight_3() {
        return Weight_3;
    }

    public void setWeight_3(int Weight_3) {
        this.Weight_3 = Weight_3;
    }

    @Override
    public String toString() {
        return "ClassPojo [Altitude = " + Altitude + ", Attack_Angle = " + Attack_Angle + ", Weight_3 = " + Weight_3 + ", Temperature = " + Temperature + ", Wind_Speed = " + Wind_Speed + ", Motor_Speed = " + Motor_Speed + ", Humidity = " + Humidity + ", Target_Pitot_Speed = " + Target_Pitot_Speed + ", Weight_1 = " + Weight_1 + ", Weight_2 = " + Weight_2 + "]";
    }
}
