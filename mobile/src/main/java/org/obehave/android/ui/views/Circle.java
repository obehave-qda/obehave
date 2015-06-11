package org.obehave.android.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import org.obehave.android.R;
import org.obehave.model.Color;

public class Circle extends View{

    private Paint circlePaint;
    private int circleColor;


    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs){

        circlePaint = new Paint();
        //get the attributes specified in attrs.xml using the name we included
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.Circle, 0, 0);

        try {
            circleColor = a.getInteger(R.styleable.Circle_color, 0);//0 is default
        } finally {
            a.recycle();
        }
    }

    public int getCircleColor(){
        return circleColor;
    }

    public void setCircleColor(int newColor){
        //update the instance variable
        circleColor = newColor;
        //redraw the view
        invalidate();
        requestLayout();
    }

    public void setCircleColor(Color color){
        int red = 0;
        int green = 0;
        int blue = 0;
        if(color != null){
            red = color.getRed();
            green = color.getGreen();
            blue = color.getBlue();
        }

        setCircleColor(android.graphics.Color.rgb(red, green, blue));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int viewWidthHalf = this.getMeasuredWidth()/2;
        int viewHeightHalf = this.getMeasuredHeight()/2;

        int radius = 0;

        if(viewWidthHalf>viewHeightHalf)
            radius=viewHeightHalf;
        else
            radius=viewWidthHalf;

        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);

        circlePaint.setColor(circleColor);
        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, circlePaint);

    }
}
