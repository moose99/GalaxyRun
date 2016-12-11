package com.mustafathamer.RobotAndroid;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mustafathamer.framework.Game;
import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Screen;
import com.mustafathamer.framework.Image;
import com.mustafathamer.framework.Input.TouchEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static android.R.attr.width;
import static android.R.attr.x;
import static android.R.attr.y;
import static com.mustafathamer.RobotAndroid.Robot.rect;

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

    private static Background bg1, bg2;
    private static Robot robot;
    public static Heliboy hb, hb2;

    private Image currentSprite, character, character2, character3, heliboy,
            heliboy2, heliboy3, heliboy4, heliboy5;
    private Animation anim, hanim;

    private ArrayList tilearray = new ArrayList();

    int livesLeft = 1;
    Paint paint, paint2;

    public GameScreen(Game game)
    {
        super(game);

        // Initialize game objects here

        bg1 = new Background(0, -Background.getHeight());
        bg2 = new Background(0, 0);
        robot = new Robot(game);
        hb = new Heliboy(340, 360);
        hb2 = new Heliboy(700, 360);

        character = Assets.character;
        character2 = Assets.character2;
        character3 = Assets.character3;

        heliboy = Assets.heliboy;
        heliboy2 = Assets.heliboy2;
        heliboy3 = Assets.heliboy3;
        heliboy4 = Assets.heliboy4;
        heliboy5 = Assets.heliboy5;

        anim = new Animation();
        anim.addFrame(character, 1250);
        anim.addFrame(character2, 50);
        anim.addFrame(character3, 50);
        anim.addFrame(character2, 50);

        hanim = new Animation();
        hanim.addFrame(heliboy, 100);
        hanim.addFrame(heliboy2, 100);
        hanim.addFrame(heliboy3, 100);
        hanim.addFrame(heliboy4, 100);
        hanim.addFrame(heliboy5, 100);
        hanim.addFrame(heliboy4, 100);
        hanim.addFrame(heliboy3, 100);
        hanim.addFrame(heliboy2, 100);

        currentSprite = anim.getImage();

        loadMap();

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

    private void loadMap()
    {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        Scanner scanner = new Scanner(RobotAndroidGame.map);
        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();

            // no more lines to read
            if (line == null)
            {
                break;
            }

            if (!line.startsWith("!"))
            {
                lines.add(line);
                width = Math.max(width, line.length());

            }
        }
        height = lines.size();

        for (int j = 0; j < 12; j++)
        {
            String line = (String) lines.get(j);
            for (int i = 0; i < width; i++)
            {

                if (i < line.length())
                {
                    char ch = line.charAt(i);
                    Tile t = new Tile(i, j, Character.getNumericValue(ch));
                    tilearray.add(t);
                }

            }
        }

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
            if (event.type == TouchEvent.TOUCH_DOWN)
            {
                Rect rLeft = new Rect();
                Rect rShoot = new Rect();
                Rect rRight = new Rect();

                getLeftButtonBounds(rLeft);
                getShootButtonBounds(rShoot);
                getRightButtonBounds(rRight);

                // SHOOT BUTTON
                if (inBounds(event, rShoot.left, rShoot.top, rShoot.width(), rShoot.height()))
                {
                    if (robot.isReadyToFire())
                    {
                        robot.shoot();
                    }
                }

                // MOVE LEFT BUTTON
                if (inBounds(event, rLeft.left, rLeft.top, rLeft.width(), rLeft.height()))
                {
                    robot.moveLeft();
                    robot.setMovingLeft(true);
                }
                else
                // MOVE RIGHT BUTTON
                if (inBounds(event, rRight.left, rRight.top, rRight.width(), rRight.height()))
                {
                    robot.moveRight();
                    robot.setMovingRight(true);
                }
            }

            if (event.type == TouchEvent.TOUCH_UP)
            {

                /*
                if (inBounds(event, 0, 415, 65, 65))
                {
                    currentSprite = anim.getImage();
                }
                */

                // PAUSE BUTTON
                if (inBounds(event, 0, 0, 35, 35))
                {
                    pause();
                }

                robot.stop();
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

        updateTiles();
        hb.update();
        hb2.update();
        bg1.update();
        bg2.update();
        animate();

        // TODO - game over state
        /*
        if (robot.getCenterY() > 500)
        {
            state = GameState.GameOver;
        }
        */
    }

    private boolean inBounds(TouchEvent event, int x, int y, int width,
                             int height)
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
                    nullify();
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
                    nullify();
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }

    }

    private void updateTiles()
    {

        for (int i = 0; i < tilearray.size(); i++)
        {
            Tile t = (Tile) tilearray.get(i);
            t.update();
        }

    }

    @Override
    public void paint(float deltaTime)
    {
        Graphics g = game.getGraphics();

        g.drawImage(Assets.background, bg1.getBgX(), bg1.getBgY());
        g.drawImage(Assets.background, bg2.getBgX(), bg2.getBgY());

        // TODO paint tiles
        // paintTiles(g);

        ArrayList projectiles = robot.getProjectiles();
        for (int i = 0; i < projectiles.size(); i++)
        {
            Projectile p = (Projectile) projectiles.get(i);
            g.drawRect(p.getX(), p.getY(), 10, 5, Color.YELLOW);
        }

        // First draw the game elements.
        g.drawImage(currentSprite, robot.getCenterX() - 61,
                robot.getCenterY() - 63);
        g.drawImage(hanim.getImage(), hb.getCenterX() - 48,
                hb.getCenterY() - 48);
        g.drawImage(hanim.getImage(), hb2.getCenterX() - 48,
                hb2.getCenterY() - 48);

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

    private void paintTiles(Graphics g)
    {
        for (int i = 0; i < tilearray.size(); i++)
        {
            Tile t = (Tile) tilearray.get(i);
            if (t.type != 0)
            {
                g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY());
            }
        }
    }

    public void animate()
    {
        anim.update(10);
        hanim.update(50);
    }

    private void nullify()
    {

        // Set all variables to null. You will be recreating them in the
        // constructor.
        paint = null;
        bg1 = null;
        bg2 = null;
        robot = null;
        hb = null;
        hb2 = null;
        currentSprite = null;
        character = null;
        character2 = null;
        character3 = null;
        heliboy = null;
        heliboy2 = null;
        heliboy3 = null;
        heliboy4 = null;
        heliboy5 = null;
        anim = null;
        hanim = null;

        // Call garbage collector to clean up memory.
        System.gc();

    }

    private void getLeftButtonBounds(Rect r)
    {
        Graphics g = game.getGraphics();
        int startY = (int)(g.getHeight() * .9);
        int buttonDim = 65;
        int startX = (int)(g.getWidth()/2 - buttonDim * 1.5);

        r.set(startX, startY, startX+buttonDim, startY+buttonDim);
    }

    private void getShootButtonBounds(Rect r)
    {
        Graphics g = game.getGraphics();
        int startY = (int)(g.getHeight() * .9);
        int buttonDim = 65;
        int startX = (int)(g.getWidth()/2 - buttonDim * 1.5);
        r.set(startX+buttonDim, startY, startX+buttonDim*2, startY+buttonDim);
    }

    private void getRightButtonBounds(Rect r)
    {
        Graphics g = game.getGraphics();
        int startY = (int)(g.getHeight() * .9);
        int buttonDim = 65;
        int startX = (int)(g.getWidth()/2 - buttonDim * 1.5);

        r.set(startX+buttonDim*2, startY, startX+buttonDim*3, startY+buttonDim);
    }

    private void drawReadyUI()
    {
        Graphics g = game.getGraphics();

        g.drawARGB(155, 0, 0, 0);
        g.drawString("Tap to Start.", g.getWidth()/2, g.getHeight()/2, paint);
    }

    private void drawRunningUI()
    {
        Graphics g = game.getGraphics();
        int startY = (int)(g.getHeight() * .9);
        int buttonDim = 65;
        int startX = (int)(g.getWidth()/2 - buttonDim * 1.5);

        // 3 button panel at bottom center of screen
        g.drawImage(Assets.button, startX, startY,   0, 0,   buttonDim*3, buttonDim);

        // pause button at top left
        g.drawImage(Assets.button, 0, 0,    buttonDim*3, 35,    35, 35);
    }

    private void drawPausedUI()
    {
        Graphics g = game.getGraphics();
        // Darken the entire screen so you can display the Paused screen.
        g.drawARGB(155, 0, 0, 0);
        g.drawString("Resume", g.getWidth()/2, g.getHeight()/2, paint2);
        g.drawString("Menu", g.getWidth()/2, g.getHeight()/2+100, paint2);
    }

    private void drawGameOverUI()
    {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 1281, 801, Color.BLACK);
        g.drawString("GAME OVER.", g.getWidth()/2, g.getHeight()/2, paint2);
        g.drawString("Tap to return.", g.getWidth()/2, g.getHeight()/2 + 100, paint);

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

    public static Background getBg1()
    {
        return bg1;
    }

    public static Background getBg2()
    {
        return bg2;
    }

    public static Robot getRobot()
    {
        return robot;
    }

}
