package com.mustafathamer.framework.implementation;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import com.mustafathamer.framework.Audio;
import com.mustafathamer.framework.Music;
import com.mustafathamer.framework.Sound;

import static android.media.AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE;


/**
 * Created by Mus on 10/30/2016.
 * Manages both sounds (short clips in memory) and music (longer streamed audio from a file)
 */

public class AndroidAudio implements Audio
{
    public final int MAXSTREAMS = 20;

    private Context context;
    private AssetManager assets;
    private SoundPool soundPool;    // handles compressed audio

    public AndroidAudio(Activity activity)
    {
        context = activity;
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Log.i("MOOSE", "AndroidAudio: Using Sound Builder");
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    //.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .build();
            this.soundPool = new SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(MAXSTREAMS).build();
        } else
        {
            Log.i("MOOSE", "AndroidAudio: Using deprecated Sound Pool ctor");
            this.soundPool = new SoundPool(MAXSTREAMS, AudioManager.STREAM_MUSIC, 0);   // deprecated in API level 21
        }
    }

    @Override
    public int getBestSampleRate()
    {
        if (Build.VERSION.SDK_INT >= 17)
        {
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            String sampleRateString = am.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
            int sampleRate = sampleRateString == null ? 44100 : Integer.parseInt(sampleRateString);

            return sampleRate;
        } else
        {
            return 44100;
        }
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
