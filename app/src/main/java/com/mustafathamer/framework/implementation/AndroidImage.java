package com.mustafathamer.framework.implementation;

import android.graphics.Bitmap;

import com.mustafathamer.framework.Image;
import com.mustafathamer.framework.Graphics.ImageFormat;

/*
 * Created by Mus on 11/5/2016.
 */

//
// Wraps Android bitmap
//
class AndroidImage implements Image
{
    Bitmap bitmap;
    private ImageFormat format;

    AndroidImage(Bitmap bitmap, ImageFormat format)
    {
        this.bitmap = bitmap;
        this.format = format;
    }

    @Override    public int getWidth()   { return bitmap.getWidth(); }

    @Override    public int getHeight()  { return bitmap.getHeight(); }

    @Override    public ImageFormat getFormat()  { return format; }

    @Override    public void dispose()   { bitmap.recycle(); }
    @Override    public void resize(int dstWidth, int dstHeight, boolean filter)
    {
        this.bitmap = bitmap.createScaledBitmap(this.bitmap, dstWidth, dstHeight, filter);
    }
}
