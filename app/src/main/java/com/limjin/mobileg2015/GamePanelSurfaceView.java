package com.limjin.mobileg2015;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View.OnTouchListener;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

// Implement this interface to receive information about changes to the surface.
public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    /*************************************** System *********************************************/
    // Thread to control the rendering----------------------//
    private GameThread myThread = null;

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;

    /*************************************** Gameplay ********************************************/
    GameLevel gameLevel = new GameLevel();

    //finger touching screen----------------------------//
     private AABB finger;

    // Variables used for background rendering-------------//
    public static Bitmap bg, scaledbg;

    // background start and end point
    private short bgX = 0, bgY = 0;


    /*************************************** Drag *********************************************/
    // Set up variables for Drag Events
    short X, Y;

    /*************************************** Misc *********************************************/
    // Set up variables for game details E.g. score, etc
    int score;

    // Define sprite object from the SpriteAnimation class
    private SpriteAnimation stone_anim;


    // Variable for Game State check
    private short GameState;

    /**************************************************************************************
    //constructor for this GamePanelSurfaceView class
     **************************************************************************************/
    public GamePanelSurfaceView (Context context){

        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // Set information to get screen size
        Draw.Set(1400, 787, context);

        // Load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        scaledbg = Bitmap.createScaledBitmap(bg, Draw.ScreenWidth, Draw.ScreenHeight, true);

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

       gameLevel.Init(context);

        //finger press aabb----------------------//
        finger = new AABB(170.f, 0.f, 70.f, 70.f);
    }

    /**************************************************************************************
    Necessary abstract methods
     **************************************************************************************/
    public void surfaceCreated(SurfaceHolder holder){

        // Create the thread
        if (!myThread.isAlive()){
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        // Destroy the thread
        if (myThread.isAlive()){
            myThread.startRun(false);
        }
        boolean retry = true;
        while (retry) {
            try {
                myThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
            }
        }


    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    /**************************************************************************************
     Update
     **************************************************************************************/
    public void update(float dt, float fps){
        FPS = fps;

        switch (GameState) {
            case 0: {

                //update game level-----------------------/
              gameLevel.Update(dt);

                // Update the background to allow panning effect
                bgX -= 100 * dt; // Allow panning speed
                if(bgX < -Draw.ScreenWidth) {
                    bgX = 0;
                }
            }
            break;
        }
    }

    /**************************************************************************************
   Draw
     **************************************************************************************/
    public void doDraw(Canvas canvas){
        switch (GameState)
        {
            case 0:

                //temp. fix, why draw game when is still in splash page?
                if (Draw.canvas == null || Draw.paint == null)
                {
                    break;
                }

                RenderGameplay(canvas);

                //draw game level------------------------//
                  gameLevel.Draw(FPS);
               finger.DrawDebug();
                break;
        }
    }


    /**************************************************************************************
     Gameplay draw
     **************************************************************************************/
    public void RenderGameplay(Canvas canvas) {
        // Re-draw 2nd image after the 1st image ends
        if(canvas == null) {
            return;
        }
        Draw.canvas.drawBitmap(scaledbg, bgX, bgY, null);
        Draw.canvas.drawBitmap(scaledbg, bgX + Draw.ScreenWidth, bgY, null);


        // To print FPS on the screen
       // paint.setARGB(255, 255, 0, 0);
      //  paint.setStrokeWidth(120); // how thick you want the text to be in terms of pixel
       // paint.setTextSize(60);
        //paint.setShadowLayer(10, 10, 8, Color.BLUE);
       // canvas.drawText("screen: " + Draw.ScreenWidth + " " + Draw.ScreenHeight, 130, 50, paint);
       // canvas.drawText("view: " + Draw.ViewWidth + " " + Draw.ViewHeight, 130, 90, paint);
       // canvas.drawText("unit: " + Draw.unitX + " " + Draw.unitY, 130, 130, paint);
        //canvas.drawText("convert 100 screen: " + Draw.RealX(100.f) + " ", 130, 170, paint);
       // canvas.drawText("convert 100 view: " + Draw.ConvertX(Draw.RealX(100.f)) + " ", 130, 200, paint);
    }

    /**************************************************************************************
    Touch
     **************************************************************************************/
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //touch pos in screen space.
        float x = event.getX();
        float y = event.getY();

        //convert to view space-----------------------//
        x = Draw.ConvertX(x);
        y = Draw.ViewHeight - Draw.ConvertY(y); //invert Y to set origin at bottom

        //pass in--------------------------------------//
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:

                //game touch-------------------------------------//
               finger.UpdatePosMiddle(x, y);
                gameLevel.FingerDown(x, y, finger);
                break;


            case MotionEvent.ACTION_UP:

                //game touch-------------------------------------//
                finger.UpdatePosMiddle(x, y);
               gameLevel.FingerUp(x, y, finger);
                break;

            case MotionEvent.ACTION_MOVE:

                //game touch-------------------------------------//
                finger.UpdatePosMiddle(x, y);
                gameLevel.FingerMoves(x, y, finger);
                break;
        }
        return true;
    }
}
