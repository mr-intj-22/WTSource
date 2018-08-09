package dev.msl.wtmonitor;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.msl.wtmonitor.POJO.Scenario;
import dev.msl.wtmonitor.Scenarios.ScenariosAdapter;
import dev.msl.wtmonitor.Utils.BTUtils;
import dev.msl.wtmonitor.Utils.Const;
import dev.msl.wtmonitor.Utils.ScenarioUtils;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static dev.msl.wtmonitor.Utils.Const.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {


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
    SwitchCompat btSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

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
                ScenarioUtils.sortScenarios(scenarios);
                scenarios = ScenarioUtils.combineScenarios(scenarios);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.bt_switch_item);
        ViewGroup viewGroup = (ViewGroup) item.getActionView();
        btSwitch = viewGroup.findViewById(R.id.bt_switch);
        btSwitch.setChecked(BTUtils.isBTEnabled());
        btSwitch.setOnCheckedChangeListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connect_bt:
                if (!BTUtils.isBTEnabled()) BTUtils.enableBT();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            BTUtils.enableBT();
        } else {
            BTUtils.disableBT();
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action != null && action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        // Bluetooth has been turned off;
                        btSwitch.setChecked(false);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        // Bluetooth is turning off;
                        break;
                    case BluetoothAdapter.STATE_ON:
                        // Bluetooth has been turned on
                        btSwitch.setChecked(true);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        // Bluetooth is turning on
                        break;
                }
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        // Unregister broadcast listeners
        unregisterReceiver(mReceiver);
    }
}
