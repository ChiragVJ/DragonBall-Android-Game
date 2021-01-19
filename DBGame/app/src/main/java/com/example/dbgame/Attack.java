package com.example.dbgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
/**
 * @author chirag vijay 27009630
 * Class for attcks
 */

public class Attack extends Object {
    int x,y, width, height; // create ints for positions and dimensions
    Bitmap attack1, attack2, attack3; //crate bitmaps for the 3 attack images

    Attack(Resources res, int xPos, int yPos, int w, int h){
        super(xPos, yPos, w, h); //create the attack with the abstract class
        x = xPos; //set x pos
        y = yPos; //set y pos
        width = w; //set width
        height = h; // set height
        attack1 = BitmapFactory.decodeResource(res, R.drawable.goku1bullet); //grab images for each attack
        attack2 = BitmapFactory.decodeResource(res, R.drawable.goku2bullet);
        attack3 = BitmapFactory.decodeResource(res, R.drawable.goku3bullet);



        attack1 = Bitmap.createScaledBitmap(attack1, width, height,false); //scale the attacks to the correct sizes
        attack2 = Bitmap.createScaledBitmap(attack2, width, height,false);
        attack3 = Bitmap.createScaledBitmap(attack3, width*2, height,false);
    }
    Rect getCollShape(){
        return new Rect(x, y, x + width, y + height); //draw rect around attack
    }
}
