package com.mustafathamer.RobotAndroid;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Graphics.ImageFormat;
import com.mustafathamer.framework.Screen;

/**
 * Created by Mus on 11/23/2016.
 *
 * All screen classes have three important classes.
 * The update() method, and the paint() method, and the backButton() method
 * (which is called when the user presses the back button in the game).
 * In the update() method, you load all the resources that you will use in the game
 * (i.e. all the resources that we have created in the Assets class).
 * We would not need anything in our paint() method, unless you would like to
 * have a background loading image while the game loads these resources.
 */

public class LoadingScreen extends Screen
{
    public LoadingScreen(Game game)
    {
        super(game);
    }

    //
    // Initialize Assets
    //
    @Override
    public void update(float deltaTime)
    {
        Graphics g = game.getGraphics();
        Assets.menu = g.newImage("menu.png", ImageFormat.RGB565);
//        Assets.click = game.getAudio().createSound("explode.ogg");

        game.setScreen(new MainMenuScreen(game));
    }


    @Override
    public void paint(float deltaTime)
    {

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

    }
}
