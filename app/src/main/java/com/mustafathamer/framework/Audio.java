package com.mustafathamer.framework;

/**
 * Created by Mus on 10/22/2016.
 * uses the Music and Sound interfaces to create audio objects for use.
 */

public interface Audio
{
    Music createMusic(String file);     // create music interface
    Sound createSound(String file);     // creates sound interface
    int getBestSampleRate();            // returns the HW sample rate
}
