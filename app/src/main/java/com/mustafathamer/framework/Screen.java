package com.mustafathamer.framework;

/**
 * Created by Mus on 10/22/2016.
 * This an abstract class, not an interface, for displaying/changing game screens.
 */

public abstract class Screen
{
    protected final Game game;

    public Screen(Game game)
    {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void paint(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();

    public abstract void backButton();
}
