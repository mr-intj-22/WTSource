package dev.msl.wtmonitor.Scenarios;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import dev.msl.wtmonitor.R;

public class MainItemView extends LinearLayout {
    public MainItemView(Context context) {
        super(context);
        initialize(context);
    }

    public MainItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public MainItemView(
            Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        setClipChildren(false);
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.scenario_item, this, true);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Log.wtf("WTF", "Saving MainItemView state");
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.wtf("WTF", "Restoring MainItemView state");
        super.onRestoreInstanceState(state);
    }
}