package com.findemes.util;

//import android.graphics.Color;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

public class RandomColourGenerator {

    public RandomColourGenerator() {} //TODO TERMINAR ESTO

    public int generar() {

        Random random = new Random();
        float hue = random.nextFloat();
        float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
        float luminance = 1.0f; //1.0 for brighter, 0.0 for black
        float[] hsv = {hue, saturation, luminance};
        int color = Color.HSVToColor(hsv);

        return color;

    }

}
