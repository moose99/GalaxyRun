package com.mustafathamer.GalaxyRun;

import android.graphics.Rect;
import android.util.Log;

import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Image;

import java.util.ArrayList;

import static com.mustafathamer.GalaxyRun.Assets.player;

/**
 * Created by Mus on 11/26/2016.
 */

public class Player extends GameObject
{
    protected ArrayList<Image> imageList = new ArrayList<Image>();

    // Constants are Here
    public final int MOVESPEED = 5;
    public final int WIDTH = 99;   // from image
    public final int HEIGHT = 75;  // from image

    private final int TIME_BETWEEN_SHOTS = 150;     // in millis, 5 shots per sec
    private PowerUp.Ability ability;
    private long abilityExpires;
    private Animation shieldAnim;

    // lmage identifiers
    public enum ImageType
    {
        Left,
        Right,
        Normal,
        Damaged
    }

    // Sounds identifiers
    public enum SoundType
    {
        Laser,
        Crash,
        Explosion
    }

    private boolean isShooting = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private long lastCrashTime, lastShootTime;
    private GameScreen gameScreen;
    private int numLives;

    //
    // position at center near the bottom
    public Player(GameScreen game)
    {
        x = GameScreen.gameWidth / 2;
        y = (int) (GameScreen.gameHeight * 0.8);
        this.gameScreen = game;

        lastCrashTime = 0;
        lastShootTime = 0;
        isShooting = false;
        movingLeft = false;
        movingRight = false;
        numLives = 10;
        ability = PowerUp.Ability.None;
    }

    @Override
    public void initAssets()
    {
        // add images using order of player.ImageType enum
        imageList.add(Assets.playerLeft);
        imageList.add(Assets.playerRight);
        imageList.add(player);
        imageList.add(Assets.playerDamaged);

        // add sounds using order of player.SoundType enum
        soundList.add(Assets.playerLaser);
        soundList.add(Assets.playerCrash);
        soundList.add(Assets.explosion);

        // player ship is currently a 1 frame anim (doesn't really need to be an anim)
        anim.addFrame(player, 1000);

        // create shield animation, just 1 frame anim for now
        shieldAnim = new Animation();
//        shieldAnim.addFrame(Assets.ssReduxSprites.getImage(), Assets.ssReduxSprites.getRect("shield1.png"), 150);
//        shieldAnim.addFrame(Assets.ssReduxSprites.getImage(), Assets.ssReduxSprites.getRect("shield2.png"), 150);
        shieldAnim.addFrame(Assets.ssReduxSprites.getImage(), Assets.ssReduxSprites.getRect("shield3.png"), 150);
    }

    @Override
    public void update(float deltaTime)
    {
        // Moves Character
        x += speedX;
        y += speedY;

        // CLAMP to screen edge
        if (x > GameScreen.gameWidth)
            x = GameScreen.gameWidth;
        if (x < 0)
            x = 0;

        /*
        if (y > (int)(GameScreen.gameHeight *.9) )
            y = (int)(GameScreen.gameHeight * .9);
        if (y < (int)(GameScreen.gameHeight *.5) )
            y = (int)(GameScreen.gameHeight * .5);
        */
//        Log.i("MOOSE", "ctrX=" + x+ ", ctrY=" + y +
//                ", speedX=" + speedX + ", speedY=" + speedY);

        bounds.set(x - (int)(WIDTH*.5), y - (int)(HEIGHT*.5),
                x + (int)(WIDTH*.5), y + (int)(HEIGHT*.5));

        updateAbility(deltaTime);

        if (isShooting)
            shoot();

        anim.update((int)(deltaTime * 1000));

        checkCollision();
    }

    //
    // check if I have a powerup ability, apply it or expire it
    //
    public void updateAbility(float deltaTime)
    {
        if (ability != PowerUp.Ability.None)
        {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= abilityExpires)
            {   // turn off ability
                setAbility(PowerUp.Ability.None, 0);
                return;
            }

            // apply ability
            switch(ability)
            {
                case Shooting:
                    setShooting(true);
                    break;

                case Cloak:
                    break;

                case Shield:
                    shieldAnim.update(15);
                    break;
            }
        }
    }

    public void setAbility(PowerUp.Ability ab, int duration)
    {
        // turn off current ability
        switch(ability)
        {
            case Shooting:
                setShooting(false);
                break;

            case Cloak:
                break;

            case Shield:
                break;
        }

        // set new ability
        ability = ab;
        abilityExpires = System.currentTimeMillis() + duration;
    }

    @Override
    public void draw(Graphics g)
    {
        // First draw the game elements.
        Image playerSprite = anim.getImage();
        if (getMovingLeft())
            playerSprite = imageList.get(Player.ImageType.Left.ordinal());
        else if (getMovingRight())
            playerSprite = imageList.get(Player.ImageType.Right.ordinal());

        // player ship is centered at x,y
        g.drawImage(playerSprite, x - (int) (WIDTH * .5), y - (int) (HEIGHT * .5));
        drawAbility(g);
        drawBounds(g);
    }

    //
    // Draw extra stuff depending on the powerup ability
    //
    public void drawAbility(Graphics g)
    {
        // apply ability
        switch(ability)
        {
            case None:
                break;
            case Shooting:
                // draw turrets
                break;

            case Shield:
                // draw shield
                Rect shieldRect = shieldAnim.getRect();
                g.drawImage(shieldAnim.getImage(),                      // image
                        x - (int) (shieldRect.width() * .5), y - (int) (shieldRect.height()* .5),                          // x, y
                        shieldRect.left, shieldRect.top,                  // srcx, srcy
                        shieldRect.width(), shieldRect.height());         // width, height
                break;

            case Cloak:

                break;

            default:

            break;
        }

    }

    //
    // check for collisions with asteroids and powerups
    //
    private void checkCollision()
    {
        for (int i=0; i<gameScreen.getGameObjects().size(); i++)
        {
            GameObject gameObject = gameScreen.getGameObjects().get(i);
            if (gameObject.getType() == Type.Asteroid || gameObject.getType() == Type.Alien)
            {
                if (gameObject.getBounds().intersect(getBounds()))
                {
                    //Log.i("MOOSE", "checkCollision: HIT OBJECT");
                    gameObject.setDead(true);   // kill object

                    if (ability != PowerUp.Ability.Shield)
                    {
                        // we crashed into an object
                        soundList.get(SoundType.Crash.ordinal()).play(1.0f);
                        decrementLives();
                    }
                    else
                    {
                        // our shield destroyed an object
                        soundList.get(SoundType.Explosion.ordinal()).play(1.0f);
                        gameScreen.setScore(gameScreen.getScore() + 1);
                    }
                }
            }

            if (gameObject.getType() == Type.PowerUp)
            {
                if (gameObject.getBounds().intersect(getBounds()))
                {
                    PowerUp powerup = (PowerUp) gameObject;

                    Log.i("MOOSE", "checkCollision: HIT POWERUP: " + powerup.getName());
                    gameObject.setDead(true);   // kill powerup
                    // award powerup ability, start timer...
                    setAbility(powerup.getAbility(), powerup.getAbilityDuration());
                }
            }
        }
    }

    public void stop()
    {
        speedX = speedY = 0;
        movingLeft = movingRight = false;
    }

    private void shoot()
    {
        if (isReadyToFire())
        {
            PlayerProjectile p = new PlayerProjectile(gameScreen, x, y);
            p.initAssets();
            p.setPos(x - (int)(p.getWidth()*.5), y - (int)(p.getHeight() * .5) - 30);
            gameScreen.addGameObject(p);
            soundList.get(SoundType.Laser.ordinal()).play(1.0f);
            lastShootTime = System.currentTimeMillis();
        }
    }

    //
    // play crash time (once every half second)
    // wall crash
    //
    public void crash()
    {
        long curTime = System.currentTimeMillis();
        if (curTime - lastCrashTime > 500)
        {
            soundList.get(SoundType.Crash.ordinal()).play(1.0f);
            decrementLives();
            lastCrashTime = curTime;
        }
    }

    //
    // shot by an alien
    //
    public void wasShot()
    {
        if (getAbility() != PowerUp.Ability.Shield)
        {
            soundList.get(SoundType.Crash.ordinal()).play(1.0f);
            decrementLives();
        }
    }

    public boolean getMovingLeft()
    {
        return movingLeft;
    }
    public boolean getMovingRight()
    {
        return movingRight;
    }

    public boolean isReadyToFire()
    {
        return (System.currentTimeMillis() - lastShootTime) > TIME_BETWEEN_SHOTS;
    }

    public void setMovingLeft(boolean movingLeft)
    {
        this.movingLeft = movingLeft;
        if (movingLeft)
            this.movingRight = false;
    }
    public void setMovingRight(boolean movingRight)
    {
        this.movingRight = movingRight;
        if (movingRight)
            this.movingLeft = false;
    }

    public int getNumLives()
    {
        return numLives;
    }

    public void decrementLives()
    {
        if (gameScreen.unlimitedLives)
            return;

        numLives = numLives - 1;
        if (numLives == 0)
            setDead(true);
    }

    @Override
    public int getWidth() { return WIDTH; }

    @Override
    public int getHeight() { return HEIGHT; }

    public boolean isShooting() { return isShooting;   }
    public void setShooting(boolean shooting)   {  isShooting = shooting;  }

    @Override
    public GameObject.Type getType() { return Type.Player; }

    public PowerUp.Ability getAbility() { return ability; }

}
