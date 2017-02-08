package com.mustafathamer.RobotAndroid;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Screen;
import com.mustafathamer.framework.Input.TouchEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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

    private ArrayList<GameObject> gameObjects;
    private TileMap tileMap;
    private Paint paint, paint2;
    private int prevMoveEventX = 0;
    private int prevMoveEventY = 0;
    private Rect shootButtonBounds;
    private BackgroundMgr bgndMgr;
    private Random rand;
    private int score;

    public GameScreen(Game game)
    {
        super(game);
        init();
    }

    public void init()
    {
        gameWidth = game.getGraphics().getWidth();
        gameHeight = game.getGraphics().getHeight();
        Log.i("MOOSE", "init: gameWidth = " + gameWidth + ", gameHeight = " + gameHeight);

        rand = new Random();
        gameObjects = new ArrayList<>();

        shootButtonBounds = new Rect();
        getShootButtonBounds(shootButtonBounds);

        bgndMgr = new BackgroundMgr();
        bgndMgr.init();

        // Initialize game objects here
        // Make sure to set objects to null in uninit

        playerObj = new Player(this);
        playerObj.initAssets();

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

        score = 0;
    }

    @Override
    public void update(float deltaTime)
    {
        List touchEvents = game.getInput().getTouchEvents();

        // We have four separate update methods in this example.
        // Depending on the state of the game, we call different update methods.

        switch (state)
        {
            case Ready:
                updateReady(touchEvents);
                break;

            case Running:
                updateRunning(touchEvents, deltaTime);
                break;

            case Paused:
                updatePaused(touchEvents);
                break;

            case GameOver:
                updateGameOver(touchEvents);
                break;
        }
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
            boolean moveShipEvent = (event.y > game.getGraphics().getHeight() * .2);    // && event.pointer == 0;

//            Log.i("MOOSE", "Event, PrevX=" + prevMoveEventX + ", X=" + event.x + ", Y=" + event.y + ", Type=" + event.type
//            + ", Ptr=" + event.pointer);

            if (shootButtonEvent)
            {
                if (event.type == TouchEvent.TOUCH_DOWN)
                    playerObj.setShooting(true);        // start shooting
                else
                    if (event.type == TouchEvent.TOUCH_UP)
                        playerObj.setShooting(false);   // stop shooting
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

                // set player above the touch point
                playerObj.setX(event.x);
                playerObj.setY(event.y - 75);

                prevMoveEventX = event.x;
                prevMoveEventY = event.y;
            }
        }


        // 3. Call individual update() methods here.
        // This is where all the game updates happen.

        // TODO use delta time
        playerObj.update(deltaTime);
        updateGameObjects(deltaTime);
        tileMap.update(deltaTime);
        bgndMgr.update(game.getGraphics(), deltaTime);

        addNewGameObjects();

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
                    unInit();
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
                    unInit();
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }

    }

    //
    // add rocks, aliens, etc into the game
    //
    private void addNewGameObjects()
    {
        int r = rand.nextInt(1000 - getScore()*25) + 1;      // from 1 to 1000, chance increases as score goes up
        GameObject rock = null;
        switch (r)
        {
            case 1:
                rock = new Asteroid(this, Asteroid.Type.Large1);
                break;
            case 2:
                rock = new Asteroid(this, Asteroid.Type.Large2);
                break;
            case 3:
                rock = new Asteroid(this, Asteroid.Type.Medium1);
                break;
            case 4:
                rock = new Asteroid(this, Asteroid.Type.Medium2);
                break;
            case 5:
                rock = new Asteroid(this, Asteroid.Type.Small1);
                break;
            case 6:
                rock = new Asteroid(this, Asteroid.Type.Small2);
                break;
        }

        if (rock != null)
        {
            rock.initAssets();
            // border tile is 32 wide
            int border = 35;
            int x = rand.nextInt(gameWidth - border*2 - rock.getWidth() * 2) + border + rock.getWidth();
            int y = rand.nextInt(200);
            rock.setPos(x, y);
            rock.setSpeedX(rand.nextInt(11) - 5);    // random horiz speed from -5 to 5
 //           Log.i("MOOSE", "\tpos:" + x + ", " + y);
            addGameObject(rock);
        }
    }

    //
    // update all game objects
    //
    public void updateGameObjects(float deltaTime)
    {
        assert(gameObjects.size() < 100);

        // update all objects
        for (int i = 0; i < gameObjects.size(); i++)
        {
            gameObjects.get(i).update(deltaTime);
        }

        // remove any dead objects
        for (int i = 0; i < gameObjects.size(); )
        {
            if (gameObjects.get(i).isDead())
                gameObjects.remove(i);
            else
                i++;
        }

        if (playerObj.isDead())
        {
            //unInit();
            goToMenu();
            state = GameState.GameOver;
        }
    }

    //
    // draw all game objects
    //
    public void drawGameObjects(Graphics g)
    {
        for (int i = 0; i < gameObjects.size(); i++)
        {
            gameObjects.get(i).draw(g);
        }
    }

    @Override
    public void paint(float deltaTime)
    {
        Graphics g = game.getGraphics();

        switch (state)
        {
            case Ready:
                drawReadyUI();
                break;

            case Running:
                bgndMgr.drawBackgrounds(g);
                tileMap.draw(g);
                playerObj.draw(g);

                drawGameObjects(g);
                drawRunningUI();
                break;

            case Paused:
                drawPausedUI();
                break;

            case GameOver:
                drawGameOverUI();
                break;
        }
    }

    //
    // Set objects to null.  Called when exiting game
    //
    private void unInit()
    {
        rand = null;
        paint = null;
        paint2 = null;
        bgndMgr = null;
        gameObjects = null;
        shootButtonBounds = null;
        playerObj = null;
        tileMap = null;

        // Call garbage collector to clean up memory.
        System.gc();
    }

    private void getShootButtonBounds(Rect r)
    {
        int bloat = 2;
        Graphics g = game.getGraphics();
        int startY = 0; // (int) (g.getHeight() * .1);
        int buttonDim = 65;
        int startX = (int) (g.getWidth() - buttonDim);
        r.set(startX - bloat, startY - bloat,                       // left, top
                startX + buttonDim + bloat, startY + buttonDim + bloat);    // right bottom
    }

    private void drawReadyUI()
    {
        Graphics g = game.getGraphics();

        g.drawARGB(155, 0, 0, 0);
        g.drawString("Tap to Start", g.getWidth() / 2, g.getHeight() / 2, paint);
    }

    private void drawRunningUI()
    {
        Graphics g = game.getGraphics();
        int startY = 0; //(int) (g.getHeight() * .9);
        int buttonDim = 65;
        int startX = (int) (g.getWidth() - buttonDim);

        // 3 button panel, each button 65 pixels square, draw center button only
        g.drawImage(Assets.button,          // image
                startX, startY,             // x, y
                buttonDim, 0,               // srcx, srcy
                buttonDim, buttonDim);      // width, height

        // pause button at top left
        g.drawImage(Assets.button, 0, 0, buttonDim * 3, 35, 35, 35);

        // draw LIVES and score
        String hud = "Lives:" + playerObj.getNumLives() + "  Score:" + getScore();
        g.drawString(hud, g.getWidth()/2, (int)paint.getTextSize(), paint);
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

    public ArrayList<GameObject> getGameObjects()
    {
        return gameObjects;
    }

    public void addGameObject(GameObject gameObject)
    {
        gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject)
    {
        gameObjects.remove(gameObject);
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }
}
