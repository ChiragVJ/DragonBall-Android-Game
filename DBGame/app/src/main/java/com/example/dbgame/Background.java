package com.example.dbgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * @author chirag vijay 27009630
 * Class for backgrounds
 */
public class Background extends Object {

    int x = 0, y = 0; //set positions of the images
    Bitmap background;// background1, background2; //create bitmaps to store images

    Background (int screenX, int screenY, Resources res) {

        background = BitmapFactory.decodeResource(res, R.drawable.level1b); //grab images for the background of each level and scale them
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
     /*   background1 = BitmapFactory.decodeResource(res, R.drawable.level2b);
        background1 = Bitmap.createScaledBitmap(background1, screenX, screenY, false);
        background2 = BitmapFactory.decodeResource(res, R.drawable.level3b);
        background2 = Bitmap.createScaledBitmap(background2, screenX, screenY, false);
*/

    }

}