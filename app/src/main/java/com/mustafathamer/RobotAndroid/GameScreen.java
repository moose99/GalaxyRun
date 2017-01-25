package com.mustafathamer.RobotAndroid;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Screen;
import com.mustafathamer.framework.Input.TouchEvent;

import java.util.List;

/**
 * Created by Mus on 11/25/2016.
 * We make use of GameStates to call different sets of update/paint methods, rather than creating
 * multiple Classes for these different states of the game
 */

public class GameScreen extends Screen
{
    enum GameState
    {
        Ready, Running, Paused, GameOver
    }

    GameState state = GameState.Ready;

    // Variable Setup

    private static Player playerObj;

    public static int gameHeight, gameWidth;

    private Asteroid largeRock1;

    private TileMap tileMap;

    private int livesLeft = 1;
    private Paint paint, paint2;

    private int prevMoveEventX = 0;
    private int prevMoveEventY = 0;
    private Rect shootButtonBounds = new Rect();
    private BackgroundMgr bgndMgr;

    public GameScreen(Game game)
    {
        super(game);

        gameWidth = game.getGraphics().getWidth();
        gameHeight = game.getGraphics().getHeight();

        init();

        bgndMgr = new BackgroundMgr();
        bgndMgr.init();

        // Initialize game objects here

        playerObj = new Player(game);
        playerObj.initAssets();

        largeRock1 = new Asteroid(Asteroid.Type.Large1);
        largeRock1.initAssets();

        tileMap = new TileMap();
        tileMap.load();

        // Defining a paint object
        paint = new Paint();
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        paint2 = new Paint();
        paint2.setTextSize(100);
        paint2.setTextAlign(Paint.Align.CENTER);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.WHITE);
    }

    @Override
    public void update(float deltaTime)
    {
        List touchEvents = game.getInput().getTouchEvents();

        // We have four separate update methods in this example.
        // Depending on the state of the game, we call different update methods.
        // Refer to Unit 3's code. We did a similar thing without separating the
        // update methods.

        if (state == GameState.Ready)
            updateReady(touchEvents);
        if (state == GameState.Running)
            updateRunning(touchEvents, deltaTime);
        if (state == GameState.Paused)
            updatePaused(touchEvents);
        if (state == GameState.GameOver)
            updateGameOver(touchEvents);
    }

    //
    // update for ready screen
    //
    private void updateReady(List touchEvents)
    {
        // This example starts with a "Ready" screen.
        // When the user touches the screen, the game begins.
        // state now becomes GameState.Running.
        // Now the updateRunning() method will be called!

        if (touchEvents.size() > 0)
            state = GameState.Running;
    }

    //
    // update for game running
    //
    private void updateRunning(List touchEvents, float deltaTime)
    {
        // 1. All touch input is handled here:
        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {
            TouchEvent event = (TouchEvent) touchEvents.get(i);

            // SHOOT BUTTON
            boolean shootButtonEvent = inBounds(event, shootButtonBounds.left, shootButtonBounds.top, shootButtonBounds.width(), shootButtonBounds.height());
            boolean pauseEvent = inBounds(event, 0, 0, 35, 35);
            boolean moveShipEvent = (event.y > game.getGraphics().getHeight() * .2) && event.pointer == 0;

//            Log.i("MOOSE", "Event, PrevX=" + prevMoveEventX + ", X=" + event.x + ", Y=" + event.y + ", Type=" + event.type
//            + ", Ptr=" + event.pointer);

            if (shootButtonEvent && event.type == TouchEvent.TOUCH_DOWN)
            {
                playerObj.shoot();
            }

            if (pauseEvent && event.type == TouchEvent.TOUCH_DOWN)
            {
                pause();
            }

            if (moveShipEvent && event.type == TouchEvent.TOUCH_UP)
            {
                playerObj.stop();
            }

            // Move ship
            if (moveShipEvent && event.type == TouchEvent.TOUCH_DRAGGED)
            {
                boolean moveX = false;
                boolean moveY = false;

                int diffX = (prevMoveEventX != 0) ? event.x - prevMoveEventX : 0;
                int diffY = (prevMoveEventY != 0) ? event.y - prevMoveEventY : 0;

                if (diffX > 0)
                {
                    moveX = true;
                    diffX = Math.min(diffX, playerObj.MOVESPEED * 5);   // clamp
                } else if (diffX < 0)
                {
                    moveX = true;
                    diffX = Math.max(diffX, -playerObj.MOVESPEED * 5);  // clamp
                }

                if (diffX > 3)
                    playerObj.setMovingRight(true);
                else if (diffX < -3)
                    playerObj.setMovingLeft(true);
                else if (Math.abs(diffX) < 1)
                {
                    playerObj.setMovingRight(false);
                    playerObj.setMovingLeft(false);
                }

                if (diffY > 0)
                {
                    moveY = true;
                    diffY = Math.min(diffY, playerObj.MOVESPEED * 5);   // clamp
                }
                if (diffY < 0)
                {
                    moveY = true;
                    diffY = Math.max(diffY, -playerObj.MOVESPEED * 5);   // clamp
                }

//                Log.i("\tMOOSE", "\tdiffX=" + diffX + ", moveLeft=" + moveLeft + ", moveRight=" + moveRight);

                /*
                // MOVE LEFT OR RIGHT
                if (moveX)
                {
                    playerObj.setSpeedX(diffX);
                }

                // MOVE UP OR DOWN
                if (moveY)
                {
                    playerObj.setSpeedY(diffY);
                }
                */

                // set player above the touch point
                playerObj.setX(event.x);
                playerObj.setY(event.y - 75);

                prevMoveEventX = event.x;
                prevMoveEventY = event.y;
            }
        }

// 2. Check miscellaneous events like death:

        if (livesLeft == 0)
        {
            state = GameState.GameOver;
        }

        // 3. Call individual update() methods here.
        // This is where all the game updates happen.

        // TODO use delta time
        playerObj.update();

        largeRock1.setPos(playerObj.getX() - (int) (playerObj.WIDTH * .5), playerObj.getY() - 200);
        largeRock1.update();

        tileMap.update();

        bgndMgr.updateBackgrounds(game.getGraphics());

        // TODO - game over state
        /*
        if (playerObj.getY() > 500)
        {
            state = GameState.GameOver;
        }
        */
    }

    //
    // scroll both backgrounds, if one goes off the screen, load in the next one to take it's place
    //

    private boolean inBounds(TouchEvent event, int x, int y, int width, int height)
    {
        if (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1)
            return true;
        else
            return false;
    }

    private void updatePaused(List touchEvents)
    {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP)
            {
                if (inBounds(event, 0, 0, 800, 240))
                {

                    if (!inBounds(event, 0, 0, 35, 35))
                    {
                        resume();
                    }
                }

                if (inBounds(event, 0, 240, 800, 240))
                {
                    init();
                    goToMenu();
                }
            }
        }
    }

    private void updateGameOver(List touchEvents)
    {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN)
            {
                if (inBounds(event, 0, 0, 800, 480))
                {
                    init();
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }

    }

    @Override
    public void paint(float deltaTime)
    {
        Graphics g = game.getGraphics();

        bgndMgr.drawBackgrounds(g);
        tileMap.draw(g);
        playerObj.draw(g);
        largeRock1.draw(g);

        // Secondly, draw the UI above the game elements.
        if (state == GameState.Ready)
            drawReadyUI();
        if (state == GameState.Running)
            drawRunningUI();
        if (state == GameState.Paused)
            drawPausedUI();
        if (state == GameState.GameOver)
            drawGameOverUI();

    }

    // TODO fix this init
    private void init()
    {
        // Set all variables to null. You will be recreating them in the
        // constructor.
        paint = null;
        bgndMgr = null;

        // Call garbage collector to clean up memory.
        System.gc();

        getShootButtonBounds(shootButtonBounds);
    }

    private void getShootButtonBounds(Rect r)
    {
        int bloat = 2;
        Graphics g = game.getGraphics();
        int startY = 0; // (int) (g.getHeight() * .1);
        int buttonDim = 65;
        int startX = (int) (g.getWidth() * .85);
        r.set(startX - bloat, startY - bloat,                       // left, top
                startX + buttonDim + bloat, startY + buttonDim + bloat);    // right bottom
    }

    private void drawReadyUI()
    {
        Graphics g = game.getGraphics();

        g.drawARGB(155, 0, 0, 0);
        g.drawString("Tap to Start.", g.getWidth() / 2, g.getHeight() / 2, paint);
    }

    private void drawRunningUI()
    {
        Graphics g = game.getGraphics();
        int startY = 0; //(int) (g.getHeight() * .9);
        int buttonDim = 65;
        int startX = (int) (g.getWidth() * .85);

        // 3 button panel, each button 65 pixels square, draw center button only
        g.drawImage(Assets.button,          // image
                startX, startY,             // x, y
                buttonDim, 0,               // srcx, srcy
                buttonDim, buttonDim);      // width, height

        // pause button at top left
        g.drawImage(Assets.button, 0, 0, buttonDim * 3, 35, 35, 35);
    }

    private void drawPausedUI()
    {
        Graphics g = game.getGraphics();
        // Darken the entire screen so you can display the Paused screen.
        g.drawARGB(155, 0, 0, 0);
        g.drawString("Resume", g.getWidth() / 2, g.getHeight() / 2, paint2);
        g.drawString("Menu", g.getWidth() / 2, g.getHeight() / 2 + 100, paint2);
    }

    private void drawGameOverUI()
    {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 1281, 801, Color.BLACK);
        g.drawString("GAME OVER.", g.getWidth() / 2, g.getHeight() / 2, paint2);
        g.drawString("Tap to return.", g.getWidth() / 2, g.getHeight() / 2 + 100, paint);

    }

    @Override
    public void pause()
    {
        if (state == GameState.Running)
            state = GameState.Paused;

    }

    @Override
    public void resume()
    {
        if (state == GameState.Paused)
            state = GameState.Running;
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void backButton()
    {
        pause();
    }

    private void goToMenu()
    {
        game.setScreen(new MainMenuScreen(game));
    }

    public static Player getPlayer()
    {
        return playerObj;
    }

}
