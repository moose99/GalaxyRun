package com.mustafathamer.framework;

/**
 * Created by Mus on 10/22/2016.
 */
public interface Music
{
    void play();

    void stop();

    void pause();

    void setLooping(boolean looping);

    void setVolume(float volume);

    boolean isPlaying();

    boolean isStopped();

    boolean isLooping();

    void dispose();

    void seekBegin();
}
