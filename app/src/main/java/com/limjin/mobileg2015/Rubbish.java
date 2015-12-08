package com.limjin.mobileg2015;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

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
    private static float time = 10.f;   //10 seconds will despawn
    private float timer;

    public Rubbish(float x, float y, int xScale, int yScale, TYPE type, float acceleration, Context context)
    {
        super(x, y, xScale, yScale);
        this.type = type;
        active = false;

        //bitmap-----------------------------//
        ret = BitmapFactory.decodeResource(context.getResources(), R.drawable.button_start);
        ret = Bitmap.createScaledBitmap(ret, Draw.RealX(xScale), Draw.RealY(yScale), true);
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
        timer = 0.f;
    }

    /*************************************************************************************
     Update, animation
     *************************************************************************************/
    public void Update(float dt)
    {
        if(timer > time)
            Deactivate();

      //  if(collided)
        //    return;
        timer += dt;

       physics.Update(dt); //update vel
        pos.AddWith(physics.Vel);   //update pos
    }

    /*************************************************************************************
    Check collision
     *************************************************************************************/
    public boolean CollisonCheck(AABB obstacle)
    {
        //collision function returns true if collides-----------------------//
        //pass in obstacle AABB? dunno if class shearing is the correct way
        if(!AABB_Res(obstacle))
            return false;

        //theres a collision, rebound dir-----------------------------------//
       if(collideX) //x coillide means x length is bigger but on Y surface, vice versa
        {
           physics.Vel.y *= -1.f;
        }
        else
            physics.Vel.x *= -1.f;

        //physics.Vel.SetZero();
        collided = true;
        return true;
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
    public void Draw()
    {
        super.DrawDebug();

        Draw.canvas.drawBitmap(ret, Draw.RealX(pos.x),
                Draw.ScreenHeight - (Draw.RealY(pos.y + scale.y)), null);
    }

    void Activate(){active = true;}
    void Deactivate(){active = false;}
    boolean GetActive(){return active;}
    TYPE GetType(){return type;}
    void SetType(TYPE type){this.type = type;}
}
