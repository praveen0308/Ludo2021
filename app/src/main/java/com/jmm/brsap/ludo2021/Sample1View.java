package com.jmm.brsap.ludo2021;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;

public class Sample1View extends View {

    private static final String QUOTE = "Now is the time for all good men to come to the aid of their country.";

    private Animation animation;
    private Paint gPaint;
    private Paint gPaint1;
    private Paint gPaint2;
    private Paint cPaint;
    private Path glowCircle;
    private Path circle;
    private Paint tPaint;

    private int shadowRadius=0;
    private int xInc=1;

    private int shadowRadius1=0;
    private int xInc1=1;

    private int shadowRadius2=0;
    private int xInc2=1;


    public Sample1View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {


        cPaint = new Paint();
        cPaint.setAntiAlias(true);
        cPaint.setDither(true);

        BlurMaskFilter filter = new BlurMaskFilter(10f, BlurMaskFilter.Blur.INNER);
        cPaint.setMaskFilter(filter);


        int x = 150;
        int y = 150;
        int r = 100;

        glowCircle = new Path();
        glowCircle.addCircle(x, y, r+5, Path.Direction.CW);

        int color1 = Color.rgb(40, 40, 40);
        int color2 = Color.rgb(220, 220, 220);
        LinearGradient gradient = new LinearGradient(0, 0, 0, y*2, color2, color1, Shader.TileMode.REPEAT);
        cPaint.setShader(gradient);

        circle = new Path();
        circle.addCircle(x, y, r, Path.Direction.CW);

        tPaint = new Paint();
        tPaint.setTextSize(18f);
        tPaint.setTypeface(Typeface.DEFAULT_BOLD);
        tPaint.setColor(Color.BLACK);
        tPaint.setAntiAlias(true);

        // Important for certain APIs
        setLayerType(LAYER_TYPE_SOFTWARE, gPaint);
    }

    private void initAnimation() {
        animation = new RotateAnimation(0, 360, 150, 150);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        animation.setDuration(7500L);
        animation.setInterpolator(new LinearInterpolator());

        startAnimation(animation);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (animation == null)
            initAnimation();
        int color1 = Color.rgb(255, 0, 0);
        int color2 = Color.rgb(100, 0, 255);
        gPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LinearGradient gradient = new LinearGradient(0, 0, 0, 150*2, color2, color1, Shader.TileMode.CLAMP);
        gPaint.setShader(gradient);
        gPaint.setAlpha(0);
        if (shadowRadius>=60){
            xInc = -1;
        }
        if (shadowRadius<=0){
            xInc = 1;
        }
        shadowRadius = shadowRadius + xInc;
        gPaint.setShadowLayer(shadowRadius, 0, 0, Color.argb(200, 255, 0, 0));

        gPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        gPaint1.setAlpha(255);
        gPaint1.setShader(gradient);
        if (shadowRadius1>=50){
            xInc1 = -1;
        }
        if (shadowRadius1<=0){
            xInc1 = 1;
        }
        shadowRadius1 = shadowRadius1 + xInc1;
        gPaint1.setShadowLayer(shadowRadius1, 0, 0, Color.argb(150, 255, 0, 250));


        gPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        gPaint2.setAlpha(255);
        gPaint2.setShader(gradient);
        if (shadowRadius2>=30){
            xInc2 = -1;
        }
        if (shadowRadius2<=0){
            xInc2 = 1;
        }
        shadowRadius2 = shadowRadius2 + xInc2;
        gPaint2.setShadowLayer(shadowRadius2, 0, 0, Color.argb(200, 255, 100, 20));


        canvas.drawPath(glowCircle, gPaint);
        canvas.drawPath(glowCircle, gPaint1);
        canvas.drawPath(glowCircle, gPaint2);

        canvas.drawPath(circle, cPaint);
        canvas.drawTextOnPath(QUOTE, circle, 0, 20, tPaint);
        invalidate();

    }
}
