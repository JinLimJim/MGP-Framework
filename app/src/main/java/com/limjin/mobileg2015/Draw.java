package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 7/12/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

/*********************************************************************************************
 * Utility functions to handle drawing with canvas.
 *********************************************************************************************/
public class Draw
{
    //Size of screen depending on phone-------------------------------//
    public static int ScreenWidth;
    public static int ScreenHeight;

    //View space coordinates----------------------------------------//
    public static float ViewWidth;
    public static float ViewHeight;

    //how many pixels per unit?
    public static float unitX;
    public static float unitY;

    public static float convertX;
    public static float convertY;
    public static Paint paint = new Paint();

    //use this canvas so no need pass around by value?
    public static Canvas canvas = null;

    public static void Set(float ViewWidth, float ViewHeight, Context context)
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        Draw.ViewWidth = ViewWidth;
        Draw.ViewHeight = ViewHeight;
        Draw.unitX = (float)ScreenWidth / ViewWidth;
        Draw.unitY = (float)ScreenHeight / ViewHeight;
        convertX = ViewWidth / (float)ScreenWidth;
        convertY = ViewHeight / (float)ScreenHeight;
    }

    /*********************************************************************************************
     * Initialize bitmap to scale accordingly with screen
     *********************************************************************************************/
    public static Bitmap SetBitmap(int xScale, int yScale, int resourceID, Context context)
    {
        Bitmap mm = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        mm = Bitmap.createScaledBitmap(mm, RealX(xScale), RealY(yScale), true);
        return mm;
    }

    /*********************************************************************************************
     * Draw the bitmap
     * assume pos start from bottom left
     *********************************************************************************************/
    public static void Draw(Bitmap ret, Vector2 pos, Vector2 scale, Canvas canvas)
    {
        canvas.drawBitmap(ret, RealX(pos.x),
                ScreenHeight - RealY(pos.y + scale.y), null);
    }

    public static void DrawDebugRect(Vector2 top, Vector2 bottom, Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(RealX(bottom.x),
                ScreenHeight - RealY(top.y), RealX(top.x),
                ScreenHeight - RealY(bottom.y), paint);
    }

    public static void DrawRect(Vector2 top, Vector2 bottom, Canvas canvas, int color)
    {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(3);

        canvas.drawRect(RealX(bottom.x),
                ScreenHeight - RealY(top.y), RealX(top.x),
                ScreenHeight - RealY(bottom.y), paint);
    }

    public static void DrawLine(Vector2 start, Vector2 end, Canvas canvas, int color)
    {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(10);

        //From startPos to current dragging pos
        canvas.drawLine(RealX(start.x), ScreenHeight - RealY(start.y),
                RealX(end.x), ScreenHeight - RealY(end.y), paint);
    }

    //screen space to view space
    public static float RealX(float x){return x * unitX;}
    public static float RealY(float y){return y * unitY;}
    public static int RealX(int x){return (int)((float)x * unitX);}
    public static int RealY(int y){return (int)((float)y * unitY);}

    //view space to screen space
    public static float ConvertX(float x){return x * convertX;}
    public static float ConvertY(float y){return y * convertY;}
}
