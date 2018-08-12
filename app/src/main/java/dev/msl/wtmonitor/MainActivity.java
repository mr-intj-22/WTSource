package dev.msl.wtmonitor;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.msl.wtmonitor.Scenario.ScenarioFragment;

import static dev.msl.wtmonitor.Utils.BTUtils.disableBT;
import static dev.msl.wtmonitor.Utils.BTUtils.enableBT;
import static dev.msl.wtmonitor.Utils.BTUtils.isBTEnabled;
import static dev.msl.wtmonitor.Utils.ScreenUtils.getPixelsFromDPs;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

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

    /*fragments*/
    private ScenarioFragment sideFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /* main */
        setSupportActionBar(toolbar);

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
//                if (!BTUtils.isBTEnabled()) BTUtils.enableBT();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (btSwitch != null) {
            btSwitch.setChecked(isBTEnabled());
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
        Toast.makeText(this,
                speedSeek.getProgress() + " - " + (angleSeek.getProgress()-getResources().getInteger(R.integer.angle_value_margin)),
                Toast.LENGTH_SHORT).show();
    }
}
