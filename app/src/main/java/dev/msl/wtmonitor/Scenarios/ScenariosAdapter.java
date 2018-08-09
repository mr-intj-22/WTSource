package dev.msl.wtmonitor.Scenarios;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

import dev.msl.wtmonitor.POJO.Scenario;
import dev.msl.wtmonitor.R;
import dev.msl.wtmonitor.Utils.ScenarioUtils;

public class ScenariosAdapter extends RecyclerView.Adapter<ScenariosAdapter.ScenariosViewHolder> {

    private ArrayList<Scenario> scenarios;
    private Context c;
    private LayoutInflater mLayoutInflater;
    private int oldPos = 0;

    public ScenariosAdapter(Context c, ArrayList<Scenario> scenarios) {
        this.c = c;
        mLayoutInflater = LayoutInflater.from(c);
        this.scenarios = scenarios;
        notifyItemChanged(0, scenarios.size());
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @NonNull
    @Override
    public ScenariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScenariosViewHolder(mLayoutInflater.inflate(R.layout.scenario_item, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ScenariosViewHolder holder, int position) {

        if (scenarios.get(position).getMotor_speed() != -1) {
            holder.getSpeed().setVisibility(View.VISIBLE);
            holder.getSpeed().setText(String.format(c.getResources().getString(R.string.motor_speed_value),
                    scenarios.get(position).getMotor_speed()));
        }

        if (scenarios.get(position).getMotor_angle() != -1) {
            holder.getAngle().setVisibility(View.VISIBLE);
            holder.getAngle().setText(String.format(c.getResources().getString(R.string.motor_angle_value),
                    scenarios.get(position).getMotor_angle()));
        }

        holder.getTime().setText(String.format(c.getResources().getString(R.string.time), scenarios.get(position).getStart_time()));

        if (position == oldPos) {
            holder.getTimelineView().setMarker(ActivityCompat.getDrawable(c, R.drawable.ic_marker_active));
        }
    }

    @Override
    public int getItemCount() {
        return scenarios != null ? scenarios.size() : 0;
    }

    public class ScenariosViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView angle, speed, time;
        private TimelineView timelineView;

        public ScenariosViewHolder(View itemView, int viewType) {
            super(itemView);
            angle = itemView.findViewById(R.id.text_timeline_angle);
            speed = itemView.findViewById(R.id.text_timeline_speed);
            time = itemView.findViewById(R.id.text_timeline_time);
            timelineView = itemView.findViewById(R.id.time_marker);
            timelineView.initLine(viewType);
        }

        public AppCompatTextView getAngle() {
            return angle;
        }

        public AppCompatTextView getSpeed() {
            return speed;
        }

        public AppCompatTextView getTime() {
            return time;
        }

        public TimelineView getTimelineView() {
            return timelineView;
        }
    }

}
