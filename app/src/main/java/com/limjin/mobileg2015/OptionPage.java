package com.limjin.mobileg2015;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.provider.Settings.SettingNotFoundException;

public class OptionPage extends Activity implements OnClickListener {
    private Button btn_back;
    private Button btn_music;
    MediaPlayer buttonClick;
    public static int music = 0;
    private SeekBar sb;
    float currentBrightness;
    int screenBrightness;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.optionpage);

        backgroundMusicOff();

        buttonClick = MediaPlayer.create(this, R.raw.buttonsound);

        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        // Set the SeekBar
        sb = (SeekBar)findViewById(R.id.seekBar);

        // Set the maximum value for SeekBar
        sb.setMax(255);

        currentBrightness = 0.0f;

        // Check if there's error on brightness
        try{
            currentBrightness = android.provider.Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch(SettingNotFoundException  e)
        {
            e.printStackTrace();
        }

        // Take the current brightness value to the screen brightness
        screenBrightness = (int)currentBrightness;
        sb.setProgress(screenBrightness);

        // When clicking / moving the seekbar functions
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Takes the progress value to change the brightness value
                android.provider.Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
            }
        });
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent();

        if(view == btn_back)
        {
            intent.setClass(this, MainMenu.class);
            buttonClick.start();
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

    public void backgroundMusicOff()
    {
        CheckBox checkbox = (CheckBox)findViewById(R.id.checkBox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    music = 1;
                } else {
                    music = 0;
                }
            }
        });
    }
}
