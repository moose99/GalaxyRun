package com.mustafathamer.framework.implementation;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

import com.mustafathamer.framework.Input;

/*
 * Created by Mus on 11/5/2016.
 */

public class AndroidInput implements Input
{
    private TouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY)
    {
        //
        // In Android 2.0, multi-touch was introduced. So in this implementation, we check the SDK
        // build version (5 equates to Android 2.0) and implement the appropriate touch handler.
        //
        if (VERSION.SDK_INT < 5)
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
    }

    @Override
    public boolean isTouchDown(int pointer) { return touchHandler.isTouchDown(pointer);}

    @Override
    public int getTouchX(int pointer)   { return touchHandler.getTouchX(pointer); }

    @Override
    public int getTouchY(int pointer)   { return touchHandler.getTouchY(pointer); }

    @Override
    public List<TouchEvent> getTouchEvents()    {return touchHandler.getTouchEvents(); }

}
