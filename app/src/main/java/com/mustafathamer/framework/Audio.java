package com.mustafathamer.framework;

/**
 * Created by Mus on 10/22/2016.
 * uses the Music and Sound interfaces to create audio objects for use.
 */

public interface Audio
{
    Music createMusic(String file);

    Sound createSound(String file);
}
