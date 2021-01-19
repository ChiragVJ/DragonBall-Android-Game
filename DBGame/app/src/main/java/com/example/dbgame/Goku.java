package com.example.dbgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;


/**
 * @author chirag vijay 27009630
 * Class for Goku
 */
public class Goku extends Object {
    public boolean isGoingUp = false;

    public int toShoot = 0;
    int x, y, width, height; //position and size
    Bitmap goku1, goku2,goku3, goku4, goku5, goku6, shoot1 , shoot2 , shoot3, gokuDeath1, gokuDeath2; //images
    private GameView gameView; //create gameview
    Goku(GameView gameview, Resources res, int xPos, int yPos, int w, int h){
        super(xPos, yPos, w, h);
        x = xPos;
        y = yPos;
        width = w;
        height = h;
        this.gameView = gameview; //assign gameview
        goku1 = BitmapFactory.decodeResource(res,R.drawable.goku1); //get imagees
        goku2 = BitmapFactory.decodeResource(res,R.drawable.goku16);
        goku3 = BitmapFactory.decodeResource(res, R.drawable.goku21);
        goku4 = BitmapFactory.decodeResource(res, R.drawable.goku22);
        goku5 = BitmapFactory.decodeResource(res, R.drawable.goku31);
        goku6 = BitmapFactory.decodeResource(res, R.drawable.goku32);





        gokuDeath1 = BitmapFactory.decodeResource(res, R.drawable.goku1death); //scale death images
        gokuDeath2 = BitmapFactory.decodeResource(res, R.drawable.goku2death);





        shoot1 = BitmapFactory.decodeResource(res, R.drawable.goku13); //get shoot images
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.goku33);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.goku43);


        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false); //scale shoot images
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);






        goku1 = Bitmap.createScaledBitmap(goku1, width, height, false); //scale other images
        goku2 = Bitmap.createScaledBitmap(goku2, width, height, false);
        goku3 = Bitmap.createScaledBitmap(goku3, width, height, false);
        goku4 = Bitmap.createScaledBitmap(goku4, width, height, false);
        goku5 = Bitmap.createScaledBitmap(goku5, width, height, false);
        goku6 = Bitmap.createScaledBitmap(goku6, width, height, false);
        gokuDeath1 = Bitmap.createScaledBitmap(gokuDeath1, width*2, height, false);
        gokuDeath2 = Bitmap.createScaledBitmap(gokuDeath2, width, height, false);




    }

    /**
     * used to get correct im
     * @param level - the current level of the game
     * @return
     */
  Bitmap getGoku(int level) {

        if (toShoot!=0 && level == 1){ //if level is one draw bullet and return shoot animaton for goku on  first level
            toShoot--;
            gameView.newAttack();
                return shoot1;
            }
        if (toShoot!=0 && level == 2){//if level is two draw bullet and return shoot animaton for goku on  first level
            toShoot--;
            gameView.newAttack();
                return shoot2;
            }
       if (toShoot!=0 && level == 3){//if level is three draw bullet and return shoot animaton for goku on  first level
           toShoot--;
           gameView.newAttack();
        return shoot3;
        }



        if (level == 1){
            if (isGoingUp == true) { //if level is one and goku is flying return the correct goku image

                return goku1;
            } else {
                return goku2; // otherwise if goku is falling return the correct goku image
            }}
        if (level==2){
            if (isGoingUp == true) {  //if level is two  and goku is flying return the correct goku image
                return goku3;}
            else {
                return goku4; // otherwise if goku is falling return the correct goku image
            }
    }
        if (level==3) {
            if (isGoingUp == true) { //if level is three and goku is flying return the correct goku image
                return goku5;
            } else {
                return goku6; // otherwise if goku is falling return the correct goku image
            }

        }
        return goku1; //pointless return
    }


    /**
     *  getter used to check if two rectangles surrounding images are colliding
     * @return returns the collisions rect
     */


    /**
     * getter used to get death image
     * @return returns goku death image
     */
    Bitmap getDeath1 (){
        return gokuDeath1;
    }

    /**
     * getted used to get death image
     * @return returns goku death image
     */
    Bitmap getDeath2 () {return gokuDeath2;}
    Rect getCollShape(){
        return new Rect(x, y, x + width, y + height);
    }
}
