package com.mustafathamer.RobotAndroid;

import android.provider.MediaStore;
import android.util.Log;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Graphics.ImageFormat;
import com.mustafathamer.framework.Image;
import com.mustafathamer.framework.Screen;

/**
 * Created by Mus on 11/23/2016.
 * <p>
 * All screen classes have three important classes.
 * The update() method, and the paint() method, and the backButton() method
 * (which is called when the user presses the back button in the game).
 * In the update() method, you load all the resources that you will use in the game
 * (i.e. all the resources that we have created in the Assets class).
 * <p>
 * This screen is almost identical to the SplashLoadingScreen, except we load many more assets.
 * As long as it takes to load these assets, our game will call the paint() method, in which we
 * draw the splash screen image we loaded in the splash loading screen.
 */

public class LoadingScreen extends Screen
{
    public LoadingScreen(Game game)
    {
        super(game);
    }

    boolean loaded = false;

    @Override
    public void update(float deltaTime)
    {
        if (!loaded)
        {
            Graphics g = game.getGraphics();

            int hwAudioSampleRate = game.getAudio().getBestSampleRate();
            Log.i("MOOSE", "hwAudioSampleRate: " + hwAudioSampleRate);

            Assets.loadBackground(g);
            Assets.loadPlayer(g, game);
            Assets.loadTileMap(g);
            Assets.loadLargeRocks(g);
            Assets.loadMediumRocks(g);
            Assets.loadSmallRocks(g);
            Assets.loadProjectiles(g, game);

            Assets.menu = g.newImage("menu.png", ImageFormat.RGB565);
            Assets.button = g.newImage("button.jpg", ImageFormat.RGB565);

            loaded = true;
        }
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void paint(float deltaTime)
    {
        Graphics g = game.getGraphics();
        // TODO - change splash
        // g.drawImage(Assets.splash, 0, 0);
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
