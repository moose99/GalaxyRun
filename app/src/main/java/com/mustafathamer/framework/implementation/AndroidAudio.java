package com.mustafathamer.framework.implementation;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.mustafathamer.framework.Audio;
import com.mustafathamer.framework.Music;
import com.mustafathamer.framework.Sound;


/**
 * Created by Mus on 10/30/2016.
 * Manages both sounds (short clips in memory) and music (longer streamed audio from a file)
 */

public class AndroidAudio implements Audio
{
    AssetManager assets;
    SoundPool soundPool;    // handles compressed audio

    public AndroidAudio(Activity activity)
    {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);   // deprecated in API level 21
    }

    @Override
    public Music createMusic(String filename)
    {
        try
        {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            return new AndroidMusic(assetDescriptor);
        } catch (IOException e)
        {
            throw new RuntimeException("Couldn't load music '" + filename + "'");
        }
    }

    @Override
    public Sound createSound(String filename)
    {
        try
        {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            int soundId = soundPool.load(assetDescriptor, 0);
            return new AndroidSound(soundPool, soundId);
        } catch (IOException e)
        {
            throw new RuntimeException("Couldn't load sound '" + filename + "'");
        }
    }
}
