package com.mustafathamer.framework;

/**
 * Created by Mus on 10/22/2016.
 */

public interface Image
{
    int getWidth();
    int getHeight();
    Graphics.ImageFormat getFormat();
    void dispose();
}
