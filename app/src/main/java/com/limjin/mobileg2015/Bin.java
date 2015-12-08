package com.limjin.mobileg2015;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by tanyiecher on 6/12/2015.
 */
public class Bin extends AABB
{
    private Bitmap ret;
    private boolean recyclable;
    private int score;

    public Bin(boolean recyclable, Context context, float x, float y, int xScale, int yScale)
    {
        super(x, y, xScale, yScale);
        this.recyclable = recyclable;
        score = 0;

        //bitmap-----------------------------//
        if(recyclable)
           ret = BitmapFactory.decodeResource(context.getResources(), R.drawable.recycledustbin);
        else
            ret = BitmapFactory.decodeResource(context.getResources(), R.drawable.dangerdustbin);

        ret = Bitmap.createScaledBitmap(ret, Draw.RealX(xScale), Draw.RealX(yScale), true);
    }

    public void Init()
    {
        score = 0;
    }

    boolean RubbishCheck(Rubbish rubbish)
    {
        boolean g = false;
        if(AABB_Det_MiddleX(rubbish)) {

            g = true;
            if (recyclable) {
                if (rubbish.GetType() == Rubbish.TYPE.RECYCLABLE) {
                    ++score;
                }
            } else {
                if (rubbish.GetType() == Rubbish.TYPE.NON_RECYCLABLE) {
                    ++score;
                }
            }
        }

        return g;
    }

    public void Draw()
    {
       // super.DrawDebug();
        Draw.RealX(pos.x);

        Draw.canvas.drawBitmap(ret, Draw.RealX(pos.x),
                Draw.ScreenHeight - (Draw.RealY(pos.y + scale.y)), null);
    }

    public int GetScore(){return score;}
}
