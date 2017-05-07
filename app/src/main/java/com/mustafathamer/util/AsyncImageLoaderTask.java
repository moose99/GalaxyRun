package com.mustafathamer.util;

import android.os.AsyncTask;

import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Image;

/**
 * Created by Mus on 1/21/2017.
 * Call this from the main thread like this:
 AsyncTask<String, Void, Image> imgLoader = new AsyncImageLoaderTask(g,
    new ImageLoaderCallbackInterface()
    {
        public void onImageLoadFinished(Image img)
        {   // use loaded image here
            Assets.bgImg2 = img;
        }
    }
 );
 imgLoader.execute(fileName);
 */

public class AsyncImageLoaderTask extends AsyncTask<String /*fileName*/, Void /* progress type */, Image /* result type */>
{
    private Graphics g;
    private ImageLoaderCallbackInterface callback;

    public AsyncImageLoaderTask(Graphics g, ImageLoaderCallbackInterface callback)
    {
        this.g = g;
        this.callback = callback;
    }

    // results of this step will be passed to OnPostExecute
    @Override
    protected Image doInBackground(String... params)
    {
        String fileName = params[0];
//        Log.i("MOOSE", "doInBackground: filename=" + fileName);
        return g.newImage(fileName, Graphics.ImageFormat.RGB565);
    }

    // invoked on the UI thread after the background computation finishes.
    // The result of the background computation is passed to this step as a parameter.
    @Override
    protected void onPostExecute(Image img)
    {
        callback.onImageLoadFinished(img);

    }

//    public void setGraphics(Graphics g) { this.g = g; }
}