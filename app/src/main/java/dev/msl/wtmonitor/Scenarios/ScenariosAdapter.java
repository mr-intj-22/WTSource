package dev.msl.wtmonitor.Scenarios;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.liefery.android.vertical_stepper_view.VerticalStepperAdapter;

import java.util.ArrayList;

import dev.msl.wtmonitor.POJO.Scenario;
import dev.msl.wtmonitor.R;

public class ScenariosAdapter extends VerticalStepperAdapter {

    private Context c;
    private ArrayList<Scenario> scenarios;
    private int position = 0;

    ScenariosAdapter(Context context) {
        super(context);
        this.c = context;
        scenarios = new ArrayList<>();
    }

    public void addStep(Scenario scenario) {
        scenarios.add(scenario);
        notifyDataSetChanged();
        if (position > 0)
            reset();
    }

    public void addStep(ArrayList<Scenario> scenarios) {
        if (position > 0)
            reset();
        this.scenarios = scenarios;
        notifyDataSetChanged();
    }

    public void clear() {
        scenarios.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CharSequence getTitle(int position) {
        return String.format(c.getResources().getString(R.string.time), scenarios.get(position).getDuration());
    }

    @Nullable
    @Override
    public CharSequence getSummary(int position) {
        return String.format(c.getResources().getString(R.string.step_subtitle),
                scenarios.get(position).getMotor_speed(), scenarios.get(position).getMotor_angle());
    }

    @Override
    public boolean isEditable(int position) {
        return false;
    }

    @Override
    public int getCount() {
        return scenarios != null ? scenarios.size() : 0;
    }

    @Override
    public Void getItem(int position) {
        return null;
    }

    @NonNull
    @Override
    public View onCreateContentView(Context context, int position) {
        View content = new MainItemView(context);

        AppCompatTextView speed = content.findViewById(R.id.text_timeline_speed);
        AppCompatTextView angle = content.findViewById(R.id.text_timeline_angle);
        speed.setText(String.format(c.getResources().getString(R.string.motor_speed_value),
                scenarios.get(position).getMotor_speed()));
        angle.setText(String.format(c.getResources().getString(R.string.motor_angle_value),
                scenarios.get(position).getMotor_angle()));

        return content;
    }

    @Override
    public void next() {
        super.next();
        position++;
    }

    @Override
    public void previous() {
        super.previous();
        position--;
    }

    public void reset() {
        jumpTo(0);
        position = 0;
    }

    @Override
    public void jumpTo(int position) {
        super.jumpTo(position);
    }

    public int getPosition() {
        return position;
    }

    public Scenario getScenario() {
        return scenarios.get(position);
    }

}