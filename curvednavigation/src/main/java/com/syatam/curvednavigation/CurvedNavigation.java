package com.syatam.curvednavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenuView;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import java.lang.reflect.Field;

public class CurvedNavigation extends NavigationView {

    private static int THRESHOLD;

    private CurveDrawerSettings settings;
    private int height = 0;
    private int width = 0;
    private Path clipPath, curvePath;

    public CurvedNavigation(Context context) {
        super(context);
        init(context, null);
    }

    public CurvedNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        settings = new CurveDrawerSettings(context, attrs);
        settings.setAngle(ViewCompat.getElevation(this));

        /**
         * If hardware acceleration is on (default from API 14), clipPath worked correctly
         * from API 18.
         *
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        setBackgroundColor(Color.TRANSPARENT);
        setInsetsColor(Color.TRANSPARENT);
        THRESHOLD = Math.round(CurveDrawerSettings.dpToPx(getContext(), 15)); //some threshold for child views while remeasuring them
    }

    private void setInsetsColor(int color) {
        try {
            Field insetForegroundField = ScrimInsetsFrameLayout.class.getDeclaredField("mInsetForeground");
            insetForegroundField.setAccessible(true);
            ColorDrawable colorDrawable = new ColorDrawable(color);
            insetForegroundField.set(this, colorDrawable);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("RtlHardcoded")
    private Path createClipPath() {
        final Path path = new Path();
        curvePath = new Path();

        float curveWidth = settings.getCurveWidth();
        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) getLayoutParams();
        if (settings.isCurveInside()) {
            if (layoutParams.gravity == Gravity.START || layoutParams.gravity == Gravity.LEFT) {

                curvePath.moveTo(width, 0);
                curvePath.quadTo(width - curveWidth, height / 2,
                        width, height);
                curvePath.close();

                path.moveTo(0, 0);
                path.lineTo(width, 0);
                path.quadTo(width - curveWidth, height / 2,
                        width, height);
                path.lineTo(0, height);
                path.close();
            } else if (layoutParams.gravity == Gravity.END || layoutParams.gravity == Gravity.RIGHT) {
                curvePath.moveTo(0, 0);
                curvePath.quadTo(curveWidth, height / 2,
                        0, height);
                curvePath.close();

                path.moveTo(width, 0);
                path.lineTo(0, 0);
                path.quadTo(curveWidth, height / 2,
                        0, height);
                path.lineTo(width, height);
                path.close();
            }
        } else {
            if (layoutParams.gravity == Gravity.START || layoutParams.gravity == Gravity.LEFT) {
                curvePath.moveTo(width - curveWidth / 2, 0);
                curvePath.quadTo(width + curveWidth / 2, height / 2,
                        width - curveWidth / 2, height);
                curvePath.close();

                path.moveTo(0, 0);
                path.lineTo(width - curveWidth / 2, 0);
                path.quadTo(width + curveWidth / 2, height / 2,
                        width - curveWidth / 2, height);
                path.lineTo(0, height);
                path.close();
            } else if (layoutParams.gravity == Gravity.END || layoutParams.gravity == Gravity.RIGHT) {
                curvePath.moveTo(curveWidth / 2, 0);
                curvePath.quadTo(-curveWidth / 2, height / 2,
                        curveWidth / 2, height);
                curvePath.close();

                path.moveTo(width, 0);
                path.lineTo(curveWidth / 2, 0);
                path.quadTo(-curveWidth / 2, height / 2,
                        curveWidth / 2, height);
                path.lineTo(width, height);
                path.close();
            }
        }
        return path;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            calculateLayoutAndChildren();
        }
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);

        if (child instanceof NavigationMenuView) {
            child.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(),
                    MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                    getMeasuredHeight(), MeasureSpec.EXACTLY));
        } else {
            super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
        }
    }

    private void calculateLayoutAndChildren() {
        if (settings == null) {
            return;
        }
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        if (width > 0 && height > 0) {
            clipPath = createClipPath();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                ViewCompat.setElevation(this, settings.getAngle());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setOutlineProvider(new ViewOutlineProvider() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void getOutline(View view, Outline outline) {
                            if (clipPath.isConvex()) {
                                outline.setConvexPath(clipPath);
                            }
                        }
                    });
                }
            }

            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View v = getChildAt(i);

                if (v instanceof NavigationMenuView) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        v.setBackground(settings.getBackgroundDrawable());
                    } else {
                        v.setBackgroundDrawable(settings.getBackgroundDrawable());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        ViewCompat.setElevation(v, settings.getAngle());
                    }

                    //adjustChildViews((ViewGroup) v);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @SuppressLint("RtlHardcoded")
    private void adjustChildViews(ViewGroup container) {
        final int containerChildCount = container.getChildCount();
        PathMeasure pathMeasure = new PathMeasure(curvePath, false);
        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) getLayoutParams();

        for (int i = 0; i < containerChildCount; i++) {
            View child = container.getChildAt(i);
            if (child instanceof ViewGroup) {
                adjustChildViews((ViewGroup) child);
            } else {
                float pathCenterPointForItem[] = {0f, 0f};
                Rect location = locateView(child);
                int halfHeight = location.height() / 2;

                pathMeasure.getPosTan(location.top + halfHeight, pathCenterPointForItem, null);
                if (layoutParams.gravity == Gravity.END || layoutParams.gravity == Gravity.RIGHT) {
                    int centerPathPoint = getMeasuredWidth() - Math.round(pathCenterPointForItem[0]);
                    if (child.getMeasuredWidth() > centerPathPoint) {
                        child.measure(MeasureSpec.makeMeasureSpec(centerPathPoint - THRESHOLD,
                                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                                child.getMeasuredHeight(), MeasureSpec.EXACTLY));
                        child.layout(centerPathPoint + THRESHOLD, child.getTop(), child.getRight(), child.getBottom());
                    }
                } else if (layoutParams.gravity == Gravity.START || layoutParams.gravity == Gravity.LEFT) {
                    if (child.getMeasuredWidth() > pathCenterPointForItem[0]) {
                        child.measure(MeasureSpec.makeMeasureSpec((Math.round(pathCenterPointForItem[0]) - THRESHOLD),
                                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                                child.getMeasuredHeight(), MeasureSpec.EXACTLY));
                        child.layout(child.getLeft(), child.getTop(), (Math.round(pathCenterPointForItem[0]) - THRESHOLD), child.getBottom());
                    }
                }
                //set text ellipsize to end to prevent it from overlapping edge
                if (child instanceof TextView) {
                    ((TextView) child).setEllipsize(TextUtils.TruncateAt.END);
                }
            }
        }
    }

    private Rect locateView(View view) {
        Rect loc = new Rect();
        int[] location = new int[2];
        if (view == null) {
            return loc;
        }
        view.getLocationOnScreen(location);

        loc.left = location[0];
        loc.top = location[1];
        loc.right = loc.left + view.getWidth();
        loc.bottom = loc.top + view.getHeight();
        return loc;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.save();
        canvas.clipPath(clipPath);
        super.dispatchDraw(canvas);

        canvas.restore();
    }
}
