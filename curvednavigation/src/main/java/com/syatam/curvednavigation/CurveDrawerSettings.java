package com.syatam.curvednavigation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;

public class CurveDrawerSettings {

    public final static int CURVE_INSIDE = 0;
    public final static int CURVE_OUTSIDE = 1;
    private boolean curveInside = true;
    private float curveWidth;
    private float angle;

    //default background color of the navigation drawer
    private Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);

    /**
     * Convert dp to px
     *
     * @param context
     * @param dp
     * @return
     */
    public static float dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    /**
     * Constructor
     *
     * @param context
     * @param attrs
     */

    public CurveDrawerSettings(Context context, AttributeSet attrs) {

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CurveDrawer, 0, 0);
        curveWidth = styledAttributes.getDimension(R.styleable.CurveDrawer_curve_width, dpToPx(context, 10));

        final int direction = styledAttributes.getInt(R.styleable.CurveDrawer_curve_direction, CURVE_INSIDE);
        curveInside = (direction == CURVE_INSIDE);

        int[] attrsArray = new int[]{
                android.R.attr.background,
                android.R.attr.layout_gravity,
        };

        TypedArray androidAttrs = context.obtainStyledAttributes(attrs, attrsArray);
        backgroundDrawable = androidAttrs.getDrawable(0);

        androidAttrs.recycle();
        styledAttributes.recycle();
    }


    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public boolean isCurveInside() {
        return curveInside;
    }

    public float getCurveWidth() {
        return curveWidth;
    }

    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }

}
