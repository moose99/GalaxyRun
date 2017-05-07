package com.mustafathamer.GalaxyRun;

import android.os.AsyncTask;
import android.util.Log;

import com.mustafathamer.util.AsyncImageLoaderTask;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Image;
import com.mustafathamer.util.ImageLoaderCallbackInterface;

import static com.mustafathamer.GalaxyRun.GameScreen.gameHeight;

/**
 * Created by Mus on 1/22/2017.
 */

public class BackgroundMgr
{
    private Background bg1, bg2;
    private int curBgIdx;
    private boolean loadingBackground;

    public void init()
    {
        // Create backgrounds
        // X,Y specifies where upper left of image goes, with 0,0 at upper left of screen
        int bgY = -(Assets.bgImg2.getHeight() - gameHeight);
        bg2 = new Background(Assets.bgImg2, 0, bgY, 2);

        bgY -= Assets.bgImg1.getHeight();
        bg1 = new Background(Assets.bgImg1, 0, bgY, 1);
        curBgIdx = 2;

        loadingBackground = false;
    }

    public void update(Graphics g, float deltaTime)
    {
        bg1.update();
        bg2.update();

        if (loadingBackground)
            return;

        // check if offscreen and time to load next image

        String fileName="";
        if ( (bg1.getBgY() >= gameHeight) || bg2.getBgY() >= gameHeight)
        {
            curBgIdx += 1;
            if (curBgIdx > Assets.numBackgrounds)
                curBgIdx = 1;
            fileName = "Background-" + curBgIdx + ".png";
            loadingBackground = true;
        }
        if (bg1.getBgY() >= gameHeight)
        {
            // load new img in the background
            AsyncTask<String, Void, Image> imgLoader = new AsyncImageLoaderTask(g,
                    new ImageLoaderCallbackInterface()
                    {
                        public void onImageLoadFinished(Image img)
                        {
                            Log.i("MOOSE", "onImageLoadFinished");
                            Assets.bgImg1 = img;
                            bg1 = new Background(Assets.bgImg1, 0, bg2.getBgY() - Assets.bgImg1.getHeight(), curBgIdx);
                            loadingBackground = false;
                        }
                    }
            );
            Log.i("MOOSE", "updateBackgrounds: imgLoader.execute, fileName=" + fileName);
            imgLoader.execute(fileName);
        }
        else
        {
            if (bg2.getBgY() >= gameHeight)
            {
                // load new img in the background
                AsyncTask<String, Void, Image> imgLoader = new AsyncImageLoaderTask(g,
                        new ImageLoaderCallbackInterface()
                        {
                            public void onImageLoadFinished(Image img)
                            {
                                Log.i("MOOSE", "onImageLoadFinished");
                                Assets.bgImg2 = img;
                                bg2 = new Background(Assets.bgImg2, 0, bg1.getBgY() - Assets.bgImg2.getHeight(), curBgIdx);
                                loadingBackground = false;
                            }
                        }
                );
                Log.i("MOOSE", "updateBackgrounds: imgLoader.execute, fileName=" + fileName);
                imgLoader.execute(fileName);
            }
        }
    }

    public void drawBackgrounds(Graphics g)
    {
        bg1.draw(g);
        bg2.draw(g);
    }

}
