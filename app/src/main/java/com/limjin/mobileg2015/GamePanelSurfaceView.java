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

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering

    // Variables used for background rendering
    private Bitmap bg, scaledbg, ground, scaledground;

    // Variables used for dangerous dustbin
    private Bitmap Ddustbin, scaledDDustbin;

    // Variables used for recycled dustbin
    private Bitmap Rdustbin, scaledRDustbin;

    // Variables used for rubbish
    private Bitmap rubbish, scaledRubbish;
    short rX = 0, rY = 0;

    // Define Screen width and Screen height as integer
    int screenWidth, screenHeight;
    // Variables for defining background start and end point
    private short bgX = 0, bgY = 0;
    // bitmap array to stores 4 images of the spaceship
    private Bitmap[] spaceShip = new Bitmap[4];
    // Variable as an index to keep track of the spaceship images
    private short spaceshipIndex = 0;
    // Variables to store the new location upon touch
    private short mX = 0, mY = 0;

    // Set up variables for Drag Events
    short X, Y;

    // Set up variables for game details E.g. score, etc
    int score;

    // Define sprite object from the SpriteAnimation class
    private SpriteAnimation stone_anim;

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;

    // Collision bounds


    // Variable for Game State check
    private short GameState;

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context){

        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        // Load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        scaledbg = Bitmap.createScaledBitmap(bg, screenWidth, screenHeight, true);
//        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
//        scaledground = Bitmap.createScaledBitmap(ground, screenWidth, screenHeight, true);

        Ddustbin = BitmapFactory.decodeResource(getResources(), R.drawable.dangerdustbin);
        scaledDDustbin = Bitmap.createScaledBitmap(Ddustbin, 250, 250, true);

        Rdustbin = BitmapFactory.decodeResource(getResources(), R.drawable.recycledustbin);
        scaledRDustbin = Bitmap.createScaledBitmap(Rdustbin, 250, 250, true);

        rubbish = BitmapFactory.decodeResource(getResources(), R.drawable.rubbish2);
        scaledRubbish = Bitmap.createScaledBitmap(rubbish, 300, 300, true);

        // Load the images of the spaceships
        spaceShip[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_1);
        spaceShip[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_2);
        spaceShip[2] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_3);
        spaceShip[3] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_4);

        // Load the animated sprite
        stone_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.flystone), 320, 64, 5, 5);

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    //must implement inherited abstract methods
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

    public void RenderGameplay(Canvas canvas) {
        // Re-draw 2nd image after the 1st image ends
        if(canvas == null) {
            return;
        }
        canvas.drawBitmap(scaledbg, bgX, bgY, null);
        canvas.drawBitmap(scaledbg, bgX + screenWidth, bgY, null);

        // Draw the rubbish
        canvas.drawBitmap(scaledRubbish, rX, rY, null);

        // Draw the spaceships
        //canvas.drawBitmap(spaceShip[spaceshipIndex], mX, mY, null);

        // Draw the dangerous dustbin
        canvas.drawBitmap(scaledDDustbin, canvas.getWidth() - scaledDDustbin.getWidth() - 800, canvas.getHeight() - scaledDDustbin.getHeight(), null);

        // Draw the recycled dustbin
        canvas.drawBitmap(scaledRDustbin, canvas.getWidth() - scaledRDustbin.getWidth() - 100, canvas.getHeight() - scaledRDustbin.getHeight(), null);

        // Render the sprite
        //stone_anim.Draw(canvas);
        //stone_anim.setY(600);

        // To print FPS on the screen
        Paint paint = new Paint();
        paint.setARGB(255, 255, 0, 0);
        paint.setStrokeWidth(120); // how thick you want the text to be in terms of pixel
        paint.setTextSize(60);
        paint.setShadowLayer(10, 10, 8, Color.GREEN);
        canvas.drawText("FPS: " + FPS, 130, 50, paint);

        // To print score on the screen
        Paint scoring = new Paint();
        scoring.setARGB(255, 255, 0, 0);
        scoring.setStrokeWidth(120); // how thick you want the text to be in terms of pixel
        scoring.setTextSize(60);
        scoring.setShadowLayer(10, 10, 8, Color.BLACK);
        canvas.drawText("Score: " + score, 1200, 50, scoring);
    }


    //Update method to update the game play
    public void update(float dt, float fps){
        FPS = fps;

        switch (GameState) {
            case 0: {
                // Update the background to allow panning effect
                bgX -= 100 * dt; // Allow panning speed
                if(bgX < -screenWidth) {
                    bgX = 0;
                }

                // Update the spaceship images / shipIndex so that the animation will occur.
                spaceshipIndex++;
                spaceshipIndex %= 4; // run through the index

                // Update the sprite animation
                stone_anim.Update(System.currentTimeMillis());
            }
            break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas){
        switch (GameState)
        {
            case 0:
                RenderGameplay(canvas);
                break;
        }
    }

    public boolean CheckCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2){
        if(x2 >= x1 && x2 < (x1 + w1))   // Start to detect collision of the top left corner
        {
            if(y2 > y1 && y2 < (y1 + h1)) // Comparing yellow box to blue box
            {
                return true;
            }
        }

        if((x2 + w2) >= x1 && (x2 + w2) < (x1 + w1)) // Start to detect collision of the top right corner
        {
            if(y2 > y1 && y2 < (y1 + h1))
            {
                return true;
            }
        }

        if( x2 >= x1 && x2 < (x1 + w1)) // Start to detect collision of the Bottom Left corner
        {
            if((y2 + h2) > y1 && (y2 + h2) < (y1 + h1))
            {
                return true;
            }
        }

        if((x2 + w2) >= x1 && (x2 + w2) < (x1 + w2)) // Start to detect collision of the bottom right corner
        {
            if((y2 + h2) > y1 && (y2 + h2) < (y1 + h1))
            {
                return true;
            }
        }
        return false;
    }

 /*   @Override
    public boolean onTouchEvent(MotionEvent event){

        // In event of touch on screen, the spaceship will relocate to the point of touch
        short X = (short) event.getX();
        short Y = (short) event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            // New location for the image to land on
            mX = (short)(X - spaceShip[spaceshipIndex].getWidth() / 2);
            mY = (short)(Y - spaceShip[spaceshipIndex].getHeight() / 2);

            if(CheckCollision(mX, mY, spaceShip[spaceshipIndex].getWidth(), spaceShip[spaceshipIndex].getHeight(), stone_anim.getX(), stone_anim.getY(), stone_anim.getSpriteWidth(), stone_anim.getSpriteHeight()))
            {
                Random r = new Random();
                stone_anim.setX(r.nextInt(screenWidth));
                stone_anim.setY(r.nextInt(screenHeight));
            }
        }

        return super.onTouchEvent(event);
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // Get the x & y positions when player touches the screen
                X = (short) event.getRawX();
                Y = (short) event.getRawY();
                break;


            case MotionEvent.ACTION_UP:
                // Get the x & y positions of the player's last touch
                X = (short) event.getRawX();
                Y = (short) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // Set the spaceship image position to where the touched event happens
               // mX = (short)((short) event.getX() - (spaceShip[spaceshipIndex].getWidth() / 2));
                //mY = (short)((short) event.getY() - (spaceShip[spaceshipIndex].getHeight() / 2));

                // Set the rubbish image position to where the touched event happens
                rX = (short)((short) event.getX() - (scaledRubbish.getWidth() / 2));
                rY = (short)((short) event.getY() - (scaledRubbish.getHeight() / 2));

/*                if(CheckCollision(mX, mY, spaceShip[spaceshipIndex].getWidth(), spaceShip[spaceshipIndex].getHeight(), scaledDustbin.getWidth(),scaledDustbin.getHeight(), dustbin.getWidth(), dustbin.getHeight()))
                {
                    score++;
                }*/

                if(CheckCollision(mX, mY, spaceShip[spaceshipIndex].getWidth(), spaceShip[spaceshipIndex].getHeight(), stone_anim.getX(), stone_anim.getY(), stone_anim.getSpriteWidth(), stone_anim.getSpriteHeight()))
                {
                    Random r = new Random();
                    stone_anim.setX(r.nextInt(screenWidth));
                    stone_anim.setY(r.nextInt(screenHeight));
                }

                break;
        }
        return true;
    }
}
