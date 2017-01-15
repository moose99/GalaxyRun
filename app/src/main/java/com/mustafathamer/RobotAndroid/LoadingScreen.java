package com.mustafathamer.RobotAndroid;

import android.util.Log;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Graphics.ImageFormat;
import com.mustafathamer.framework.Screen;

/**
 * Created by Mus on 11/23/2016.
 * <p>
 * All screen classes have three important classes.
 * The update() method, and the paint() method, and the backButton() method
 * (which is called when the user presses the back button in the game).
 * In the update() method, you load all the resources that you will use in the game
 * (i.e. all the resources that we have created in the Assets class).
 *
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

    @Override
    public void update(float deltaTime)
    {
        Graphics g = game.getGraphics();
        Assets.menu = g.newImage("menu.png", ImageFormat.RGB565);
        Assets.background = g.newImage("starfield_bg.png", ImageFormat.RGB565);

        // use ARGB8888 for highest quality, with alpha
        Assets.player = g.newImage("player.png", ImageFormat.ARGB8888);
        Assets.playerLeft = g.newImage("playerLeft.png", ImageFormat.ARGB8888);
        Assets.playerRight = g.newImage("playerRight.png", ImageFormat.ARGB8888);
        Assets.playerDamaged = g.newImage("playerDamaged.png", ImageFormat.ARGB8888);


        Assets.heliboy = g.newImage("heliboy.png", ImageFormat.ARGB4444);
        Assets.heliboy2 = g.newImage("heliboy2.png", ImageFormat.ARGB4444);
        Assets.heliboy3 = g.newImage("heliboy3.png", ImageFormat.ARGB4444);
        Assets.heliboy4 = g.newImage("heliboy4.png", ImageFormat.ARGB4444);
        Assets.heliboy5 = g.newImage("heliboy5.png", ImageFormat.ARGB4444);


        Assets.tiledirt = g.newImage("tiledirt.png", ImageFormat.RGB565);
        Assets.tilegrassTop = g.newImage("tilegrasstop.png", ImageFormat.RGB565);
        Assets.tilegrassBot = g.newImage("tilegrassbot.png", ImageFormat.RGB565);
        Assets.tilegrassLeft = g.newImage("tilegrassleft.png", ImageFormat.RGB565);
        Assets.tilegrassRight = g.newImage("tilegrassright.png", ImageFormat.RGB565);

        Assets.button = g.newImage("button.jpg", ImageFormat.RGB565);

        int hwAudioSampleRate = game.getAudio().getBestSampleRate();
        Log.i("MOOSE", "hwAudioSampleRate: " + hwAudioSampleRate);

        // TODO load audio format based on hwAudioSampleRate, to allow for fast playback (avoid conversion)
        Assets.playerLaser = game.getAudio().createSound("playerLaser_48khz.wav");
        Assets.playerCrash = game.getAudio().createSound("playerCrash.wav");

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
