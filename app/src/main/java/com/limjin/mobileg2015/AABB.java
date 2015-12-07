package com.limjin.mobileg2015;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by tanyiecher on 6/12/2015.
 */
public class AABB
{
    protected Vector2 pos = new Vector2();  //bottom
    protected Vector2 scale = new Vector2();

    protected static Vector2 bottom = new Vector2();
    protected static Vector2 top = new Vector2();
    protected static Vector2 checkBottom = new Vector2();
    protected static Vector2  checkTop = new Vector2();
    protected static Vector2 normal = new Vector2();    //to see which side collide
    protected static boolean collideX = false;
    protected static boolean collideY = false;
    protected static float lengthX = 0.f;
    protected static float lengthY = 0.f;

    public AABB()
    {}

    public AABB(float xPos, float yPos, float xScale, float yScale)
    {
        pos.x = xPos;
        pos.y = yPos;
        scale.x = xScale;
        scale.y = yScale;
    }

    public void UpdatePos(float xPos, float yPos)
    {
        pos.x = xPos;
        pos.y = yPos;
    }

    //for touch event, pos set to middle
    public void UpdatePosMiddle(float xPos, float yPos)
    {
        pos.x = xPos - scale.x * 0.5f;
        pos.y = yPos - scale.y * 0.5f;
    }

    public boolean AABB_Det(AABB checkMe)
    {
        bottom.Copy(pos);
        top.Copy(pos);
        top.AddWith(scale);
        checkBottom.Copy(checkMe.pos);
        checkTop.Copy(checkMe.pos);
        checkTop.AddWith(checkMe.scale);

        return (top.x > checkBottom.x &&
                bottom.x < checkTop.x) &&
                (top.y > checkBottom.y &&
                        bottom.y < checkTop.y);
    }

    //collides and is in the middle of x range
    public boolean AABB_Det_MiddleX(AABB checkMe)
    {
        bottom.Copy(pos);
        top.Copy(pos);
        top.AddWith(scale);
        checkBottom.Copy(checkMe.pos);
        checkTop.Copy(checkMe.pos);
        checkTop.AddWith(checkMe.scale);

        if((top.x > checkBottom.x && bottom.x < checkTop.x) && (top.y > checkBottom.y && bottom.y < checkTop.y))
        {
            if ((top.x > checkTop.x) && (bottom.x < checkBottom.x))
                return true;
            else
                return false;
        }
        return false;
    }

    public boolean AABB_Res(AABB checkMe)
    {
        collideX = collideY = false;
        normal.SetZero();

        bottom.Copy(pos);
        top.Copy(pos);
        top.AddWith(scale);
        checkBottom.Copy(checkMe.pos);
        checkTop.Copy(checkMe.pos);
        checkTop.AddWith(checkMe.scale);

        //no collision---------------------------------------------------------//
        if(!AABB_Det(checkMe))
            return false;

        //check collide which pos/neg------------------------------------------------------//
        /****************************************** X *****************************************/
        //Middle--------------------------//
        if(top.x < checkTop.x && bottom.x >checkBottom.x )
        {
            //check Y which side collide--------------//
            if(top.y > checkTop.y)  //pos
                normal.y = 1.f;
            else    //neg
                normal.y = -1.f;

            //check Y offset------------------------//
            if(normal.y == 1.f)
                lengthY = checkTop.y - bottom.y;
            else
                lengthY = top.y - checkBottom.y;

            //pos offset---------------------------//
            pos.y += lengthY * normal.y;
            collideX = true;    //x side is longest on Y surface
            return true;
        }

        //Corner---------------------//
        else if(top.x < checkTop.x)  //pos
            normal.x = 1.f;
        else    //neg
            normal.x = -1.f;

        //y
        /****************************************** Y *****************************************/
        //Middle--------------------------//
        if(top.y < checkTop.y && bottom.y >checkBottom.y )
        {
            //check X which side collide--------------//
            if(top.x > checkTop.x)  //pos
                normal.x = 1.f;
            else    //neg
                normal.x = -1.f;

            //check Y offset------------------------//
            if(normal.x == 1.f)
                lengthX = checkTop.x - bottom.x;
            else
                lengthX = top.x - checkBottom.x;

            //pos offset---------------------------//
            pos.x += lengthX * normal.x;
            collideY = true;    //y side is longest on X surface
            return true;
        }

        //Corner---------------------//
        else if(top.y < checkTop.y)  //pos
            normal.y = 1.f;
        else    //neg
            normal.y = -1.f;

        //check collide which side-------------------------------------------------------//
        //x
        if(normal.x == 1.f)
            lengthX = top.x - checkBottom.x;
        else
            lengthX = checkTop.x - bottom.x;

        //y
        if(normal.y == 1.f)
            lengthY = top.y - checkBottom.y;
        else
            lengthY = checkTop.y - bottom.y;

        if(lengthX > lengthY)
            collideX = true;
        else
            collideY = true;

        //translate back out----------------------------------------------------------//
        if(collideY)
        {
            pos.x += lengthX * -normal.x;
        }
        else
        {
            pos.y += lengthY * -normal.y;
        }

        return true;
    }

    //check corner collision, changes offset and normal
    private float CheckCorner(float offset, float normal, final float _top, final float _CheckTop, final float _bottom, final float _CheckBottom)
    {
        return offset;
    }



    public void DrawDebug(Canvas canvas)
    {
       Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        top.Copy(pos);
        top.AddWith(scale);

        canvas.drawRect(pos.x, canvas.getHeight() - top.y, top.x, canvas.getHeight() - pos.y, paint);
    }
}
