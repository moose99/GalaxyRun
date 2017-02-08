package com.mustafathamer.GalaxyRun;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Screen;

/**
 * Created by Mus on 11/26/2016.
 */

public class SplashLoadingScreen extends Screen
{
    public SplashLoadingScreen(Game game)
    {
        super(game);
    }

    //
    // We load our first Image as an RGB565 (which does not support transparency but takes up the
    // least amount of memory). We do not paint anything. As soon as the loading of the splash.jpg
    // is complete, we go straight to the LoadingScreen
    //
    @Override
    public void update(float deltaTime)
    {
        Graphics g = game.getGraphics();
        // TODO load splash image
//        Assets.splash = g.newImage("splash.jpg", ImageFormat.RGB565);

        game.setScreen(new LoadingScreen(game));
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
