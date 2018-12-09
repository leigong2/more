package com.moreclub.moreapp.main.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.moreclub.moreapp.R;


/**
 * Created by Administrator on 2017-07-04.
 */

public class WaveLayout extends FrameLayout {
    private int color;
    private int radius;
    private LoadView child1;
    private LoadView child2;
    private LoadView child3;
    private LoadView child4;
    public Handler handler = new Handler();

    public WaveLayout(Context context) {
        super(context);
        startChildren();
    }

    public WaveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View inflate = View.inflate(getContext(), R.layout.wave_layout, this);
        child1 = (LoadView) inflate.findViewById(R.id.load_bar1);
        child2 = (LoadView) inflate.findViewById(R.id.load_bar2);
        child3 = (LoadView) inflate.findViewById(R.id.load_bar3);
        child4 = (LoadView) inflate.findViewById(R.id.load_bar4);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveLayout);
        radius = typedArray.getDimensionPixelSize(R.styleable.WaveLayout_wave_layout_radius, 10);
        color = typedArray.getColor(R.styleable.WaveLayout_wave_layout_color, Color.YELLOW);
        child1.setRadius(radius);
        child1.setColor(color);
        child2.setRadius(radius);
        child2.setColor(color);
        child3.setRadius(radius);
        child3.setColor(color);
        child4.setRadius(radius);
        child4.setColor(color);
        startChildren();
    }

    public WaveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        startChildren();
    }

    private void startChildren() {
        child1.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                child2.start();
            }
        }, 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                child3.start();
            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                child4.start();
            }
        }, 1500);
    }
}