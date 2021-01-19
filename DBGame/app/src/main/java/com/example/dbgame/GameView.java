package com.example.dbgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author chirag vijay 27009630
 * Class for GameView
 */
public class GameView extends SurfaceView implements Runnable {

    private Thread thread; // First thread
    private boolean isPlaying, isGameOver = false; // booleans for playing  and if game over
    private int level = 1, livesCount = 5; // ints for level and lives
    String highscoreName; //string to store highscore name which is entered in main menu
    private Lives lives; // lives object
    private int screenX, screenY, score = 0; //screen x and y lengths and score int
    private Goku goku; // goku object (main  character)
    private GameActivity activity; // game activity object
    private List<Attack> attacks; // attacks list
    private List<Enemies> enemies; // enemies list
    private Random random; //using random
    private SoundPool soundPool; // for sound effects
    private int kiblast, spiritbomb, kamehameha, kaix10; // all sound effects
    private SharedPreferences pref; //local data storage
    private Accel sensor; // accelerometer
    public static float screenRatioX, screenRatioY; // ratios to scale for different resolutions
    private Paint paint; // paint to draw on screen
    private Background background1;// background2, background3; // all 3 backgrounds for the different levels
    @RequiresApi(api = Build.VERSION_CODES.KITKAT) //needed for firebase database to work
    public GameView(GameActivity activity, int screenX, int screenY, String name) {

        super(activity); //gameview created from surfaceview
        sensor = new Accel(activity); //initialise accelerometer passing activity as context

        highscoreName = name; //asigning name
        pref = getContext().getSharedPreferences("game", Context.MODE_PRIVATE);
        this.activity = activity; // initialising
        this.screenX = screenX; //initialising
        this.screenY = screenY;// initialising
        screenRatioX = 1920f /screenX; //screen ratio X is 1920 divided by screen size of device
        screenRatioY = 1080f / screenY;  //screen ratio Y is 1080 divided by screen size of device
        paint = new Paint(); // initalise paint
        paint.setTextSize(128); //set paint text size
        paint.setColor(Color.BLACK); // set paint text colour
        lives = new Lives(getResources(), (int) (40 * screenRatioX), (int) (40 * screenRatioY), (int) (120 * screenRatioX), (int) (140 * screenRatioY)); //grab lives image


        random = new Random(); //initalise random
        background1 = new Background(screenX, screenY, getResources()); // grab background images
  /*      background2 = new Background(screenX, screenY, getResources());
        background3 = new Background(screenX, screenY, getResources());
*/
        attacks = new ArrayList<>();  //initalise arraylists
        enemies = new ArrayList<>();

        goku = new Goku(this, getResources(), (int) (64 * screenRatioX), screenY / 2, (int) (160 * screenRatioX), (int) (240 * screenRatioY)); //initialise goku object passing coordinates and size for abstract class

        Enemies yamcha = new Enemies(getResources(), (int) (64 * screenRatioX), -(int) (240 * screenRatioY), (int) (160 * screenRatioX), (int) (240 * screenRatioY)); //  initialise enemies objects passing coordinates and size for abstract class
        Enemies krillin = new Enemies(getResources(), (int) (64 * screenRatioX), -(int) (240 * screenRatioY), (int) (150 * screenRatioX), (int) (210 * screenRatioY));
        Enemies popo = new Enemies(getResources(), (int) (64 * screenRatioX), -(int) (240 * screenRatioY), (int) (160 * screenRatioX), (int) (240 * screenRatioY));
        enemies.add(0, yamcha); // add the enemies to the arraylist of enemies so positions can be manipulated
        enemies.add(1, krillin);
        enemies.add(2, popo);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //sound initialisation

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        kiblast = soundPool.load(activity, R.raw.kiblast, 1); // all sound affects for shooting
        spiritbomb = soundPool.load(activity, R.raw.spiritbomb, 1);
        kamehameha = soundPool.load(activity, R.raw.kamehamha, 1);
        kaix10 = soundPool.load(activity, R.raw.kaix10, 1);


    }

    /**
     * run for the main gameThread- responsible for updating, drawing and running the second thread.
     */
    @Override
    public void run() { // main  thread running the game
        while (isPlaying) {
            update(); //update positions
            draw(); //draw game
            frameRateThread(); // run second thread responible for frame rate settings
        }
    }

    /**
     * second thread responsible for refreshing the first thread every 17ms at 60fps
     */
 private void frameRateThread(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sleep(); // seperate thread to sleep every 17 ms for 60 fps

            }
        };
        Thread frameThr = new Thread(runnable);
        frameThr.start();
    }


    /**
     * responsible for the logic of the game, collisions and moving characters
     */
    private void update() {

        if (sensor.tilt <= -1) { //if phone tilted to left
            if (goku.x <= 0) { //if goku isnt on the left edge
            } else {
                goku.x -= 20 * screenRatioX; //move to the left by 20
            }
        }
        if (sensor.tilt >= 1) { // if phone tilted to right
            if (goku.x >= screenX / 2) { // if goku isnt passed half of the screen (making it more than screenx/2 would make the objective of the game wayy to hard)

            } else {
                goku.x += 20 * screenRatioX; //move goku to the right
            }
        }

        if (goku.isGoingUp) {
            goku.y -= 30 * screenRatioY; //if goku is going up move goku up by 30* screenratio

        } else goku.y += 30 * screenRatioY; //otherwise move him down by 30*screenratio
        if (goku.y < 0) { //make sure that goku doesn't drop out of the screen in the y axis
            goku.y = 0;

        }
        if (goku.y > screenY - goku.height) {
            goku.y = screenY - goku.height; //make sure goku doesn't move up too high out of screen in the y axis
        }
        List<Attack> trash = new ArrayList<>(); // trash catcher for attacks

        for (int i = 0; i < attacks.size(); i++) { // for every attack in the attack arraylist
            Attack attack = attacks.get(i);
            if (attack.x > screenX) {
                trash.add(attack); // if it out of the screen add it to the trash arraylist

            }

            attack.x += 50 * screenRatioX; // otherwise make the attacks move at 50 pixels * screen ratio per 17ms
            for (int j = 0; j < enemies.size(); j++) { //for every enemy in the enemies arraylist
                Enemies enemy = enemies.get(j);


                if (Rect.intersects(enemy.getCollShape(), attack.getCollShape())) { //if the enemy touches an attack
                    enemy.hit++; // raise the enemy hit counter so we know the enemy has been hit
                    attack.x = screenX + 500; //move the attack out of the screen as the enemy has been hit so it can be put in the trash catcher
                    enemy.wasShot = true; // note that the enemy was shot


                    if (level == 1 && enemy.hit == 1) { // as level one requires 1 hit to kill enemies , if an enemy is hit

                        score++; //increase the score
                        enemy.hit = 0; //reset the hit counter


                        attack.x = screenX + 500; //move the attack out of the screen for trash catching
                        enemy.x = -500; //move the enemy out of the screen
                        attack.x = screenX + 500; //move the attack out of the screen for trash catching
                        enemy.wasShot = true; // note that the enemy was shot

                    }

                    if (level == 2 && enemy.hit == 2) {   // as level one requires 2 hits to kill enemies , if an enemy is hit twice

                        // do the same thing as level one
                        score++;
                        enemy.hit = 0;

                        attack.x = screenX + 500;
                        enemy.x = -500;
                        attack.x = screenX + 500;
                        enemy.wasShot = true;

                    }

                    if (level == 3 && enemy.hit == 3) { // as level one requires 3 hits to kill enemies , if an enemy is hit thrice

                        // do the same thing as level 2
                        score++;
                        enemy.hit = 0;

                        attack.x = screenX + 500;
                        enemy.x = -500;
                        attack.x = screenX + 500;
                        enemy.wasShot = true;

                    }
                    if (score == 10 && enemy.hit == 0) { //change the level to 2 if the score is 10 and no enemeies have been hit (fixes bug where score is stuck at 10)
                        level = 2; // change the level to 2
                        enemies.clear(); //clear the enemies arraylist
                        attacks.clear(); //clear the attacks arraylist

                        Enemies frieza = new Enemies(getResources(), (int) (64 * screenRatioX), -(int) (240 * screenRatioY), (int) (210 * screenRatioX), (int) (240 * screenRatioY)); //initalise new objects of new enemies for level 2 using the abstract class
                        Enemies cell = new Enemies(getResources(), (int) (64 * screenRatioX), -(int) (240 * screenRatioY), (int) (160 * screenRatioX), (int) (240 * screenRatioY));
                        Enemies buu = new Enemies(getResources(), (int) (64 * screenRatioX), -(int) (240 * screenRatioY), (int) (170 * screenRatioX), (int) (240 * screenRatioY));
                        enemies.add(0, frieza); //add these new enemies to the arraylist
                        enemies.add(1, cell);
                        enemies.add(2, buu);
                        if (livesCount != 5) // if the lives arent already maxed, give the user an extra life
                            livesCount++;
                        if (!pref.getBoolean("isMuted", false)) {
                            soundPool.play(kaix10, 1, 1, 0, 0, 1); // sound affect for goku upgrade


                        }

                        break;
                    }
                    if (score == 15 && enemy.hit == 0) { //change the level to 3 if the score is 15 and no enemeies have been hit (fixes bug where score is stuck at 15)
                        level = 3;
                        enemies.clear();
                        attacks.clear();
                        Enemies toppo = new Enemies(getResources(), (int) (64 * screenRatioX), -(int) (240 * screenRatioY), (int) (135 * screenRatioX), (int) (213 * screenRatioY));  //initalise new objects of new enemies for level 3 using the abstract class
                        Enemies beerus = new Enemies(getResources(), (int) (64 * screenRatioX), -(int) (240 * screenRatioY), (int) (140 * screenRatioX), (int) (200 * screenRatioY));
                        Enemies jiren = new Enemies(getResources(), (int) (64 * screenRatioX), -(int) (240 * screenRatioY), (int) (180 * screenRatioX), (int) (260 * screenRatioY));
                        enemies.add(0, toppo);  //add these new enemies to the arraylist
                        enemies.add(1, beerus);
                        enemies.add(2, jiren);

                        if (livesCount != 5) // if the lives arent already maxed, give the user an extra life
                            livesCount++;

                        break;

                    }


                }
            }
        }
        for (Attack attack : trash) { // for every attack in the trash
            attacks.remove(attack); //clear the trash
        }
        for (Enemies enemy : enemies) { //for every enemy in enemies
            enemy.x -= enemy.speed; //adjust the movement of the enemy depending on the speed
            if (enemy.x + enemy.width < 0) { // if the enemy goes past goku and touches the left edge
                if (!enemy.wasShot) { //if the enemy was not shot
                    enemy.wasShot = true; //say its been shot so a new enemy can be spawned

                    if (level == 1) { // if level is 1
                        enemy.wasShot = true;  //say its been shot so a new enemy can be spawned
                        livesCount--; //reduce the lives by 1
                    }
                    if (level == 2) { //if level is 2
                        enemy.wasShot = true; //say its been shot so a new enemy can be spawned
                        livesCount = livesCount - 2; // reduce the lives by 2
                    }
                    if (level == 3) { // if the level is 3
                        enemy.wasShot = true; //say its been shot so a new enemy can be spawned
                        livesCount = livesCount - 3; // reduce the lives by 3
                    }


                    if (livesCount <= 0) { //if no lives left then
                        isGameOver = true; // end the game
                    }
                    return;
                }
                int bound = (int) (30 * screenRatioX); //max speed enemeis can travel at
                enemy.speed = random.nextInt(bound); // speed of the enemies is random between 10 and 30
                if (enemy.speed < 10 * screenRatioX) { //if speed of the enemies is too low
                    enemy.speed = (int) (10 * screenRatioX); // increase it

                }
                enemy.x = screenX; // enemies start at right hand side of the screen
                enemy.y = random.nextInt(screenY - enemy.height); // enemies spawn at random heights
                enemy.wasShot = false; // enemies not shot


            }
            if (Rect.intersects(enemy.getCollShape(), goku.getCollShape())) { // if enemy touchs goku
                enemy.x = -500; //move the enemy out of the screen
                enemy.wasShot = true; // say that the enemy was shot so a new one can be spawned
                if (level == 1)  { // if the level is one
                    enemy.wasShot = true; // say enemy is shot
                    livesCount--; //reduce lives by 1
                }
                if (level == 2) { //if level is 2
                    enemy.wasShot = true;
                    livesCount = livesCount - 2; //reduce lives by  2
                }
                if (level == 3) { // if level is 3
                    enemy.wasShot = true;
                    livesCount = livesCount - 3; // reduce lives by 3
                }


                if (livesCount <= 0) { // if lives is less than 0 or 0 , end game
                    isGameOver = true;
                }
                return;
            }
        }
    }

    /**
     * draws images onto screen
     */
    private void draw() {
        try{//draw onto screen
                Canvas canvas = getHolder().lockCanvas(); //lock canvas
                if (getHolder().getSurface().isValid()) { // if surface holder is value
                    if (level == 1) { //if level is 1


                        canvas.drawBitmap(background1.background, background1.x, background1.y, paint); //draw background
                        canvas.drawBitmap(enemies.get(0).getYamcha(), enemies.get(0).x, enemies.get(0).y, paint); // draw level 1 enemies
                        canvas.drawBitmap(enemies.get(1).getKrillin(), enemies.get(1).x, enemies.get(1).y, paint);
                        canvas.drawBitmap(enemies.get(2).getPopo(), enemies.get(2).x, enemies.get(2).y, paint);
                        if (livesCount == 5) { //draw the lives depending on how many lives are left

                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 4) {
                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 3) {
                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 2) {
                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 1) {
                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 0) {
                            canvas.drawBitmap(lives.emptyLife, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }

                        canvas.drawText(score + "", screenX / 2f, 165, paint); //draw the score


                        canvas.drawText("Level 1", screenX - 500, 165, paint); //draw the level

                        if (isGameOver) { // if game is over
                            isPlaying = false; //not game is not being played
                            canvas.drawBitmap(goku.getDeath1(), goku.x, goku.y, paint); //draw goku death for level 1
                            getHolder().unlockCanvasAndPost(canvas); //unlock the canvas
                            saveScore(); // save the highscore to firebase
                            waitBeforeExiting(); //restart game after 3 seconds so everything can be reloaded

                            return;
                        }

                        canvas.drawBitmap(goku.getGoku(1), goku.x, goku.y, paint); // draw goku
                        for (Attack attack : attacks) {
                            canvas.drawBitmap(attack.attack1, attack.x, attack.y, paint); //draw the attacks
                        }
                        getHolder().unlockCanvasAndPost(canvas); //unlock canvas for next level
                    } else if (level == 2) { //moving on to level 2

                        canvas.drawColor(0, PorterDuff.Mode.CLEAR); //clear the screen
                        canvas.drawBitmap(background1.background, background1.x, background1.y, paint); //draw the second background
                        canvas.drawBitmap(enemies.get(0).getFrieza(), enemies.get(0).x, enemies.get(0).y, paint); //draw enemies for level 2
                        canvas.drawBitmap(enemies.get(1).getCell(), enemies.get(1).x, enemies.get(1).y, paint);
                        canvas.drawBitmap(enemies.get(2).getBuu(), enemies.get(2).x, enemies.get(2).y, paint);
                        if (livesCount == 5) { //draw the lives depending on how many lives are left

                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 4) {
                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 3) {
                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 2) {
                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 1) {
                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 0) {
                            canvas.drawBitmap(lives.emptyLife, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }


                        canvas.drawText(score + "", screenX / 2f, 165, paint); //draw the score
                        canvas.drawText("Level 2", screenX - 500, 165, paint); //draw level 2
                        if (isGameOver) { //if game ends
                            isPlaying = false; //note game is not being played
                            canvas.drawBitmap(goku.getDeath2(), goku.x, goku.y, paint); //draw goku level 2 death
                            getHolder().unlockCanvasAndPost(canvas); //unlock canvas ready for reload
                            saveScore(); //save highscore in firebase database
                            waitBeforeExiting(); // reload game after 3 seconds

                            return;
                        }


                        canvas.drawBitmap(goku.getGoku(2), goku.x, goku.y, paint); //draw goku
                        for (Attack attack : attacks) {
                            canvas.drawBitmap(attack.attack2, attack.x, attack.y, paint); //draw level 2 attacks
                        }
                        getHolder().unlockCanvasAndPost(canvas); //unlock canvas ready for next level
                    } else if (level == 3) { //move onto next level


                        canvas.drawColor(0, PorterDuff.Mode.CLEAR); //clear screen
                        canvas.drawBitmap(background1.background, background1.x, background1.y, paint); //draw background for third level
                        canvas.drawBitmap(enemies.get(0).getToppo(), enemies.get(0).x, enemies.get(0).y, paint); //draw enemies for third level
                        canvas.drawBitmap(enemies.get(1).getBeerus(), enemies.get(1).x, enemies.get(1).y, paint);
                        canvas.drawBitmap(enemies.get(2).getJiren(), enemies.get(2).x, enemies.get(2).y, paint);
                        if (livesCount == 5) { //draw the lives depending on how many lives are left

                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 4) {
                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 3) {
                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 2) {
                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.life, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 1) {
                            canvas.drawBitmap(lives.life, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }
                        if (livesCount == 0) {
                            canvas.drawBitmap(lives.emptyLife, lives.x, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 170, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 340, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 510, lives.y, paint);
                            canvas.drawBitmap(lives.emptyLife, lives.x + 680, lives.y, paint);
                        }


                        canvas.drawText(score + "", screenX / 2f, 165, paint); //draw score
                        canvas.drawText("Level 3", screenX - 500, 165, paint); //draw level 3
                        if (isGameOver) { //if game is over
                            isPlaying = false; //not game is no being played
                            canvas.drawBitmap(goku.getDeath1(), goku.x, goku.y, paint); //draw death for level 3
                            getHolder().unlockCanvasAndPost(canvas); //unlock canvas for reload
                            saveScore(); //save score on firebase database


                            return;
                        }


                        canvas.drawBitmap(goku.getGoku(3), goku.x, goku.y, paint); // draw goku
                        for (Attack attack : attacks) {
                            canvas.drawBitmap(attack.attack3, attack.x, attack.y, paint); // draw level 3 attack
                        }
                        getHolder().unlockCanvasAndPost(canvas); //unlock canvas for reload
                    }

                }}
        catch(Exception e){ //sometimes surface doesn't get unlocked so this catches it and re runs
            run();

        }
    }

    /**
     * pause the game for 3 seconds before restarting
     */
    private void waitBeforeExiting() {
        try {
            Thread.sleep(3000); //sleep for 3 seconds
            activity.startActivity(new Intent(activity, MainActivity.class)); //restart game
            isPlaying = false;
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * saves score to realtime firebase database and local shared preferences
     */
    private void saveScore() {
        if (pref.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
        FirebaseDatabase rootNode; //create firebase node
        DatabaseReference reference; //create firebase reference
        rootNode = FirebaseDatabase.getInstance(); //get database instance from node
        reference = rootNode.getReference("Highscores"); // assign highscores to reference
        if (score != 0){
        reference.push().setValue(highscoreName + ": " + score);} // add score to database
        waitBeforeExiting(); // restart game


    }

    /**
     * used to refresh the thread every 17 ms fo 60 fps
     */
    private void sleep() {
        try {
            Thread.sleep(17); //frame refrash every 17ms for 60 fps
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * used to resume the game if paused by restarting the thread
     */
    public void resume() {
        isPlaying = true; //resume game
        thread = new Thread(this); //start a new thread using this gameview
        thread.start(); //start the thread
    }

    /**
     * pause the game by joining threads
     */
    public void pause() {
        try {
            isPlaying = false; //pause game
            thread.join(); //join threads
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * used for touch sensing
     * @param event - motion event
     * @return - always returns true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //if screen touched on the left hand side
                if (event.getX() < screenX / 2) {
                    goku.isGoingUp = true; //move goku up
                }
                break;
            case MotionEvent.ACTION_UP: //if let go
                goku.isGoingUp = false;
                if (event.getX() > screenX / 2) { //move goku down
                    goku.toShoot = 1;
                }
                break;
        }
        return true;

    }

    /**
     * generates sound effects depending on which attack is shot- and does the logic for the attack travelling
     */
    public void newAttack() {
        if (level == 1) { // if level is 1
            if (!pref.getBoolean("isMuted", false)) {
                soundPool.play(kiblast, 1, 1, 0, 0, 1); //everytime goku shoots play this sound
            }

        } else if (level == 2) { // if level is 2
            if (!pref.getBoolean("isMuted", false)) {
                soundPool.play(spiritbomb, 1, 1, 0, 0, 1); //everytime goku shoots play this sound
            }
        } else if (level == 3) { //if level is 3
            if (!pref.getBoolean("isMuted", false)) {
                soundPool.play(kamehameha, 1, 1, 0, 0, 1); //everytime goku shoots play this sound
            }
        }
        Attack attack = new Attack(getResources(), (int) (64 * screenRatioX), (int) (64 * screenRatioY), (int) (140 * screenRatioX), (int) (120 * screenRatioY)); //get the attack image
        attack.x = goku.x + goku.width; //when shooting place the starting position of the attack in  front of goku
        attack.y = goku.y + (goku.height / 2); // when shooting place the startin g position of the attack in the middle of goku height wise
        attacks.add(attack); // add attack to attack arraylist
    }


}






