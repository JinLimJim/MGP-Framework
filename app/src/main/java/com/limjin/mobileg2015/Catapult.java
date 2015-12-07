package com.limjin.mobileg2015;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import java.util.LinkedList;

/**
 * Created by tanyiecher on 6/12/2015.
 */

/*************************************************************************************
Launches rubbish.
 *************************************************************************************/
public class Catapult
{
    enum STATE
    {
        IDLE,
        DRAGGING,
        LAUNCHING,
    }

    STATE state;

    //Design---------------------------------------------------------------//
    private Vector2 pos = new Vector2(); //bottom
    private Vector2 startPos = new Vector2();
    private Vector2 scale = new Vector2();

    //dragging---------------------------------------------------------------//
    private Vector2 fingerPos = new Vector2();
    private AABB bound;
    private boolean dragging;   //is it dragging?

    //Speed------------------------------------------------------------------//
    private static float dragLength = 0.f;
    private static float speedFactor = 0.1f;

    //catapult-------------------------------------------------------------//
    public LinkedList<Rubbish> rubbishPile = new LinkedList<Rubbish>();
    boolean isBeingDragged;
    public int totalDragged = 0;  //keep track of current rubbish being dragged

    //bitmap---------------------------------------------------------//
    public static Bitmap ret;
    private static Paint paint = new Paint();

    //Utilities-----------------------//
    public static Vector2 dir = new Vector2();

    public Catapult(float posX, float posY, float scaleX, float scaleY, Context context)
    {
        this.pos.x = posX;
        this.pos.y = posY;
        this.scale.x = scaleX;
        this.scale.y = scaleY;
        startPos.Copy(pos);
        startPos.y += scale.y * 0.85f;
        startPos.x += scaleX * 0.4f;
        dragging = false;
        state = STATE.IDLE;

        //bitmap-----------------------------------------------------------//
        ret = BitmapFactory.decodeResource(context.getResources(), R.drawable.catapult);
        ret = Bitmap.createScaledBitmap(ret, (int) scale.x, (int) scale.y, true);

        bound = new AABB(pos.x, pos.y, scale.x, scale.y);
    }

    public void Init(int totalRubbish, Context context, RubbishManager rubbishManager)
    {
        isBeingDragged = false;
        totalDragged = 0;    //currently dragged is undefined

        rubbishPile.clear();
        //rubbish----------------------------------------------------------//
        for(int i = 0; i < totalRubbish; ++i)
        {
            rubbishPile.add(rubbishManager.GetRubbish(context, Rubbish.TYPE.RECYCLABLE));
        }

    }

    public void Update(float dt)
    {
        //update rubbish-------------------//
        for(int i = 0; i < totalDragged; ++i)
        {
            if(rubbishPile.get(i).GetActive())
              rubbishPile.get(i).Update(dt);
        }
    }

    public void Draw(Canvas canvas)
    {
        canvas.drawBitmap(ret, pos.x, canvas.getHeight() - (pos.y + scale.y), null);

        //testing line------------------------------------------------//
        if(state == STATE.DRAGGING)
        {
            paint.setColor(Color.RED);
            paint.setStrokeWidth(10);

            //From startPos to current dragging pos
            canvas.drawLine(startPos.x, canvas.getHeight() - startPos.y, fingerPos.x, canvas.getHeight() - fingerPos.y, paint);
        }

        //draw the rubbish------------------------------------------//
        for(int i = 0; i < totalDragged; ++i)
        {
            if(rubbishPile.get(i).GetActive())
                 rubbishPile.get(i).Draw(canvas);
        }

        paint.setARGB(255, 255, 0, 0);
        paint.setStrokeWidth(120); // how thick you want the text to be in terms of pixel
        paint.setTextSize(60);
        canvas.drawText("Active: " + totalDragged, 130, 80, paint);

        bound.DrawDebug(canvas);
    }

    /*************************************************************************************
     Call when finger started dragging on screen
     *************************************************************************************/
    public void StartDrag(float xTouchPos, float yTouchPos, AABB finger)
    {
        //if collide and still got rubbish-------------------------------------------------//
        if(bound.AABB_Det(finger) && totalDragged < rubbishPile.size()) {

            //increase index----------------------------------------------------//
            ++totalDragged;

            fingerPos.Set(xTouchPos, yTouchPos);
            state = STATE.DRAGGING;
            dragging = true;
        }
        else
            dragging = false;
    }

    /*************************************************************************************
    Call when finger is dragging on screen
     *************************************************************************************/
    public void Drag(float xTouchPos, float yTouchPos, AABB finger)
    {
        //if not dragging state-----------------------------------------------------//
        if(!dragging)
            return;

        //being dragged--------------------------------------//
        fingerPos.Set(xTouchPos, yTouchPos);
        rubbishPile.get(totalDragged-1).Dragged(xTouchPos, yTouchPos);

        //cal dir-----------------------------//
        dir.Set(1.f, 0.f);  //fae 0 deg

        if(!fingerPos.Same(startPos))
        {
            dir.Copy(fingerPos);
            dir.SubtractWith(startPos);
            dir.Normalized();
        }
    }

    /*************************************************************************************
     Call when finger just RELEASED from screen
     *************************************************************************************/
    public void Release()
    {
        //if not dragging state-----------------------------------------------------//
        if(!dragging)
            return;

        dragging = false;
        state = STATE.LAUNCHING;

        //No need safety check (currentDragged < size), if exceeded size, dragging state will not even be successful
        float initialSpeed = 0.f;

        dir.Inverse();
        Vector2 len = new Vector2();
        len.Copy(fingerPos);
        len.SubtractWith(startPos);

        dragLength = len.Length();

        //abs---------------//
        if(dragLength < 0.f)
            dragLength = -dragLength;
        dragLength *= speedFactor;
        rubbishPile.get(totalDragged-1).Released(dragLength, dir);
    }
}
