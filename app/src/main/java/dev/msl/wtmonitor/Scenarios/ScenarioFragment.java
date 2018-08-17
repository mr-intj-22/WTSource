package dev.msl.wtmonitor.Scenarios;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.msl.wtmonitor.R;
import dev.msl.wtmonitor.Scenarios.ScenariosAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScenarioFragment extends Fragment {

    /* Side View */
    @BindView(R.id.add_scenario)
    FloatingActionButton addScenario;
    @BindView(R.id.rvscenarios)
    RecyclerView rvScenarios;
    @BindView(R.id.noscenarios)
    View noScenarios;
    private ScenariosAdapter scenariosAdapter;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
