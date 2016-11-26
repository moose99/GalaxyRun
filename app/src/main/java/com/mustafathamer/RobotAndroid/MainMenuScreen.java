package com.mustafathamer.RobotAndroid;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Screen;
import com.mustafathamer.framework.Input.TouchEvent;

import java.util.List;

/**
 * Created by Mus on 11/23/2016.
 *
 * Notice that here we also have the three methods: update, paint, and backButton.
 * In addition, we have added an inBounds method that is used to create rectangles
 * with coordinates (x, y, x2, y2).  We use this to create regions in the screen
 * that we can touch to interact with the game (as we do here in the update method).
 * In our case, when the user touches and releases inside the square with side
 * length 250 with a corner at (0, 0), we would call the:
 * game.setScreen(new GameScreen(game));
 *
 * This is the screen on which we will run our game.
 */

public class MainMenuScreen extends Screen
{
    public MainMenuScreen(Game game)
    {
        super(game);
    }

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
                if (inBounds(event, 0, 0, 250, 250))
                {
                    //START GAME
                    game.setScreen(new GameScreen(game));
                }
            }
        }
    }

    //
    // Determine if the touch even is in the given bounds
    //
    private boolean inBounds(TouchEvent event, int x, int y, int width,
                             int height)
    {
        if (event.x > x && event.x < x + width - 1 &&
                event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }

    //
    // Draws the menu image
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
        //Display "Exit Game?" Box

    }
}
