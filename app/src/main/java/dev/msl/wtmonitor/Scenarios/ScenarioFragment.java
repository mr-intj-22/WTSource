package dev.msl.wtmonitor.Scenarios;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
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
import android.widget.Toast;

import com.liefery.android.vertical_stepper_view.VerticalStepperView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.msl.wtmonitor.MainActivity;
import dev.msl.wtmonitor.POJO.Scenario;
import dev.msl.wtmonitor.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScenarioFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

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
                PopupMenu popup = new PopupMenu(getContext(), view);
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
        ((MainActivity) getActivity()).sendData(
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
}
