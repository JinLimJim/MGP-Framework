package com.limjin.mobileg2015;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;

/*************************************************************************************
Obstacle rectangle
 *************************************************************************************/
public class Obstacle extends AABB
{
    private Bitmap ret;
    private static Paint paint = new Paint();

    public Obstacle(float x, float y, float scaleX, float scaleY, Context context)
    {
        super(x, y, scaleX, scaleY);
       // ret = BitmapFactory.decodeResource(context.getResources(), R.drawable.optionpage);
    }

    /*************************************************************************************
     Draw function
     *************************************************************************************/
    public void Draw(Canvas canvas)
    {
        paint.setColor(Color.MAGENTA);
        paint.setStrokeWidth(3);
        top.Copy(pos);
        top.AddWith(scale);

        canvas.drawRect(pos.x, canvas.getHeight() - top.y, top.x, canvas.getHeight() - pos.y, paint);
      //  canvas.drawBitmap(ret, bottom.x, canvas.getHeight() - bottom.y, null);
    }
}
