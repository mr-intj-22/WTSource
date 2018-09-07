package dev.msl.wtmonitor.Digital;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.msl.wtmonitor.POJO.MonitoredData;
import dev.msl.wtmonitor.R;

public class DigitalMonitor extends Fragment {

    @BindView(R.id.motor_speed)
    AppCompatTextView motor_speed;
    @BindView(R.id.wind_speed)
    AppCompatTextView wind_speed;
    @BindView(R.id.pitot_speed)
    AppCompatTextView pito_speed;
    @BindView(R.id.attack_angle)
    AppCompatTextView attack_angle;
    @BindView(R.id.temperature)
    AppCompatTextView temp;
    @BindView(R.id.altitude)
    AppCompatTextView alt;
    @BindView(R.id.humidity)
    AppCompatTextView humidity;
    @BindView(R.id.weight_1)
    AppCompatTextView weight1;
    @BindView(R.id.weight_2)
    AppCompatTextView weight2;
    @BindView(R.id.weight_3)
    AppCompatTextView weight3;

    DecimalFormat df = new DecimalFormat("####0.00");

    public DigitalMonitor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_digital_monitor, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateValues(new MonitoredData());
//        //Start
//        handler.postDelayed(runnable, 500);
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            double i = new Random().nextDouble()*9;
            MonitoredData monitoredData = new MonitoredData();
            monitoredData.setAltitude(i);
            monitoredData.setAttack_Angle(i);
            monitoredData.setWeight_1(i);
            updateValues(monitoredData);
            handler.postDelayed(this, 500);
        }
    };

    @SuppressLint("SetTextI18n")
    public void updateValues(MonitoredData data) {
        motor_speed.setText(spanText(data.getMotor_Speed(), getString(R.string.unit_speed),0.7f));
        wind_speed.setText(spanText(data.getWind_Speed(), getString(R.string.unit_speed),0.7f));
        pito_speed.setText(spanText(data.getTarget_Pitot_Speed(), getString(R.string.unit_speed),0.7f));
        attack_angle.setText(spanText(data.getAttack_Angle(), getString(R.string.unit_angle),1));
        weight1.setText(spanText(data.getWeight_1(), getString(R.string.unit_weight)+"  ",0.7f));
        weight2.setText(spanText(data.getWeight_2(), getString(R.string.unit_weight),0.7f));
        weight3.setText(spanText(data.getWeight_3(), getString(R.string.unit_weight),0.7f));
        alt.setText(spanText(data.getAltitude(), getString(R.string.unit_altitude),0.7f));
        humidity.setText(spanText(data.getHumidity(), getString(R.string.unit_humidity),0.7f));
        temp.setText(spanText(data.getTemperature(), getString(R.string.unit_temperature),0.7f));
    }

    private SpannableStringBuilder spanText(double val, String unit, float f) {
        String text = df.format(val) + " " + unit;
        RelativeSizeSpan smallSizeText = new RelativeSizeSpan(f);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);
        ssBuilder.setSpan(
                smallSizeText,
                text.indexOf(" "),
                text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return ssBuilder;
    }
}