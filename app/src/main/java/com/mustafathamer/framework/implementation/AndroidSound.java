package com.mustafathamer.framework.implementation;

import android.media.SoundPool;

import com.mustafathamer.framework.Sound;


/**
 * Created by Mus on 10/30/2016.
 * uses the SoundPool and an integer ID to keep track
 * of various sounds, play them, and dispose them from memory
 */

public class AndroidSound implements Sound
{
    int soundId;
    SoundPool soundPool;

    public AndroidSound(SoundPool soundPool, int soundId)
    {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    @Override
    public void play(float volume)
    {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    @Override
    public void dispose()
    {
        soundPool.unload(soundId);
    }
}
