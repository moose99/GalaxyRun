package com.mustafathamer.RobotAndroid;

import com.mustafathamer.framework.Screen;
import com.mustafathamer.framework.implementation.AndroidGame;

/**
 * Created by Mus on 11/23/2016.
 * <p>
 * Our game will have just one Activity that will display a
 * SurfaceView (that we create using the AndroidFastRenderView class).
 * This SurfaceView will paint objects (created using classes like Robot or Platform)
 * as they update.
 * <p>
 * This will be the main activity of our game. We will set this in the AndroidManifest.
 * we set most of the screen layouts in the onCreate method of the AndroidGame class.
 */

public class RobotAndroidGame extends AndroidGame
{
    @Override
    public Screen getInitScreen()
    {
        return new LoadingScreen(this);
    }

    //
    // back button functionality
    //
    @Override
    public void onBackPressed()
    {
        getCurrentScreen().backButton();
    }
}
