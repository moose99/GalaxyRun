package com.mustafathamer.framework;

/**
 * Created by Mus on 10/22/2016.
 */

public interface Game
{
    Audio getAudio();

    Input getInput();

    FileIO getFileIO();

    Graphics getGraphics();

    void setScreen(Screen screen);

    Screen getCurrentScreen();

    Screen getInitScreen();
}
