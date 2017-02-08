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
 * In addition, we have added an inBounds method that is used to create rectangles
 * with coordinates (x, y, x2, y2).  We use this to create regions in the screen
 * that we can touch to interact with the game (as we do here in the update method).
 * In our case, when the user touches and releases inside the square with side
 * length 250 with a corner at (0, 0), we would call the:
 * game.setScreen(new GameScreen(game));
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
                if (inBounds(event, 50, 350, 250, 450))
                {
                    game.setScreen(new GameScreen(game));
                }

            }
        }
    }

    private boolean inBounds(TouchEvent event, int x, int y, int width,
                             int height)
    {
        if (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1)
            return true;
        else
            return false;
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
