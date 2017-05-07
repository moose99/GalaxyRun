package com.mustafathamer.util;

import com.mustafathamer.GalaxyRun.BuildConfig;

/**
 * Created by Mus on 5/7/2017.
 * More reliable assert class - suggested by Android docs
 */

public class Assert
{
    public static void Assert(boolean cond)
    {
       if (BuildConfig.DEBUG && !cond)
       {
           throw new AssertionError();
       }
    }
}
