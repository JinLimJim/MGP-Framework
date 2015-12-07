package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 6/12/2015.
 */

/*************************************************************************************
 Handles physics component of game object.
 *************************************************************************************/
public class Physics
{
    private float Dcceleration;
    private float max_speed;
    public Vector2 GetVel = new Vector2();
    public Vector2 Vel = new Vector2();
    private  Vector2 Dir = new Vector2();

    public Physics(float Max_Speed, float Dcceleration)
    {
        Set(Max_Speed, Dcceleration);
    }

    public void Set(float Max_Speed, float Dcceleration)
    {
        this.max_speed = Max_Speed;
        this.Dcceleration = Dcceleration;
    }

    /*************************************************************************************
     Movement function. Call this function ONCE to set the end destination.
     Vel will accelerate and slow down to destination.
     *************************************************************************************/
    void StartMove(float speed, Vector2 dir)
    {
        Dir.Copy(dir);
        Vel.x = speed * Dir.x;
        Vel.y = speed * Dir.y;
    }

    /*************************************************************************************
     Update physics movement.
     *************************************************************************************/
    void Update(double dt)
    {
        //define Gravity 9.8
       // GetVel.Copy(Vel);
        //GetVel.MultiplyWithVec(Dir);

        //if(Vel.y > 0.f)
            Vel.y -= 9.8f * dt;
       // else
       //     Vel.y += 9.8f * dt;
        Vel.x *= 0.99f;
        Vel.y *= 0.99f;
    }

    void InverseXdir(){Vel.x = -Vel.x;}
    void InverseYdir(){Vel.y = -Vel.y;}

    void SetVel(float x, float y)
    {
        Vel.x = x;
        Vel.y = y;
    }
}
