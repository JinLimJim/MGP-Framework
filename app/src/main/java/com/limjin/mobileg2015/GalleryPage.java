package com.limjin.mobileg2015;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class GalleryPage extends Activity implements OnClickListener {
    private Button btn_back;
    private Button btn_change;
    Boolean changeImage = false;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.gallerypage);

        image = (ImageView) findViewById(R.id.imageView1);

        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        btn_change = (Button)findViewById(R.id.btn_change);
        btn_change.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changeImage == false) {
                    image.setImageResource(R.drawable.krghost);
                    changeImage = true;
                }
                else {
                    image.setImageResource(R.drawable.city);
                    changeImage = false;
                }
            }
        });

        Random rnd = new Random();
        View LayoutView = findViewById(R.id.Layout);
        LayoutView.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent();

        if(view == btn_back)
        {
            intent.setClass(this, MainMenu.class);
        }

        startActivity(intent);
    }

    protected void onPause(){
        super.onPause();
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}
