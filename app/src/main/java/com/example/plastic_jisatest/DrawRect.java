package com.example.plastic_jisatest;

/**
 * Created by jisajose on 2017-08-12.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class DrawRect extends  View {
    private Rect rectangle;
    private Paint paint;

    public DrawRect(Context context) {
        super(context);
        int x = 300;
        int y = 300;
        int sideLength = 500;

        // create a rectangle that we'll draw later
        rectangle = new Rect(x, y, sideLength, sideLength);

        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.GRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
        canvas.save();
        canvas.save();
        canvas.rotate(45);
        canvas.drawRect(166, 748, 314, 890, paint);
        canvas.restore();

//stuff to draw that should be rotated
        canvas.restore();


    }
    public void rotate(){

    }

}
