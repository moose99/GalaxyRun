package com.mustafathamer.GalaxyRun;

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
            boolean pauseEvent = inBounds(event, 0, 0, 35, 35);
            boolean moveShipEvent = (event.y > game.getGraphics().getHeight() * .2);    // && event.pointer == 0;

//            Log.i("MOOSE", "Event, PrevX=" + prevMoveEventX + ", X=" + event.x + ", Y=" + event.y + ", Type=" + event.type
//            + ", Ptr=" + event.pointer);


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
        int r = rand.nextInt(500);  // 800 - getScore()*20) + 1;      // from 1 to 1000, chance increases as score goes up
        GameObject gameObject = null;
        switch (r)
        {
            case 1:
                gameObject = addNewRock(Asteroid.Size.Large1);
                break;
            case 2:
                gameObject = addNewRock(Asteroid.Size.Large2);
                break;
            case 3:
                gameObject = addNewRock(Asteroid.Size.Medium1);
                break;
            case 4:
                gameObject = addNewRock(Asteroid.Size.Medium2);
                break;
            case 5:
                gameObject = addNewRock(Asteroid.Size.Small1);
                break;
            case 6:
                gameObject = addNewRock(Asteroid.Size.Small2);
                break;

            case 7:
            case 8:
                gameObject = addNewPowerUp("powerupBlue_bolt.png");
                break;
            case 9:
            case 10:
                gameObject = addNewPowerUp("powerupBlue_shield.png");
                break;
//            case 9:
//                gameObject = addNewPowerUp("powerupBlue_star.png");
//                break;
/*
            case 10:
                gameObject = addNewPowerUp("powerupGreen_bolt.png");
                break;
            case 11:
                gameObject = addNewPowerUp("powerupGreen_shield.png");
                break;
            case 12:
                gameObject = addNewPowerUp("powerupGreen_star.png");
                break;

            case 13:
                gameObject = addNewPowerUp("powerupRed_bolt.png");
                break;
            case 14:
                gameObject = addNewPowerUp("powerupRed_shield.png");
                break;
            case 15:
                gameObject = addNewPowerUp("powerupRed_star.png");
                break;

            case 16:
                gameObject = addNewPowerUp("powerupYellow_bolt.png");
                break;
            case 17:
                gameObject = addNewPowerUp("powerupYellow_shield.png");
                break;
            case 18:
                gameObject = addNewPowerUp("powerupYellow_star.png");
                break;
                */
        }

        if (gameObject != null)
        {
            // border tile is 32 wide
            int border = 35;
            int x = rand.nextInt(gameWidth - border*2 - gameObject.getWidth() * 2) + border + gameObject.getWidth();
            int y = rand.nextInt(200);
            gameObject.setPos(x, y);
        }
    }

    private GameObject addNewPowerUp(String name)
    {
        PowerUp powerUp = new PowerUp();
        powerUp.initAssets(name);
        powerUp.setSpeedX(rand.nextInt(11) - 5);    // random horiz speed from -5 to 5
        addGameObject(powerUp);
        return powerUp;
    }

    private GameObject addNewRock(Asteroid.Size size)
    {
        if (size == null)
            return null;

        GameObject rock = null;
        switch (size)
        {
            case Large1:
                rock = new Asteroid(this, Asteroid.Size.Large1);
                break;
            case Large2:
                rock = new Asteroid(this, Asteroid.Size.Large2);
                break;
            case Medium1:
                rock = new Asteroid(this, Asteroid.Size.Medium1);
                break;
            case Medium2:
                rock = new Asteroid(this, Asteroid.Size.Medium2);
                break;
            case Small1:
                rock = new Asteroid(this, Asteroid.Size.Small1);
                break;
            case Small2:
                rock = new Asteroid(this, Asteroid.Size.Small2);
                break;
        }

        rock.initAssets();
        rock.setSpeedX(rand.nextInt(11) - 5);    // random horiz speed from -5 to 5

        addGameObject(rock);
        return rock;
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
            {
                // Some rocks get replaced with smaller ones
                if (gameObjects.get(i).getType() == GameObject.Type.Asteroid)
                {
                    Asteroid rock = (Asteroid)gameObjects.get(i);
                    GameObject newRock = addNewRock(rock.getSmallerSize());
                    if (newRock != null)
                        newRock.setPos(rock.getX(), rock.getY());
                }
                gameObjects.remove(i);
            }
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
        playerObj = null;
        tileMap = null;

        // Call garbage collector to clean up memory.
        System.gc();
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
        int buttonDim = 65;

        // pause button at top left
        g.drawImage(Assets.button, 0, 0, buttonDim * 3, 35, 35, 35);

        // draw LIVES and score
        String hud = "Lives:" + playerObj.getNumLives() + "  Score:" + getScore();
        g.drawString(hud, g.getWidth()/2, (int)paint.getTextSize(), paint);

        String abilityString="";
        switch(playerObj.getAbility())
        {
            case Cloak:
                abilityString = "Cloak";
                break;
            case Shield:
                abilityString = "Shield";
                break;
            case Shooting:
                abilityString = "Fire";
                break;
        }
        if (abilityString.length() != 0)
            g.drawString(abilityString, g.getWidth()/2, GameScreen.gameHeight-(int)paint.getTextSize(), paint);
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
