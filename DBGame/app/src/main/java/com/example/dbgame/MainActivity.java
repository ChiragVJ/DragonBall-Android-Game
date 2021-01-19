package com.example.dbgame;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;


/**
 * @author chirag vijay 27009630
 * Class for main activty main menu
 */

public class MainActivity extends AppCompatActivity{
    HomeWatcher mHomeWatcher; // create homewatcher
    private boolean isMuted = true; //boolean used to check if the mute button has been pressed
    private String name= "Anonymous"; // default name to be passed into activity
    private boolean mIsBound = false; // boolean for checking if music service is bound
    private MusicService mServ; // create music service
    private boolean started; // check if the music service has been started




    private ServiceConnection Scon =new ServiceConnection(){


        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService(); // get music service
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        } //music service is null if dissconected
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true; //create intent for music service and bind it
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false; //unbind music service
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //set window size

        setContentView(R.layout.activity_main);
        doBindService();
        Intent music = new Intent(); //music intent
        music.setClass(this, MusicService.class);

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() { //if home button pressed
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic(); //pause music
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic(); //pause music
                }
            }
        });
        mHomeWatcher.startWatch();
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() { //if play clicked

            @Override
            public void onClick(View view) {
                Intent play = new Intent(MainActivity.this, GameActivity.class); //create intent for game activity
                play.putExtra("name", name); //pass through name entered into game activity
                startActivity(play); // start game activity

            }
        });
        findViewById(R.id.hiscores).setOnClickListener(new View.OnClickListener() { //if highscores clicked

            @Override
            public void onClick(View view) {
                Intent play = new Intent(MainActivity.this, highScores.class); //create intent for highscores activty
                startActivity(play); //start highscore activty
            }
        });
        findViewById(R.id.howtoplay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InstructionActivity.class)); //create and start intent for how to play

            }
        });
        findViewById(R.id.entername).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //create a dialog box for user to enter name
                builder.setTitle("Enter name for Highscores"); //set title for dialog box

// Set up the input
                final EditText input = new EditText(MainActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name = input.getText().toString(); //convert into to a string and set as name to be passed through
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); //cancel dialog if cancel pressed
                    }
                });

                builder.show();

            }
        });




        final SharedPreferences pref = getSharedPreferences("game", MODE_PRIVATE); //create shared preferences for game
        isMuted = pref.getBoolean("isMuted", true); // set default is muted value as true in preferences


        final ImageView volumeCtrl = findViewById(R.id.volCtrl); // set image with the id found from the xml
        if (isMuted){ // if muted
            volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp); // draw volume off
            if (started){
                mServ.stopMusic();} //if music service has been started stop the music
        }
        else {
            volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp); //othwise draw volume on
            startService(music); // start music service
            started = true; // set started to true
        }



                volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // if volume button is clicked

                isMuted = !isMuted; //flip the boolean
                if (isMuted){
                    volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
                    mServ.pauseMusic();} //if muted then pause muisic
                else{
                    volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);
                    mServ.resumeMusic(); //otherwise resume music
                }

                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isMuted", isMuted); // update the boolean in preferences
                editor.apply();

            }
        });

    }


    @Override
    protected void onResume() { //resume music
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }

    }

    @Override
    protected void onDestroy() { //stop music
        super.onDestroy();

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);

    }
    @Override
    protected void onPause() { //pause music
        super.onPause();

        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false; //pause music if screen is off
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }

    }

}
