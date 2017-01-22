package com.mustafathamer.RobotAndroid;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Screen;
import com.mustafathamer.framework.Image;
import com.mustafathamer.framework.Input.TouchEvent;
import com.mustafathamer.framework.Sound;

import java.util.ArrayList;
import java.util.List;

/*
import static com.mustafathamer.RobotAndroid.Assets.heliboy;
import static com.mustafathamer.RobotAndroid.Assets.heliboy2;
import static com.mustafathamer.RobotAndroid.Assets.heliboy3;
import static com.mustafathamer.RobotAndroid.Assets.heliboy4;
import static com.mustafathamer.RobotAndroid.Assets.heliboy5;
import static com.mustafathamer.RobotAndroid.Robot.rect;
*/

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

    private static Robot robot;
    //   public static Heliboy hb, hb2;

    public static int gameHeight, gameWidth;

    private Image playerSprite, player, playerLeft, playerRight, playerDamaged;
    // private Image heliboy, heliboy2, heliboy3, heliboy4, heliboy5;
    private Animation playerAnim;
//    private Animation hanim;

    private Sound playerLaser;
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

        robot = new Robot(game);

        player = Assets.player;
        playerRight = Assets.playerRight;
        playerLeft = Assets.playerLeft;
        playerDamaged = Assets.playerDamaged;

        playerLaser = Assets.playerLaser;

        // player ship is currently a 1 frame anim (doesn't really need to be an anim)
        playerAnim = new Animation();
        playerAnim.addFrame(player, 1000);

        /*
        hanim = new Animation();
        hanim.addFrame(heliboy, 100);
        hanim.addFrame(heliboy2, 100);
        hanim.addFrame(heliboy3, 100);
        hanim.addFrame(heliboy4, 100);
        hanim.addFrame(heliboy5, 100);
        hanim.addFrame(heliboy4, 100);
        hanim.addFrame(heliboy3, 100);
        hanim.addFrame(heliboy2, 100);
*/

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
                if (robot.isReadyToFire())
                {
                    robot.shoot();
                    playerLaser.play(1.0f);
                }
            }

            if (pauseEvent && event.type == TouchEvent.TOUCH_DOWN)
            {
                pause();
            }

            if (moveShipEvent && event.type == TouchEvent.TOUCH_UP)
            {
                robot.stop();
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
                    diffX = Math.min(diffX, robot.MOVESPEED * 5);   // clamp
                } else if (diffX < 0)
                {
                    moveX = true;
                    diffX = Math.max(diffX, -robot.MOVESPEED * 5);  // clamp
                }

                if (diffX > 3)
                    robot.setMovingRight(true);
                else if (diffX < -3)
                    robot.setMovingLeft(true);
                else if (Math.abs(diffX) < 1)
                {
                    robot.setMovingRight(false);
                    robot.setMovingLeft(false);
                }

                if (diffY > 0)
                {
                    moveY = true;
                    diffY = Math.min(diffY, robot.MOVESPEED * 5);   // clamp
                }
                if (diffY < 0)
                {
                    moveY = true;
                    diffY = Math.max(diffY, -robot.MOVESPEED * 5);   // clamp
                }

//                Log.i("\tMOOSE", "\tdiffX=" + diffX + ", moveLeft=" + moveLeft + ", moveRight=" + moveRight);

                /*
                // MOVE LEFT OR RIGHT
                if (moveX)
                {
                    robot.setSpeedX(diffX);
                }

                // MOVE UP OR DOWN
                if (moveY)
                {
                    robot.setSpeedY(diffY);
                }
                */

                // set robot above the touch point
                robot.setCenterX(event.x);
                robot.setCenterY(event.y - 75);

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

        // TODO
        robot.update();

        ArrayList projectiles = robot.getProjectiles();
        for (int i = 0; i < projectiles.size(); i++)
        {
            Projectile p = (Projectile) projectiles.get(i);
            if (p.isVisible() == true)
            {
                p.update();
            } else
            {
                projectiles.remove(i);
            }
        }

        tileMap.update();

        /*
        hb.update();
        hb2.update();
        */
        bgndMgr.updateBackgrounds(game.getGraphics());
        animate();

        // TODO - game over state
        /*
        if (robot.getCenterY() > 500)
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

        ArrayList projectiles = robot.getProjectiles();
        for (int i = 0; i < projectiles.size(); i++)
        {
            Projectile p = (Projectile) projectiles.get(i);
            g.drawRect(p.getX(), p.getY(), 4, 4, Color.YELLOW);
        }

        // First draw the game elements.
        playerSprite = playerAnim.getImage();
        if (robot.getMovingLeft())
            playerSprite = playerLeft;
        else if (robot.getMovingRight())
            playerSprite = playerRight;

        g.drawImage(playerSprite, robot.getCenterX() - (int) (robot.WIDTH * .5), robot.getCenterY() - (int) (robot.HEIGHT * .5));

        /*
        g.drawImage(hanim.getImage(), hb.getCenterX() - 48,
                hb.getCenterY() - 48);
        g.drawImage(hanim.getImage(), hb2.getCenterX() - 48,
                hb2.getCenterY() - 48);
*/
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

    // TODO pass elapsed time into animate()
    public void animate()
    {
        playerAnim.update(10);
        //       hanim.update(50);
    }

    private void init()
    {

        // Set all variables to null. You will be recreating them in the
        // constructor.
        paint = null;
        bgndMgr = null;

        robot = null;
//        hb = null;
//        hb2 = null;
        playerSprite = null;
        player = null;
        playerLeft = null;
        playerRight = null;
        playerDamaged = null;
 /*
        heliboy = null;

        heliboy2 = null;
        heliboy3 = null;
        heliboy4 = null;
        heliboy5 = null;
*/
        playerAnim = null;
//        hanim = null;

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

    public static Robot getRobot()
    {
        return robot;
    }

}
