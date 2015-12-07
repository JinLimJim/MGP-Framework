package com.limjin.mobileg2015;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by tanyiecher on 6/12/2015.
 */
public class Bin extends AABB
{
    private Bitmap ret;
    private boolean recyclable;
    private int score;

    public Bin(boolean recyclable, Context context, float x, float y, float xScale, float yScale)
    {
        super(x, y, xScale, yScale);
        this.recyclable = recyclable;
        score = 0;

        //bitmap-----------------------------//
        if(recyclable)
         ret = BitmapFactory.decodeResource(context.getResources(), R.drawable.recycledustbin);
        else
            ret = BitmapFactory.decodeResource(context.getResources(), R.drawable.dustbin);
        ret = Bitmap.createScaledBitmap(ret, (int) scale.x, (int) scale.y, true);
    }

    public void Init()
    {
        score = 0;
    }

    boolean RubbishCheck(Rubbish rubbish)
    {
        if(!AABB_Det(rubbish))  //as long as got collide, will return true
            return false;

        if(AABB_Det_MiddleX(rubbish)) {
++score;
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

        return true;
    }

    public void Draw(Canvas canvas)
    {
        super.DrawDebug(canvas);
        canvas.drawBitmap(ret, pos.x, canvas.getHeight() - (pos.y + scale.y), null);
    }

    public int GetScore(){return score;}
}
