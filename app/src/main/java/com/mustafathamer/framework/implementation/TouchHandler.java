package com.mustafathamer.framework.implementation;

import java.util.List;

import android.view.View.OnTouchListener;

import com.mustafathamer.framework.Input.TouchEvent;


/*
 * Created by Mus on 11/5/2016.
 */

interface TouchHandler extends OnTouchListener
{

    boolean isTouchDown(int pointer);

    int getTouchX(int pointer);

    int getTouchY(int pointer);

    List<TouchEvent> getTouchEvents();
}

