package com.moreclub.moreapp.main.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;

import static com.moreclub.moreapp.main.constant.Constants.LEFT;
import static com.moreclub.moreapp.main.constant.Constants.RIGHT;
import static com.moreclub.moreapp.main.constant.Constants.ROOT;

/**
 * Created by Administrator on 2017-06-09.
 */

public class LinesView extends View {

    @BindDimen(R.dimen.lines_width)
    int linesWidth;
    private List<float[]> locations;
    private String tag;
    private int linesCount;
    private boolean isFirst = true;

    public LinesView(Context context) {
        super(context);
    }

    public LinesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTag(String tag) {
        this.tag = tag;
        invalidate();
    }

    public void setLocations(List<float[]> locations) {
        if (isFirst) {
            isFirst = false;
            this.locations = locations;
            invalidate();
        }
    }

    public void setLinesCount(int linesCount) {
        this.linesCount = linesCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#16FFFFFF"));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(6 * ScreenUtil.getScreenWidth(getContext()) / 720);
        if (tag != null)
            switch (tag) {
                case LEFT:
                case RIGHT:
                    if (locations != null) {
                        for (int i = 0; i < (linesCount > 4 ? 4 : linesCount); i++) {
                            if (locations.size() > 0 && i > 0)
                                canvas.drawLine(locations.get(i)[0], locations.get(i)[1],
                                        locations.get(i - 1)[0], locations.get(i - 1)[1],
                                        paint);
                        }
                    }
                    break;
                case ROOT:
                    if (locations != null) {
                        for (int i = 0; i < locations.size(); i++) {
                            if (i < locations.size() - 1) {
                                canvas.drawLine(locations.get(i)[0], locations.get(i)[1],
                                        locations.get(i + 1)[0], locations.get(i + 1)[1],
                                        paint);
                            }
                        }
                    }
                    break;

            }

    }
}
