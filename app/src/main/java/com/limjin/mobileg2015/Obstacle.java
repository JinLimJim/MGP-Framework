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

    public Obstacle(float x, float y, float scaleX, float scaleY)
    {
        super(x, y, scaleX, scaleY);
    }

    /*************************************************************************************
     Move function: moves obstacle
     *************************************************************************************/
    public void Move(float x, float y)
    {
        pos.x += x;
        pos.y += y;
    }

    /*************************************************************************************
     Draw function
     *************************************************************************************/
    public void Draw()
    {
        top.Copy(pos);
        top.AddWith(scale);

        Draw.paint.setColor(Color.CYAN);
        Draw.paint.setStrokeWidth(3);
        Draw.paint.setStyle(Paint.Style.FILL);

        Draw.canvas.drawRect(Draw.RealX(pos.x),
                Draw.ScreenHeight - Draw.RealY(top.y), Draw.RealX(top.x),
                Draw.ScreenHeight - Draw.RealY(pos.y), Draw.paint);
    }

    public float GetPosX(){return pos.x;}
    public float GetPosY(){return pos.y;}
}
