package com.relsellglobal.crk.app.customcomponents;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by anil on 28/6/15.
 */
public class CustomLinearLayoutDots extends TextView {

    private int mDifficultyLevel = 0;

    public CustomLinearLayoutDots(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public CustomLinearLayoutDots(Context context) {
        this(context, null);
        setWillNotDraw(false);
    }

    public CustomLinearLayoutDots(Context context, int difficultylevel) {
        this(context, null);
        setWillNotDraw(false);
    }

    public void setmDifficultyLevel(int difficultyLevel) {
        mDifficultyLevel = difficultyLevel;
        this.invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawViewBackground(canvas);
        drawViewCircle(canvas);
    }


    private void drawViewBackground(Canvas canvas) {

        Paint p = new Paint();
        p.setColor(Color.argb(0,0,0,0));
        canvas.drawRect(0,0, canvas.getWidth(), canvas.getHeight(), p);
    }
    private void drawViewCircle(Canvas canvas) {

        Paint p = new Paint();

        for(int i=0,j=(canvas.getWidth() / 2) - 20;i<3;i++,j+=20) {
            if(i == mDifficultyLevel) {
                p.setColor(Color.rgb(0,0,0));
                canvas.drawCircle(j, 50,6, p);
            } else {
                p.setColor(Color.rgb(255, 255, 255));
                canvas.drawCircle(j,50,6, p);
            }
        }

    }

}
