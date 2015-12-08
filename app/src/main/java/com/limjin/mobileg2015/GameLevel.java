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
    private float yVal;
    private boolean goUp = false;

    private boolean initAlready;

    /*************************************************************************************
     Constructor
     *************************************************************************************/
    public GameLevel()
    {
        initAlready = false;

        obstacles.clear();
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
    public void Init(Context context)
    {
        if(!initAlready)
        {
            initAlready = true;
            rubbishMan = new RubbishManager(70, context);

            //posX, posY, scaleX, scaleY
            //right wall------------------------//
            obstacles.add(new Obstacle(Draw.ViewWidth * 0.95f, 0.f, Draw.ViewHeight * 0.1f, Draw.ViewHeight));

            //floor----//
            obstacles.add(new Obstacle(Draw.ViewWidth * 0.2f,  Draw.ViewHeight * -0.05f, Draw.ViewWidth, Draw.ViewHeight * 0.1f));

            //roof----------//
            obstacles.add(new Obstacle(Draw.ViewWidth * 0.2f,  Draw.ViewHeight * 0.95f, Draw.ViewWidth, Draw.ViewHeight * 0.1f));

            //random wall---------------------//
            obstacles.add(new Obstacle(600.f,  Draw.ViewHeight * 0.7f, 150.f, 200.f));


            catapult = new Catapult(200.f, 0.f, 130, 260, context);

            //bin---------------------------------//
            recycleBin = new Bin(true, context, Draw.ViewWidth * 0.75f, Draw.ViewHeight * 0.05f, 160, 250);
            normalBin = new Bin(false, context, Draw.ViewWidth * 0.45f, Draw.ViewHeight * 0.05f, 160, 250);
        }

        rubbishMan.Init();
          catapult.Init(70, context, rubbishMan);
           recycleBin.Init();
            normalBin.Init();
            Score = 0;

        //misc
        yVal = obstacles.get(3).GetPosY();
    }

    /*************************************************************************************
     Level gameplay update
     *************************************************************************************/
    public void Update(float dt)
    {
        //moving obstacle------------------------//
        if(obstacles.get(3).GetPosY() > 600.f && goUp)
            goUp = false;
        if(obstacles.get(3).GetPosY() < 20.f && !goUp)
            goUp = true;

        if(goUp)
            yVal = 1.8f;
        else
            yVal = -1.8f;

        obstacles.get(3).Move(0.f, yVal);

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
            if(ru.CollisonCheck(recycleBin))
            {
               if( recycleBin.RubbishCheck(ru))
                   ru.Deactivate();
            }

            if(ru.CollisonCheck(normalBin))
            {
               if( normalBin.RubbishCheck(ru))
                   ru.Deactivate();
            }

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
    public void Draw(float FPS)
    {
      for(int i = 0; i < obstacles.size(); ++i)
            obstacles.get(i).Draw();
       catapult.Draw();

        recycleBin.Draw();
        normalBin.Draw();

        // To print score on the screen
        Draw.paint.setStyle(Paint.Style.FILL);
        Draw.paint.setARGB(255, 255, 255, 0);
        Draw.paint.setStrokeWidth(120); // how thick you want the text to be in terms of pixel
        Draw.paint.setTextSize(60);
        //Draw.paint.setShadowLayer(10, 10, 8, Color.MAGENTA);
        Draw.canvas.drawText("Score: " + Score, 800, 50, Draw.paint);

        // To print FPS on the screen
        Draw.paint.setARGB(255, 255, 0, 0);
        Draw.paint.setStrokeWidth(120); // how thick you want the text to be in terms of pixel
        Draw.paint.setTextSize(60);
        //Draw.paint.setShadowLayer(10, 10, 8, Color.BLUE);
        Draw.canvas.drawText("FPS: " + FPS, 130, 50, Draw.paint);
    }
}
