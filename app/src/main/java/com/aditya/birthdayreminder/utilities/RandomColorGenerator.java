package com.aditya.birthdayreminder.utilities;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by Vasudha G on 01-04-2018.
 */

public class RandomColorGenerator {

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
