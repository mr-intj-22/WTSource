package dev.msl.wtmonitor.Scenarios;


import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.liefery.android.vertical_stepper_view.VerticalStepperView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.msl.wtmonitor.MainActivity;
import dev.msl.wtmonitor.POJO.Scenario;
import dev.msl.wtmonitor.R;
import dev.msl.wtmonitor.Utils.JSONUtils;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScenarioFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private final int PERMISSIONS_REQUEST_READ_STORAGE = 1242;
    private final int PICK_SCENARIO = 1241;
    /* Side View */
    @BindView(R.id.add_scenario)
    FloatingActionButton addScenario;
    @BindView(R.id.stepper_list)
    VerticalStepperView verticalStepper;
    @BindView(R.id.noscenarios)
    View noScenarios;
    @BindView(R.id.add_scenario_card)
    CardView add_scenario_card;
    @BindView(R.id.angle_seek)
    AppCompatSeekBar angleSeek;
    @BindView(R.id.speed_seek)
    AppCompatSeekBar speedSeek;
    @BindView(R.id.duration_seek)
    AppCompatSeekBar durationSeek;
    @BindView(R.id.angle_val)
    TextView angleVal;
    @BindView(R.id.speed_val)
    TextView speedVal;
    @BindView(R.id.duration_val)
    TextView durationVal;
    @BindView(R.id.scenario_state_changer)
    ImageView scenario_state_changer;
    @BindView(R.id.scenario_state_holder)
    View scenario_state_holder;
    @BindView(R.id.scenario_menu)
    View scenario_menu;

    private ScenariosAdapter adapter;
    private int cardHeight = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private boolean scenarioStarted = false;

    public ScenarioFragment() {
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
        View view = inflater.inflate(R.layout.fragment_scenario, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addScenario.setOnClickListener(this);
        scenario_state_holder.setOnClickListener(this);
        scenario_menu.setOnClickListener(this);
        // seeks
        angleSeek.setOnSeekBarChangeListener(this);
        speedSeek.setOnSeekBarChangeListener(this);
        durationSeek.setOnSeekBarChangeListener(this);
        angleSeek.setMax(getResources().getInteger(R.integer.max_motor_angle));
        angleSeek.setProgress(getResources().getInteger(R.integer.default_motor_angle));
        angleVal.setText(String.valueOf(angleSeek.getProgress() - getResources().getInteger(R.integer.angle_value_margin)));
        speedSeek.setMax(getResources().getInteger(R.integer.max_motor_speed));
        speedSeek.setProgress(getResources().getInteger(R.integer.default_motor_speed));
        speedVal.setText(String.valueOf(speedSeek.getProgress()));
        durationSeek.setMax(getResources().getInteger(R.integer.max_duration));
        durationSeek.setProgress(getResources().getInteger(R.integer.default_duration));
        durationVal.setText(String.valueOf(durationSeek.getProgress()));
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                cardHeight = add_scenario_card.getMeasuredHeight();
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        // Setting up and initializing the form
        adapter = new ScenariosAdapter(getActivity());
        verticalStepper.setStepperAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add_scenario:
                Scenario scenario = new Scenario();
                scenario.setMotor_angle(angleSeek.getProgress() - getResources().getInteger(R.integer.angle_value_margin));
                scenario.setMotor_speed(speedSeek.getProgress());
                scenario.setDuration(durationSeek.getProgress());
                adapter.addStep(scenario);
                break;
            case R.id.scenario_state_holder:
                if (adapter != null && adapter.getCount() > 0) {
                    scenarioStarted = !scenarioStarted;
                    animateSync(scenarioStarted);
                    cardVisibility();
                }
                break;
            case R.id.scenario_menu:
                PopupMenu popup = new PopupMenu(Objects.requireNonNull(getContext()), view);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.scenario_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.clear:
                                adapter.clear();
                                break;
                            case R.id.from_file:
                                requestPermission();
                                break;
                            case R.id.random:
                                randomScenarios(10);
                                break;
                        }
                        return true;
                    }
                });
                popup.show(); //showing popup menu
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data.getData() == null)
            return;

        switch (requestCode) {
            case PICK_SCENARIO:
                // Get the Uri of the selected file
                Uri uri = data.getData();
                String uriString = uri.getPath();
                File json = new File(uriString);
                adapter.clear();
                adapter.reset();
                try {
                    adapter.addStep(JSONUtils.fromJSONArray(file2String(json)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case PERMISSIONS_REQUEST_READ_STORAGE:
                pickScenario();
                break;
        }
    }

    private void pickScenario() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        startActivityForResult(intent, PICK_SCENARIO);
    }

    private String file2String(File file) throws IOException {
        int length = (int) file.length();

        byte[] bytes = new byte[length];

        try (FileInputStream in = new FileInputStream(file)) {
            int i = in.read(bytes);
            in.close();
        }
        return new String(bytes);
    }

    private void cardVisibility() {
        ValueAnimator anim = ValueAnimator.ofInt(add_scenario_card.getMeasuredHeight() > 0 ? add_scenario_card.getMeasuredWidth() : 0,
                add_scenario_card.getMeasuredHeight() > 0 ? 0 : cardHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = add_scenario_card.getLayoutParams();
                layoutParams.height = val;
                add_scenario_card.setLayoutParams(layoutParams);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (!scenarioStarted) {
                    handler.removeCallbacks(runnable);
                    addScenario.show();
                } else {
                    addScenario.hide();
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (scenarioStarted) {
                    if (!adapter.hasNext()) adapter.reset();
                    //start handler as activity become visible
                    sync(adapter.getScenario().getDuration());
                }
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
    }

    private void sync(int delay) {
        ((MainActivity) Objects.requireNonNull(getActivity())).sendData(
                adapter.getScenario().getMotor_speed(),
                adapter.getScenario().getMotor_angle());
        if (adapter.hasNext()) {
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    adapter.next();
                    sync(adapter.getScenario().getDuration());
                }
            }, delay * 1000);
        } else {
            animateSync(false);
            scenarioStarted = false;
            cardVisibility();
        }
    }

    private void animateSync(boolean b) {
        if (b) {
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            rotateAnimation.setDuration(500);
            rotateAnimation.setRepeatMode(Animation.RESTART);
            scenario_state_changer.startAnimation(rotateAnimation);
        } else {
            scenario_state_changer.clearAnimation();
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
            case R.id.duration_seek:
                durationVal.setText(String.valueOf(i));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void randomScenarios(int n) {
        Random random = new Random();
        ArrayList<Scenario> scenarios = new ArrayList<>(n);
        int max_speed = getResources().getInteger(R.integer.max_motor_speed);
        int min_speed = getResources().getInteger(R.integer.min_motor_speed);
        int max_angle = getResources().getInteger(R.integer.max_motor_angle);
        int margin_angle = getResources().getInteger(R.integer.angle_value_margin);
        for (int i = 0; i < n; i++) {
            scenarios.add(new Scenario()
                    .setDuration(random.nextInt(60) + 1)
                    .setMotor_speed(random.nextInt(max_speed - min_speed + 1) + min_speed)
                    .setMotor_angle(random.nextInt(max_angle + 1) - margin_angle));
        }
        adapter.addStep(scenarios);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // No explanation needed; request the permission
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            pickScenario();
        }
    }
}
