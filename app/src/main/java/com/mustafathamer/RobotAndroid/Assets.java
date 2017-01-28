package com.mustafathamer.RobotAndroid;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Music;
import com.mustafathamer.framework.Sound;
import com.mustafathamer.framework.Image;

/**
 * Created by Mus on 11/23/2016.
 * <p>
 * This Assets class is used to create a static variable for each resource
 * that we will use in the game.
 * We will initialize them in the LoadingScreen.
 * We define all the assets that will be used in our game, and also load & play the backgroundImg music at 85% volume.
 */

public class Assets
{
    public static final int numBackgrounds=7;
    public static Image bgImg1, bgImg2;

    public static int numAsteroidImages=16;
    public static Image largeRock1[], largeRock2[];
    public static Image mediumRock1[], mediumRock2[];
    public static Image smallRock1[], smallRock2[];

    public static Image menu, splash;
    public static Image tileLeft, tileRight;
    public static Image player, playerLeft, playerRight, playerDamaged;
    public static Image button;
    public static Sound playerLaser, playerCrash;
    public static Music theme;

    // TODO - this should probably load all the assets, only. Play the music in the loading or menu screen
    // TODO - organize assets by level?
    public static void load(RobotAndroidGame sampleGame)
    {
        theme = sampleGame.getAudio().createMusic("xeon6.ogg"); //"Lines of Code.mp3");
        theme.setLooping(true);
        theme.setVolume(0.8f);
        theme.play();
    }

    public static boolean loadLargeRocks(Graphics g)
    {
        // load large rock 1 - 2
        Assets.largeRock1 = new Image[Assets.numAsteroidImages];
        for (int i = 0; i < Assets.numAsteroidImages; i++)
        {
            String fileName = "asteroids/large/a" + (int) (10000 + i) + ".png";
            Assets.largeRock1[i] = g.newImage(fileName, Graphics.ImageFormat.RGB565);
        }

        Assets.largeRock2 = new Image[Assets.numAsteroidImages];
        for (int i = 0; i < Assets.numAsteroidImages; i++)
        {
            String fileName = "asteroids/large/b" + (int) (30000 + i) + ".png";
            Assets.largeRock2[i] = g.newImage(fileName, Graphics.ImageFormat.RGB565);
        }

        return true;
    }

    public static boolean loadMediumRocks(Graphics g)
    {
        // load large rock 1 - 2
        Assets.mediumRock1 = new Image[Assets.numAsteroidImages];
        for (int i = 0; i < Assets.numAsteroidImages; i++)
        {
            String fileName = "asteroids/medium/a" + (int) (10000 + i) + ".png";
            Assets.mediumRock1[i] = g.newImage(fileName, Graphics.ImageFormat.RGB565);
        }

        Assets.mediumRock2 = new Image[Assets.numAsteroidImages];
        for (int i = 0; i < Assets.numAsteroidImages; i++)
        {
            String fileName = "asteroids/medium/d" + (int) (30000 + i) + ".png";
            Assets.mediumRock2[i] = g.newImage(fileName, Graphics.ImageFormat.RGB565);
        }

        return true;
    }

    public static boolean loadSmallRocks(Graphics g)
    {
        // load large rock 1 - 2
        Assets.smallRock1 = new Image[Assets.numAsteroidImages];
        for (int i = 0; i < Assets.numAsteroidImages; i++)
        {
            String fileName = "asteroids/small/a" + (int) (10000 + i) + ".png";
            Assets.smallRock1[i] = g.newImage(fileName, Graphics.ImageFormat.RGB565);
        }

        Assets.smallRock2 = new Image[Assets.numAsteroidImages];
        for (int i = 0; i < Assets.numAsteroidImages; i++)
        {
            String fileName = "asteroids/small/a" + (int) (30000 + i) + ".png";
            Assets.smallRock2[i] = g.newImage(fileName, Graphics.ImageFormat.RGB565);
        }

        return true;
    }

    public static boolean loadTileMap(Graphics g)
    {
        Assets.tileLeft = g.newImage("tileLeft2.png", Graphics.ImageFormat.RGB565);
        Assets.tileRight = g.newImage("tileRight2.png", Graphics.ImageFormat.RGB565);

        return true;
    }

    public static boolean loadBackground(Graphics g)
    {
        // load first 2 backgrounds
        Assets.bgImg1 = g.newImage("Background-1.png", Graphics.ImageFormat.RGB565);
        Assets.bgImg2 = g.newImage("Background-2.png", Graphics.ImageFormat.RGB565);

        return true;
    }

    public static boolean loadPlayer(Graphics g, Game game)
    {
        // use ARGB8888 for highest quality, with alpha
        Assets.player = g.newImage("player.png", Graphics.ImageFormat.ARGB8888);
        Assets.playerLeft = g.newImage("playerLeft.png", Graphics.ImageFormat.ARGB8888);
        Assets.playerRight = g.newImage("playerRight.png", Graphics.ImageFormat.ARGB8888);
        Assets.playerDamaged = g.newImage("playerDamaged.png", Graphics.ImageFormat.ARGB8888);

        // TODO load audio format based on hwAudioSampleRate, to allow for fast playback (avoid conversion)
        Assets.playerLaser = game.getAudio().createSound("playerLaser_48khz.wav");
        Assets.playerCrash = game.getAudio().createSound("playerCrash.wav");

        return true;
    }

}
