package com.limjin.mobileg2015;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class GamePage extends Activity{
    ImageView img;
    MediaPlayer bgMusic;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(new GamePanelSurfaceView(this));

        bgMusic = MediaPlayer.create(GamePage.this, R.raw.fieldmusic);
        bgMusic.setLooping(true);
        bgMusic.start();
    }

    protected void onPause(){
        super.onPause();
        bgMusic.release();
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}
