package com.mustafathamer.GalaxyRun;

import android.graphics.Rect;

import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Image;

import static com.mustafathamer.GalaxyRun.PowerUp.Ability.Cloak;
import static com.mustafathamer.GalaxyRun.PowerUp.Ability.Shield;
import static com.mustafathamer.GalaxyRun.PowerUp.Ability.Shooting;

/**
 * Created by Mus on 3/4/2017.
 */

public class PowerUp extends GameObject
{
    enum Ability
    {
        None,
        Shooting,
        Shield,
        Cloak
    }
    private Rect spriteSheetRect;
    private Image spriteSheetImage;
    private String name;
    private Ability ability;

    public int getWidth()       { return spriteSheetRect.width(); }
    public int getHeight()      { return spriteSheetRect.height(); }
    public Type getType()       { return Type.PowerUp; }
    public Ability getAbility() { return ability; }
    public int getAbilityDuration() { return 5000; }    // all abilities last 5 seconds for now

    public void PowerUp()
    {

    }

    public void initAssets(String name)
    {
        this.name = name;
        setSpriteSheetImage(Assets.ssReduxSprites.getImage());
        Rect r = Assets.ssReduxSprites.getRect(name);
        assert(r != null);
        setSpriteSheetRect(r);
        speedY = Background.speedY * 2;

        if (name.equals("powerupBlue_star.png"))
            ability = Shield;
        else
        if (name.equals("powerupRed_star.png"))
            ability = Shooting;

    }

    public void draw(Graphics g)
    {
        g.drawImage(spriteSheetImage,           // image
                x, y,                           // x, y
                spriteSheetRect.left, spriteSheetRect.top,               // srcx, srcy
                spriteSheetRect.width(), spriteSheetRect.height());      // width, height
        drawBounds(g);
    }

    public void update(float deltaTime)
    {
        y += speedY;
        x += speedX;

        updateBounds(getWidth(), getHeight());
        bounceOffWalls();
        checkBelowScreen();
    }

    public void setSpriteSheetRect(Rect r)      { this.spriteSheetRect = r;   }
    public void setSpriteSheetImage(Image img)  { this.spriteSheetImage = img; }
    public String getName() { return name; }
}
