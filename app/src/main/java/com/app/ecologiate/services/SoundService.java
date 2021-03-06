package com.app.ecologiate.services;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

import com.app.ecologiate.R;

import java.io.IOException;

public class SoundService {

    private Context context;

    private static final String TAG = SoundService.class.getSimpleName();

    private static final float SOUND_VOLUME = 1.0f;
    private static final long VIBRATE_DURATION_LONG = 200L;
    private static final long VIBRATE_DURATION_SHORT = 50L;

    public SoundService(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // Do not keep a reference to the Activity itself, to prevent leaks
        this.context = activity.getApplicationContext();
    }

    public void playBastaChicos(Boolean vibrate){
        playSound(R.raw.basta_chicos);
        if(vibrate){
            vibrateLong();
        }
    }

    public MediaPlayer playSound(int resourceId) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.w("", "Failed to play " + what + ", " + extra);
                // possibly media player error, so release and recreate
                mp.stop();
                mp.release();
                return true;
            }
        });
        try {
            AssetFileDescriptor file = context.getResources().openRawResourceFd(resourceId);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            } finally {
                file.close();
            }
            mediaPlayer.setVolume(SOUND_VOLUME, SOUND_VOLUME);
            mediaPlayer.prepare();
            mediaPlayer.start();
            return mediaPlayer;
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            mediaPlayer.release();
            return null;
        }
    }

    private void vibrateLong(){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATE_DURATION_LONG);
    }

    public static void vibrate(Context context, long duration){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(duration);
    }

    public static void vibrateShort(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATE_DURATION_SHORT);
    }
}
