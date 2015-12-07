package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 6/12/2015.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.LinkedList;

/*************************************************************************************
 Init update and render game level
 *************************************************************************************/
public class GameLevel
{
   //Game object manager--------------------------------------//
   private RubbishManager rubbishMan;

    //catapult------------------------------------------------//
   private Catapult catapult;

    //Obstacles------------------------------------------------//
    private LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();

    //bins-------------------------------------------------//
    private Bin recycleBin;
    private Bin normalBin;
    private int Score = 0;

    private boolean initAlready;

    private Paint paint = new Paint();

    /*************************************************************************************
     Constructor
     *************************************************************************************/
    public GameLevel()
    {
        initAlready = false;
    }

    /*************************************************************************************
    Load level from text file
     *************************************************************************************/
    public void LoadLevel(String path)
    {

    }

    /*************************************************************************************
    Init level: call everytime start new level
     *************************************************************************************/
    public void Init(Context context, float width, float height)
    {
        if(!initAlready)
        {
            initAlready = true;
            rubbishMan = new RubbishManager(50, context);
            //posX, posY, scaleX, scaleY
            //right wall------------------------//
            obstacles.add(new Obstacle(width * 0.95f, 0.f, width * 0.1f, height, context));

            //floor----//
            obstacles.add(new Obstacle(width * 0.2f,  height * -0.05f, width, height * 0.1f, context));

            //roof----------//
            obstacles.add(new Obstacle(width * 0.2f,  height * 0.95f, width, height * 0.1f, context));

            //random wall---------------------//
            obstacles.add(new Obstacle(600.f,  height * 0.7f, 150.f, 200.f, context));

            catapult = new Catapult(200.f, 0.f, 130.f, 260.f, context);

            //bin---------------------------------//
            recycleBin = new Bin(true, context, width * 0.75f, height * 0.05f, 160.f, 250.f);
            normalBin = new Bin(false, context, width * 0.45f, height * 0.05f, 160.f, 250.f);
        }

        rubbishMan.Init();
        catapult.Init(40, context, rubbishMan);
        recycleBin.Init();
        normalBin.Init();
        Score = 0;
    }

    /*************************************************************************************
     Level gameplay update
     *************************************************************************************/
    public void Update(float dt)
    {
        catapult.Update(dt);

        //check collision------------------------------------//
        for(Rubbish ru : catapult.rubbishPile)
        {
            if(!ru.GetActive())
                continue;

            for(Obstacle ob : obstacles)
            {
                ru.CollisonCheck(ob);
            }

            //score or no score, if hit dustbin, still despawn
            if(recycleBin.RubbishCheck(ru))
                ru.Deactivate();
            else if(normalBin.RubbishCheck(ru))
                ru.Deactivate();
        }

        //update score----------------------------------------//
        Score = recycleBin.GetScore() + normalBin.GetScore();
    }

    /*************************************************************************************
    Called when user JUST touches the screen
     *************************************************************************************/
    public void FingerDown(float xTouchPos, float yTouchPos, AABB finger)
    {
     catapult.StartDrag(xTouchPos, yTouchPos, finger);
    }

    /*************************************************************************************
     Called when user is STILL touches the screen and moves his finger
     *************************************************************************************/
    public void FingerMoves(float xTouchPos, float yTouchPos, AABB finger)
    {
      catapult.Drag(xTouchPos, yTouchPos, finger);
    }

    /*************************************************************************************
     Called when user is lets go finger from screen
     *************************************************************************************/
    public void FingerUp(float xTouchPos, float yTouchPos, AABB finger)
    {
       catapult.Release();
    }

    /*************************************************************************************
     Level gameplay Draw
     *************************************************************************************/
    public void Draw(Canvas canvas, float FPS)
    {
       for(int i = 0; i < obstacles.size(); ++i)
            obstacles.get(i).Draw(canvas);
       catapult.Draw(canvas);

        recycleBin.Draw(canvas);
        normalBin.Draw(canvas);

        // To print score on the screen
        paint.setARGB(255, 255, 255, 0);
        paint.setStrokeWidth(120); // how thick you want the text to be in terms of pixel
        paint.setTextSize(60);
        paint.setShadowLayer(10, 10, 8, Color.MAGENTA);
        canvas.drawText("Score: " + Score, 800, 50, paint);

        // To print FPS on the screen
        paint.setARGB(255, 255, 0, 0);
        paint.setStrokeWidth(120); // how thick you want the text to be in terms of pixel
        paint.setTextSize(60);
        paint.setShadowLayer(10, 10, 8, Color.BLUE);
        canvas.drawText("FPS: " + FPS, 130, 50, paint);
    }
}
