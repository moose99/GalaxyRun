package com.mustafathamer.GalaxyRun;

import android.util.Log;

import com.mustafathamer.framework.Screen;
import com.mustafathamer.framework.implementation.AndroidGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Mus on 11/23/2016.
 * <p>
 * Our game will have just one Activity that will display a
 * SurfaceView (that we create using the AndroidFastRenderView class).
 * This SurfaceView will paint objects (created using classes like Player or Platform)
 * as they update.
 * <p>
 * This will be the main activity of our game. We will set this in the AndroidManifest.
 * we set most of the screen layouts in the onCreate method of the AndroidGame class.
 */

public class GalaxyRunGame extends AndroidGame
{
    public static String map;
    boolean firstTimeCreate = true;

    @Override
    public Screen getInitScreen()
    {
        //
        // checks whether this is the first time that SampleGame has been opened.
        // If it is the first time, we call the load method inside the Assets class, which loads our music
        //
        if (firstTimeCreate)
        {
            Assets.load(this);
            firstTimeCreate = false;
        }

        // we also define a String called map, which contains the information from the map1.txt file.
        InputStream is = getResources().openRawResource(R.raw.map1);
        map = convertStreamToString(is);

        return new SplashLoadingScreen(this);
    }

    @Override
    public void onBackPressed()
    {
        getCurrentScreen().backButton();
    }

    // convert the map txt file to a string
    private static String convertStreamToString(InputStream is)
    {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append((line + "\n"));
            }
        } catch (IOException e)
        {
            Log.w("LOG", e.getMessage());
        } finally
        {
            try
            {
                is.close();
            } catch (IOException e)
            {
                Log.w("LOG", e.getMessage());
            }
        }
        return sb.toString();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        Assets.theme.play();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        Assets.theme.pause();

    }
}
