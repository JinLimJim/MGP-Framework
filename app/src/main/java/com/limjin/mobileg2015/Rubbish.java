package com.limjin.mobileg2015;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by tanyiecher on 6/12/2015.
 */

/*************************************************************************************
Rubbish to throw.
 *************************************************************************************/
public class Rubbish extends AABB
{
    enum TYPE
    {
        NON_RECYCLABLE,
        RECYCLABLE,
    }

    private boolean collided = false;
    private Bitmap ret;
    private TYPE type;
    private Physics physics = new Physics(0.f, 0.f);
    private boolean active;

    public Rubbish(float x, float y, float xScale, float yScale, TYPE type, float acceleration, Context context)
    {
        super(x, y, xScale, yScale);
        this.type = type;
        active = false;

        //bitmap-----------------------------//
        ret = BitmapFactory.decodeResource(context.getResources(), R.drawable.button_start);
        ret = Bitmap.createScaledBitmap(ret, (int) xScale, (int) yScale, true);
    }

    /*************************************************************************************
     Init
     *************************************************************************************/
    public void Set(float x, float y, TYPE type, float acceleration)
    {
        pos.x = x;
        pos.y = y;
        this.type = type;
        active = false;
        physics.SetVel(0.f, 0.f);
    }

    /*************************************************************************************
     Update, animation
     *************************************************************************************/
    public void Update(float dt)
    {
      //  if(collided)
        //    return;

       physics.Update(dt); //update vel
        pos.AddWith(physics.Vel);   //update pos
    }

    /*************************************************************************************
    Check collision
     *************************************************************************************/
    public void CollisonCheck(Obstacle obstacle)
    {
        //collision function returns true if collides-----------------------//
        //pass in obstacle AABB? dunno if class shearing is the correct way
        if(!AABB_Res(obstacle))
            return;

        //theres a collision, rebound dir-----------------------------------//
       if(collideX) //x coillide means x length is bigger but on Y surface, vice versa
        {
           physics.Vel.y *= -1.f;
        }
        else
            physics.Vel.x *= -1.f;

        //physics.Vel.SetZero();
        collided = true;
    }

    /*************************************************************************************
     When being dragged
     *************************************************************************************/
    public void Dragged(float touchPosX, float touchPosY)
    {
        UpdatePosMiddle(touchPosX, touchPosY);
    }

    /*************************************************************************************
     On release of rubbish from catapult
     *************************************************************************************/
    public void Released(float initialSpeed, Vector2 dir)
    {
       physics.StartMove(initialSpeed, dir);
    }

    /*************************************************************************************
     Draw function
     *************************************************************************************/
    public void Draw(Canvas canvas)
    {
        super.DrawDebug(canvas);
        canvas.drawBitmap(ret, pos.x, canvas.getHeight() - (pos.y + scale.y), null);
    }

    void Activate(){active = true;}
    void Deactivate(){active = false;}
    boolean GetActive(){return active;}
    TYPE GetType(){return type;}
    void SetType(TYPE type){this.type = type;}
}
