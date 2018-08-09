package dev.msl.wtmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.msl.wtmonitor.POJO.Scenario;
import dev.msl.wtmonitor.Scenarios.ScenariosAdapter;
import dev.msl.wtmonitor.Utils.Const;
import dev.msl.wtmonitor.Utils.ScenarioUtils;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static dev.msl.wtmonitor.Utils.Const.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    /* Side View */
    @BindView(R.id.sidenavview)
    NavigationView sideNavView;
    @BindView(R.id.add_scenario)
    FloatingActionButton addScenario;
    @BindView(R.id.rvscenarios)
    RecyclerView rvScenarios;
    @BindView(R.id.noscenarios)
    View noScenarios;
    @BindView(R.id.add_scenario_value)
    AppCompatEditText add_scenario_value;
    @BindView(R.id.add_scenario_duration)
    AppCompatEditText add_scenario_duration;
    @BindView(R.id.add_scenario_spinner)
    AppCompatSpinner add_scenario_spinner;
    @BindView(R.id.add_scenario_button)
    AppCompatButton add_scenario_button;
    @BindView(R.id.add_scenario_view)
    View add_scenario_view;
    private ScenariosAdapter scenariosAdapter;
    private int spinnerVal = SPEED;
    private ArrayList<Scenario> angles = new ArrayList<>();
    private ArrayList<Scenario> speeds = new ArrayList<>();
    private ArrayList<Scenario> scenarios = new ArrayList<>();

    /* main */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /* main */
        setSupportActionBar(toolbar);

        /* Side View */
        scenariosAdapter = new ScenariosAdapter(this, scenarios);
        rvScenarios.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));
        rvScenarios.setAdapter(scenariosAdapter);
        addScenario.setOnClickListener(this);
        add_scenario_button.setOnClickListener(this);
        // Spinner click listener
        add_scenario_spinner.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.scenarios_selector));
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        add_scenario_spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add_scenario:
                addScenario.hide();
                add_scenario_view.animate().alpha(1.0f);
                add_scenario_view.setVisibility(View.VISIBLE);
                break;
            case R.id.add_scenario_button:
                /* TEST */
                if (!isValidInput()) {
                    return;
                }
                if (noScenarios.getVisibility() == View.VISIBLE) noScenarios.animate().alpha(0.0f);
                Scenario scenario = new Scenario();
                if (spinnerVal == ANGLE) {
                    scenario.setMotor_angle(Integer.valueOf(add_scenario_value.getText().toString()));
                    if (angles.size() > 0) {
                        scenario.setStart_time(angles.get(angles.size() - 1).getTotal());
                    } else {
                        scenario.setStart_time(0);
                    }
                    angles.add(scenario);
                    scenarios.add(scenario);
                } else {
                    scenario.setMotor_speed(Integer.valueOf(add_scenario_value.getText().toString()));
                    if (speeds.size() > 0) {
                        scenario.setStart_time(speeds.get(speeds.size() - 1).getTotal());
                    } else {
                        scenario.setStart_time(0);
                    }
                    speeds.add(scenario);
                    scenarios.add(scenario);
                }
                scenario.setDuration(Integer.valueOf(add_scenario_duration.getText().toString()));
                Log.d("JSON", "start: " + scenario.getStart_time());
                Log.d("JSON", "for: " + scenario.getDuration());
                Log.d("JSON", "total: " + scenario.getTotal());
                ScenarioUtils.sortScenarios(scenarios);
                scenarios = ScenarioUtils.combineScenarios(scenarios);
                scenariosAdapter.insert(scenario);
//                rvScenarios.scrollToPosition(scenarios.indexOf(scenario));
                add_scenario_view.animate().alpha(0.0f);
                add_scenario_view.setVisibility(View.GONE);
                addScenario.show();
                break;
        }
    }

    private boolean isValidInput() {
        if (TextUtils.isEmpty(add_scenario_value.getText()) ||
                (spinnerVal == SPEED &&
                        Integer.valueOf(add_scenario_value.getText().toString()) < MIN_SPEED ||
                        Integer.valueOf(add_scenario_value.getText().toString()) > MAX_SPEED) ||
                (spinnerVal == ANGLE &&
                        Integer.valueOf(add_scenario_value.getText().toString()) < MIN_ANGLE ||
                        Integer.valueOf(add_scenario_value.getText().toString()) > MAX_ANGLE)) {
            add_scenario_value.setError(String.format(getResources().getString(R.string.validate_input),
                    spinnerVal == SPEED ? MIN_SPEED : MIN_ANGLE, spinnerVal == SPEED ? MAX_SPEED : MAX_ANGLE));
            return false;
        }
        if (TextUtils.isEmpty(add_scenario_duration.getText())) {
            add_scenario_duration.setError(String.format(getResources().getString(R.string.validate_input),
                    MIN_DURATION, MAX_DURATION));
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.ADD_SCENARIO:
                break;
            case Const.ENABLE_BT:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinnerVal = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
