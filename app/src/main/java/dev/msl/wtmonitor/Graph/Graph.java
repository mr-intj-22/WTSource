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
public class Graph extends Fragment {

    @BindView(R.id.graph)
    GraphView graphView;

    private LineGraphSeries<DataPoint> series;
    private double graph2LastXValue = 0d, start = System.currentTimeMillis();
    private String[] titles;
    private int pos = 0;
    private AlertDialog alert;


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

        titles = getResources().getStringArray(R.array.digital_titles);
        series = new LineGraphSeries<>();
        // styling series
        series.setTitle(titles[pos]);
        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setBackgroundColor(Color.WHITE);
        graphView.getLegendRenderer().setTextColor(ContextCompat.getColor(getActivity(), R.color.secondary_text));
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        series.setColor(ContextCompat.getColor(getActivity(), R.color.primary));
        series.setDrawDataPoints(true);
        series.setDrawBackground(true);
        series.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.accent_track));
        series.setDataPointsRadius(10);
        series.setThickness(8);
        graphView.addSeries(series);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(5);
//        handler.postDelayed(runnable, 1000);
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
            alt_bld.setSingleChoiceItems(R.array.digital_titles, pos, new DialogInterface
                    .OnClickListener() {

                public void onClick(DialogInterface dialog, int item) {
                    reset();
                    series.setTitle(titles[item]);
                    Graph.this.pos = item;
                    dialog.dismiss();// dismiss the alertbox after chose option
                }
            });
            alert = alt_bld.create();
        }
        alert.show();
    }

    public void update(MonitoredData data) {
        double d = 0d;
        switch (pos) {
            case 0:
                d = data.getMotor_Speed();
                break;
            case 1:
                d = data.getWind_Speed();
                break;
            case 2:
                d = data.getAttack_Angle();
                break;
            case 3:
                d = data.getTarget_Pitot_Speed();
                break;
            case 4:
                d = data.getTemperature();
                break;
            case 5:
                d = data.getHumidity();
                break;
            case 6:
                d = data.getAltitude();
                break;
            case 7:
                d = data.getWeight_1();
                break;
            case 8:
                d = data.getWeight_2();
                break;
            case 9:
                d = data.getWeight_3();
                break;
        }
        graph2LastXValue = (System.currentTimeMillis() - start) / 1000;
        Log.d("JSON", graph2LastXValue + "");
        series.appendData(new DataPoint(graph2LastXValue, d), true, 10);
    }

    public void reset() {
        series.resetData(new DataPoint[0]);
        start = System.currentTimeMillis();
        graph2LastXValue = (System.currentTimeMillis() - start) / 1000;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.graph_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.graph:
                dialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
