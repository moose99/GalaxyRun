package com.mustafathamer.RobotAndroid;

import com.mustafathamer.framework.Music;
import com.mustafathamer.framework.Sound;
import com.mustafathamer.framework.Image;

/**
 * Created by Mus on 11/23/2016.
 * <p>
 * This Assets class is used to create a static variable for each resource
 * that we will use in the game.
 * We will initialize them in the LoadingScreen.
 * We define all the assets that will be used in our game, and also load & play the background music at 85% volume.
 */

public class Assets
{

    public static Image menu, splash, background, heliboy, heliboy2, heliboy3, heliboy4, heliboy5;
    public static Image tiledirt, tilegrassTop, tilegrassBot, tilegrassLeft, tilegrassRight, characterJump, characterDown;
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

}
