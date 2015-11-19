package com.limjin.mobileg2015;

import android.app.Activity;
import android.content.Intent;
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

public class OptionPage extends Activity implements OnClickListener {
    private Button btn_back;
    private Button btn_music;
   // Intent music = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.optionpage);

        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
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
