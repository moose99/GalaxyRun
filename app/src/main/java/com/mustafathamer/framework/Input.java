package com.mustafathamer.framework;

import java.util.List;

/**
 * Created by Mus on 10/22/2016.
 * The Input interface keeps track of touch events, with variables x, y, type (touch down, touch up, etc),
 * and pointer (each point of contact on screen
 */

public interface Input
{
    class TouchEvent
    {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;
        public static final int TOUCH_HOLD = 3;

        public int type;
        public int x, y;
        public int pointer;
    }

    boolean isTouchDown(int pointer);

    int getTouchX(int pointer);

    int getTouchY(int pointer);

    List<TouchEvent> getTouchEvents();
}
