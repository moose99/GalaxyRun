package com.mustafathamer.RobotAndroid;

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
            Log.i("MOOSE", "LoadingScreen.update: ");
            Graphics g = game.getGraphics();
            Assets.menu = g.newImage("menu.png", ImageFormat.RGB565);

            // load first 2 backgrounds
            Assets.bgImg1 = g.newImage("Background-1.png", ImageFormat.RGB565);
            Assets.bgImg2 = g.newImage("Background-2.png", ImageFormat.RGB565);

            // use ARGB8888 for highest quality, with alpha
            Assets.player = g.newImage("player.png", ImageFormat.ARGB8888);
            Assets.playerLeft = g.newImage("playerLeft.png", ImageFormat.ARGB8888);
            Assets.playerRight = g.newImage("playerRight.png", ImageFormat.ARGB8888);
            Assets.playerDamaged = g.newImage("playerDamaged.png", ImageFormat.ARGB8888);


            Assets.tileLeft = g.newImage("tileLeft2.png", ImageFormat.RGB565);
            Assets.tileRight = g.newImage("tileRight2.png", ImageFormat.RGB565);

            Assets.button = g.newImage("button.jpg", ImageFormat.RGB565);

            int hwAudioSampleRate = game.getAudio().getBestSampleRate();
            Log.i("MOOSE", "hwAudioSampleRate: " + hwAudioSampleRate);

            // TODO load audio format based on hwAudioSampleRate, to allow for fast playback (avoid conversion)
            Assets.playerLaser = game.getAudio().createSound("playerLaser_48khz.wav");
            Assets.playerCrash = game.getAudio().createSound("playerCrash.wav");

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
