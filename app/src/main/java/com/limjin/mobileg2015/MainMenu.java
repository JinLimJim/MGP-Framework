package com.limjin.mobileg2015;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener {
    private Button btn_start;
    private Button btn_option;
    private Button btn_gallery;
    MediaPlayer bgMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.mainmenu);

        bgMusic = MediaPlayer.create(MainMenu.this, R.raw.chasers);
        bgMusic.setLooping(true);
        bgMusic.start();

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        btn_option = (Button)findViewById(R.id.btn_option);
        btn_option.setOnClickListener(this);

        btn_gallery = (Button)findViewById(R.id.btn_gallery);
        btn_gallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent();

        if(view == btn_start)
        {
            intent.setClass(this, Splashpage.class);
        }

        else if(view == btn_option){
            intent.setClass(this, OptionPage.class);
        }

        else if(view == btn_gallery){
            intent.setClass(this, GalleryPage.class);
        }

        startActivity(intent);
    }

    protected void onPause(){
        super.onPause();
        bgMusic.release();
      //  finish();
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}
