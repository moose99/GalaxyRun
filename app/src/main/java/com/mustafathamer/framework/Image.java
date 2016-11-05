package com.mustafathamer.framework;

import com.mustafathamer.framework.Graphics.ImageFormat;

/*
 * Created by Mus on 10/22/2016.
 */

public interface Image
{
    int getWidth();
    int getHeight();
    ImageFormat getFormat();
    void dispose();
}
