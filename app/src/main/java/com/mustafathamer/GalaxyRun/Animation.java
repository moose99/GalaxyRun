package com.mustafathamer.GalaxyRun;

import android.graphics.Rect;

import java.util.ArrayList;
import com.mustafathamer.framework.Image;

import static android.R.attr.duration;

/**
 * Created by Mus on 11/26/2016.
 */

public class Animation
{
    private ArrayList frames;
    private int currentFrame;
    private long animTime;
    private long totalDuration;

    public Animation()
    {
        frames = new ArrayList();
        totalDuration = 0;

        synchronized (this)
        {
            animTime = 0;
            currentFrame = 0;
        }
    }

    public synchronized void addFrame(Image image, long duration)
    {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }

    public synchronized void addFrame(Image image, Rect subRect, long duration)
    {
        totalDuration += duration;
        frames.add(new AnimFrame(image, subRect, totalDuration));
    }

    public synchronized void update(long elapsedTime)
    {
        if (frames.size() > 1)
        {
            animTime += elapsedTime;
            if (animTime >= totalDuration)
            {
                animTime = animTime % totalDuration;
                currentFrame = 0;
            }

            while (animTime > getFrame(currentFrame).endTime)
            {
                currentFrame++;

            }
        }
    }

    public synchronized Image getImage()
    {
        if (frames.size() == 0)
        {
            return null;
        }
        return getFrame(currentFrame).image;
    }

    public synchronized Rect getRect()
    {
        if (frames.size() == 0)
        {
            return null;
        }

        return getFrame(currentFrame).subRect;
    }

    private AnimFrame getFrame(int i)
    {
        return (AnimFrame) frames.get(i);
    }

    private class AnimFrame
    {
        Image image;
        long endTime;
        Rect subRect;     // optional image subrect

        public AnimFrame(Image image, long endTime)
        {
            this.image = image;
            this.endTime = endTime;
            this.subRect = null;
        }

        public AnimFrame(Image image, Rect r, long endTime)
        {
            this.image = image;
            this.subRect = r;
            this.endTime = endTime;
        }

    }
}
