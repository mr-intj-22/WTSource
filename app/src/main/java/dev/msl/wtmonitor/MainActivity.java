package dev.msl.wtmonitor;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.msl.wtmonitor.Bluetooth.BluetoothDeviceManager;
import dev.msl.wtmonitor.Bluetooth.BluetoothService;
import dev.msl.wtmonitor.Digital.DigitalMonitor;
import dev.msl.wtmonitor.Graph.Graph;
import dev.msl.wtmonitor.POJO.SentData;
import dev.msl.wtmonitor.Scenarios.ScenarioFragment;
import dev.msl.wtmonitor.Utils.BTUtils;
import dev.msl.wtmonitor.Utils.Const;
import dev.msl.wtmonitor.Utils.JSONUtils;

import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static dev.msl.wtmonitor.Utils.BTUtils.disableBT;
import static dev.msl.wtmonitor.Utils.BTUtils.enableBT;
import static dev.msl.wtmonitor.Utils.BTUtils.isBTEnabled;
import static dev.msl.wtmonitor.Utils.ScreenUtils.getPixelsFromDPs;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        SeekBar.OnSeekBarChangeListener, BluetoothDeviceManager.BluetoothDevicePickResultHandler {

    /* main */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tabs_pager)
    ViewPager pager;
    @BindView(R.id.sidenavview)
    NavigationView sideNavView;
    @BindView(R.id.side_menu_nav)
    NavigationView sideMenuNav;
    @BindView(R.id.angle_seek)
    AppCompatSeekBar angleSeek;
    @BindView(R.id.speed_seek)
    AppCompatSeekBar speedSeek;
    @BindView(R.id.angle_val)
    TextView angleVal;
    @BindView(R.id.speed_val)
    TextView speedVal;

    private SwitchCompat btSwitch;
    private SwitchCompat scenarioSwitch;
    private TextView btStatus, btName;
    private ImageView btStateColor;
    private ProgressBar btStateProgress;

    private DigitalMonitor digitalMonitor = null;
    private Graph graph = null;

    private StringBuffer mOutStringBuffer;

    /*fragments*/
//    private ScenarioFragment sideFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /* main */
        setSupportActionBar(toolbar);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

        /* Side Menu */
        Menu nav_menu = sideMenuNav.getMenu();
        scenarioSwitch = nav_menu.findItem(R.id.scenario_status).getActionView().findViewById(R.id.scenario_switch);
        scenarioSwitch.setChecked(false);
        scenarioSwitch.setOnCheckedChangeListener(this);
        ViewGroup.LayoutParams layoutParams = sideNavView.getLayoutParams();
        layoutParams.width = 0;
        sideNavView.setLayoutParams(layoutParams);
        btStatus = nav_menu.findItem(R.id.connection_status).getActionView().findViewById(R.id.bt_connection_status);
        btName = nav_menu.findItem(R.id.connection_status).getActionView().findViewById(R.id.bt_connected_name);
        btStateColor = nav_menu.findItem(R.id.connection_status).getActionView().findViewById(R.id.bt_img_connection_status);
        btStateProgress = nav_menu.findItem(R.id.connection_status).getActionView().findViewById(R.id.bt_progress_connection_status);
        angleSeek.setOnSeekBarChangeListener(this);
        speedSeek.setOnSeekBarChangeListener(this);
        angleSeek.setMax(getResources().getInteger(R.integer.max_motor_angle));
        angleSeek.setProgress(getResources().getInteger(R.integer.default_motor_angle));
        angleVal.setText(String.valueOf(angleSeek.getProgress() - getResources().getInteger(R.integer.angle_value_margin)));
        speedSeek.setMax(getResources().getInteger(R.integer.max_motor_speed));
        speedSeek.setProgress(getResources().getInteger(R.integer.default_motor_speed));
        speedVal.setText(String.valueOf(speedSeek.getProgress()));

        /* Scenario Navigation View */
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ScenarioFragment fragment = new ScenarioFragment();
        fragmentTransaction.replace(R.id.side_fragment, fragment);
        fragmentTransaction.commit();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        String[] title = getResources().getStringArray(R.array.tabs);

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    digitalMonitor = new DigitalMonitor();
                    return digitalMonitor;
                default:
                    graph = new Graph();
                    return graph;
            }
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.bt_switch_item);
        ViewGroup viewGroup = (ViewGroup) item.getActionView();
        btSwitch = viewGroup.findViewById(R.id.bt_switch);
        btSwitch.setChecked(isBTEnabled());
        btSwitch.setOnCheckedChangeListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connect_bt:
                // Launch the DeviceListActivity to see devices and do scan
                connectDevice();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.bt_switch:
                if (b) {
                    enableBT();
                } else {
                    disableBT();
                }
                break;
            case R.id.scenario_switch:
                ValueAnimator anim = ValueAnimator.ofInt(sideNavView.getMeasuredWidth() > 0 ? sideNavView.getMeasuredWidth() : 0,
                        sideNavView.getMeasuredWidth() > 0 ? 0 : getPixelsFromDPs(this, 256));
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = sideNavView.getLayoutParams();
                        layoutParams.width = val;
                        sideNavView.setLayoutParams(layoutParams);
                    }
                });
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                        scenarioSwitch.setEnabled(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        scenarioSwitch.setEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                anim.setDuration(500);
                anim.start();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (btSwitch != null) {
            btSwitch.setChecked(isBTEnabled());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothService != null) {
            bluetoothService.stop();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (bluetoothService == null) {
            setupChat();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.angle_seek:
                angleVal.setText(String.valueOf(i - getResources().getInteger(R.integer.angle_value_margin)));
                break;
            case R.id.speed_seek:
                speedVal.setText(String.valueOf(i));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        sendData(speedSeek.getProgress(), angleSeek.getProgress() - getResources().getInteger(R.integer.angle_value_margin));
    }

    /* Bluetooth Service */

    private String mConnectedDeviceName = null;

    private BluetoothService bluetoothService;

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        // Initialize the BluetoothChatService to perform bluetooth connections
        bluetoothService = new BluetoothService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer();
    }

    /**
     * Sends a json data.
     */
    public void sendData(int speed, int angle) {
        // Check that we're actually connected before trying anything
        if (bluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.none_found, Toast.LENGTH_SHORT).show();
            return;
        }

        SentData sentData = new SentData();
        sentData.setAttack_Angle(angle);
        sentData.setTarget_Pitot_Speed(speed);
        String message = JSONUtils.toJson(sentData);

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            bluetoothService.write(send);
        }
    }

    /**
     * Updates the status on the action bar.
     *
     * @param state integer to determine the state
     */
    private void setStatus(int state, String name) {
        int resId, colorId, visibility;
        switch (state) {
            case BluetoothService.STATE_CONNECTED:
                resId = R.string.connected;
                colorId = R.color.connected;
                visibility = View.VISIBLE;
                btStateProgress.setVisibility(View.GONE);
                btName.setText(name);
                getWindow().addFlags(FLAG_KEEP_SCREEN_ON);
                break;
            case BluetoothService.STATE_CONNECTING:
                resId = R.string.connecting;
                colorId = R.color.connecting;
                visibility = View.GONE;
                btStateProgress.setVisibility(View.VISIBLE);
                break;
            default:
                resId = R.string.disconnected;
                colorId = R.color.disconnected;
                visibility = View.GONE;
                btStateProgress.setVisibility(View.GONE);
                getWindow().clearFlags(FLAG_KEEP_SCREEN_ON);
                break;
        }
        btStatus.setText(resId);
        btStateColor.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(colorId)));
        btName.setVisibility(visibility);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus(msg.arg1, mConnectedDeviceName);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus(msg.arg1, null);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            setStatus(msg.arg1, null);
                            if (graph != null) {
                                graph.reset();
                            }
                            break;
                    }
                    break;
                case Const.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Log.d("JSON", writeMessage);
                    break;
                case Const.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    int begin = msg.arg1;
                    int end = msg.arg2;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf);
                    readMessage = readMessage.substring(begin, end);
                    if (readMessage.startsWith("{")) {
                        if (digitalMonitor != null) {
                            digitalMonitor.updateValues(JSONUtils.fromJSON(readMessage));
                        }
                        if (graph != null) {
                            graph.update(JSONUtils.fromJSON(readMessage));
                        }
                    }
                case Const.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Const.DEVICE_NAME);
                    break;
                case Const.MESSAGE_TOAST:
                    break;
            }
        }
    };

    private void connectDevice() {
        if (!BTUtils.isBTEnabled()) BTUtils.enableBT();
        BluetoothDeviceManager manager = new BluetoothDeviceManager();
        manager.pickDevice(this, this);
    }

    @Override
    public void onDevicePicked(BluetoothDevice device) {
        // Attempt to connect to the device

        if (device == null) {
            return;
        }
        bluetoothService.connect(device);
    }
}
