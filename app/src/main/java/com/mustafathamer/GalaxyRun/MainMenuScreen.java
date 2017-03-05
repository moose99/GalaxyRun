package com.mustafathamer.GalaxyRun;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Screen;
import com.mustafathamer.framework.Input.TouchEvent;

import java.util.List;

/**
 * Created by Mus on 11/23/2016.
 * <p>
 * Notice that here we also have the three methods: update, paint, and backButton.
 * <p>
 * This is the screen on which we will run our game.
 */

public class MainMenuScreen extends Screen
{
    public MainMenuScreen(Game game)
    {
        super(game);
    }

    //
    // We have also defined an inbounds method that allows
    // us to check if the user touches inside a rectangle. If the user touches the Play button, we
    // open the GameScreen. If the user touches the back button, the game exits.
    //
    @Override
    public void update(float deltaTime)
    {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP)
            {
                game.setScreen(new GameScreen(game));
                break;
            }
        }
    }


    //
    // In this method, we paint the menu Image.
    //
    @Override
    public void paint(float deltaTime)
    {
        Graphics g = game.getGraphics();
        g.drawImage(Assets.menu, 0, 0);
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void backButton()
    {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
