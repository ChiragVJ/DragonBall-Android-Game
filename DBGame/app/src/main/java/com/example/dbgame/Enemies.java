package com.example.dbgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * @author chirag vijay 27009630
 * Class for Enemies
 */
public class Enemies extends Object {

    public int speed = 20; //default speed
    public boolean wasShot = true; //was shot boolean
    int x = 0 ,y, width,height, hit = 0; //positions and dimensions
    Bitmap yamcha, krillin, popo, frieza, cell, buu, toppo, beerus, jiren ; //all enemy bitmaps

    Enemies(Resources res, int xPos, int yPos, int w, int h){
        super(xPos, yPos, w, h); //create with abstract class
        x = xPos; //set x pos
        y = yPos; //set y pos
        width = w; // set width
        height = h; // set height
        yamcha = BitmapFactory.decodeResource(res, R.drawable.yamcha); //grab all images
        krillin = BitmapFactory.decodeResource(res,R.drawable.krillin);
        popo = BitmapFactory.decodeResource(res, R.drawable. popo);
        frieza = BitmapFactory.decodeResource(res, R.drawable.frieza);
        cell = BitmapFactory.decodeResource(res, R.drawable.cell);
        buu = BitmapFactory.decodeResource(res, R.drawable.buu);
        beerus = BitmapFactory.decodeResource(res, R.drawable.beerus);
        jiren = BitmapFactory.decodeResource(res, R.drawable.jiren);
        toppo = BitmapFactory.decodeResource(res, R.drawable.toppo);



        yamcha = Bitmap.createScaledBitmap(yamcha, width, height, false); //scale all images
        krillin = Bitmap.createScaledBitmap(krillin, width, height, false);
        popo = Bitmap.createScaledBitmap(popo, width, height, false);
        frieza = Bitmap.createScaledBitmap(frieza, width, height/2, false);
        cell = Bitmap.createScaledBitmap(cell, width, height, false);
        buu = Bitmap.createScaledBitmap(buu, width, height, false);
        toppo= Bitmap.createScaledBitmap(toppo, width, height, false);
        beerus= Bitmap.createScaledBitmap(beerus, width, height, false);
        jiren = Bitmap.createScaledBitmap(jiren, width, height, false);


    }


    Bitmap getYamcha() { return yamcha; } // getters for all images
    Bitmap getKrillin(){
        return krillin;
    }
    Bitmap getPopo()
    {
        return popo;
    }
    Bitmap getFrieza()
    {
        return frieza;
    }
    Bitmap getCell()
    {
        return cell;
    }
    Bitmap getToppo()
    {
        return toppo;
    }
    Bitmap getBeerus()
    {
        return beerus;
    }
    Bitmap getJiren()
    {
        return jiren;
    }
    Bitmap getBuu()
    {
        return buu;
    }




    Rect getCollShape(){
        return new Rect(x, y, x + width, y + height);
    } //draws rect around enemy for collisions purposes


}
