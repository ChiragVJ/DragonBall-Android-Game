
package com.example.dbgame;

        import android.content.res.Resources;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;

/**
 * @author chirag vijay 27009630
 * Class for lives
 */

public class Lives extends Object {

    int x , y, width, height; //x and y dimensions and width and height
    Bitmap life,  emptyLife; // bitmap for heart and empty heart images
    Lives(Resources res, int xPos, int yPos, int w, int h){
        super(xPos,yPos,w,h); //create object with abstract class
        x = xPos; //set x pos
        y = yPos; //set y pos
        width = w; //set width
        height = h; //set width


        life = BitmapFactory.decodeResource(res, R.drawable.heart); //grab images

        emptyLife = BitmapFactory.decodeResource(res, R.drawable.empty);
        life = Bitmap.createScaledBitmap(life, width, height, false); // scale images

        emptyLife = Bitmap.createScaledBitmap(emptyLife, width, height, false);



    }



}