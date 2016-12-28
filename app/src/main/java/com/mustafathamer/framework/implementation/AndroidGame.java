package com.mustafathamer.framework.implementation;

// Android SDK classes
import android.app.Activity;                // interface window
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;                   // lets you pass info between multiple activities
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;    // prevents the phone from sleeping while app is running
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

// Our interfaces
import com.mustafathamer.framework.Audio;
import com.mustafathamer.framework.FileIO;
import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Input;
import com.mustafathamer.framework.Screen;


/**
 * Created by Mus on 10/29/2016.
 * Implements the main GAME interface
 */

public abstract class AndroidGame extends Activity implements Game
{
    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;

    Screen screen;
    WakeLock wakeLock;

    //
    // Called when the activity is created for the first time.
    // Sets the layout of the activity
    //
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // remove the title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // set window to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // create framebuffer for portrait or landscape view, 800 x 480
        // we'll be using portrait only (set in AndroidManifest)
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int frameBufferWidth = isPortrait ? 480 : 800;
        int frameBufferHeight = isPortrait ? 800 : 480;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Config.RGB_565);

        // get display width and height
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float scaleX = (float) frameBufferWidth / metrics.widthPixels;
        float scaleY = (float) frameBufferHeight / metrics.heightPixels;

        // Create new instances of the classes which will implement our interfaces
        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getInitScreen();
        setContentView(renderView);

        // create a wakeLock object and bind it to our game
        // we acquire and release wakelock in the onResume and onPause methods
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyGame");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    @Override
    public Input getInput()         { return input; }

    @Override
    public FileIO getFileIO()       { return fileIO; }

    @Override
    public Graphics getGraphics()   { return graphics; }

    @Override
    public Audio getAudio()         { return audio; }

    @Override
    public void setScreen(Screen screen)
    {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    public Screen getCurrentScreen()    { return screen; }
}
