package com.example.dbgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
/**
 * @author chirag vijay 27009630
 * Class for GameActivty
 */
public class GameActivity extends AppCompatActivity{
    private GameView gameView;

    private String name = "Anonymous"; // set name to anonymous


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras(); //get the extra variables passed through the intent
        name = b.getString("name"); // getting the extra string and setting it to name
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Point point = new Point(); // create new point
        getWindowManager().getDefaultDisplay().getSize(point); //get the display size with point


        gameView = new GameView(this, point.x, point.y, name); //pass through screen dimensions, name and activity to gameview

        setContentView(gameView);

    }
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause(); //pause gameview
    }


    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume(); //resume gameview
    }

}

