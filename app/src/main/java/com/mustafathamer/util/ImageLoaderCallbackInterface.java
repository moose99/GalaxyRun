package com.mustafathamer.util;

import com.mustafathamer.framework.Image;

/**
 * Created by Mus on 5/7/2017.
 */

//define callback interface
public interface ImageLoaderCallbackInterface
{
    void onImageLoadFinished(Image img);
}