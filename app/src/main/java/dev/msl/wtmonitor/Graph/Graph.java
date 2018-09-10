package dev.msl.wtmonitor.Graph;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.msl.wtmonitor.POJO.MonitoredData;
import dev.msl.wtmonitor.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Graph extends Fragment implements View.OnClickListener {

    @BindView(R.id.graph)
    GraphView graphView;
    @BindView(R.id.x_label)
    AppCompatButton xlable;
    @BindView(R.id.y_label)
    AppCompatButton ylable;

    private LineGraphSeries<DataPoint> series;
    private double graph2LastXValue = 0d, start = System.currentTimeMillis();
    private int xpos = 0, ypos = 1;
    private String[] labels, units;
    private AlertDialog alert;
    private int axis = 0;


    public Graph() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null)
            return;

        labels = getResources().getStringArray(R.array.digital_titles);
        units = getResources().getStringArray(R.array.digital_units);

        xlable.setOnClickListener(this);
        ylable.setOnClickListener(this);
        series = new LineGraphSeries<>();
        // styling series
        series.setTitle(String.format(getString(R.string.graph_title), labels[ypos], labels[xpos]));
        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setBackgroundColor(Color.WHITE);
        graphView.getLegendRenderer().setTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_text));
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphView.getLegendRenderer().setFixedPosition(0, 0);
        series.setColor(ContextCompat.getColor(getActivity(), R.color.primary));
        series.setDrawDataPoints(true);
        graphView.getGridLabelRenderer().setGridColor(Color.LTGRAY);
        series.setDrawBackground(true);
        series.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.accent_track));
        series.setDataPointsRadius(12);
        series.setThickness(8);
        graphView.addSeries(series);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(5);
        handler.postDelayed(runnable, 1000);
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            double i = new Random().nextDouble() * 9;
            graph2LastXValue = (System.currentTimeMillis() - start) / 1000;
            series.appendData(new DataPoint(graph2LastXValue, i), true, 10);
            handler.postDelayed(this, 1000);
        }
    };

    private void dialog() {
        if (alert == null) {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
            //alt_bld.setIcon(R.drawable.icon);
            alt_bld.setTitle(getString(R.string.plot_dialog_title));
            alt_bld.setSingleChoiceItems(R.array.digital_titles, axis == 0 ? xpos : ypos, new DialogInterface
                    .OnClickListener() {

                public void onClick(DialogInterface dialog, int item) {
                    if (axis == 0) {
                        Graph.this.xpos = item;
                        xlable.setText(String.format(getString(R.string.X), labels[xpos], units[xpos]));
                    } else {
                        Graph.this.ypos = item;
                        ylable.setText(String.format(getString(R.string.Y), labels[ypos], units[ypos]));
                    }
                    series.setTitle(String.format(getString(R.string.graph_title), labels[ypos], labels[xpos]));
                    dialog.dismiss();// dismiss the alertbox after chose option
                    reset();
                }
            });
            alert = alt_bld.create();
        }
        alert.show();
    }

    public void update(MonitoredData data) {
        double x = getData(data, xpos), y = getData(data, ypos);

        series.appendData(new DataPoint(x, y), true, 10);
    }

    private double getData(MonitoredData data, int pos) {
        switch (pos) {
            case 0:
                return data.getMotor_Speed();
            case 1:
                return data.getWind_Speed();
            case 2:
                return data.getAttack_Angle();
            case 3:
                return data.getTarget_Pitot_Speed();
            case 4:
                return data.getTemperature();
            case 5:
                return data.getHumidity();
            case 6:
                return data.getAltitude();
            case 7:
                return data.getWeight_1();
            case 8:
                return data.getWeight_2();
            case 9:
                return data.getWeight_3();
            default:
                return (System.currentTimeMillis() - start) / 1000;
        }
    }

    public void reset() {
        series.resetData(new DataPoint[0]);
        start = System.currentTimeMillis();
        graph2LastXValue = (System.currentTimeMillis() - start) / 1000;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.x_label:
                axis = 0;
                dialog();
                break;
            case R.id.y_label:
                axis = 1;
                dialog();
                break;
        }
    }
}
