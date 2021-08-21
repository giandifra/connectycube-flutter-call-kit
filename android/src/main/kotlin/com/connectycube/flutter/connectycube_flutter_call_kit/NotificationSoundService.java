package com.connectycube.flutter.connectycube_flutter_call_kit;


import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class NotificationSoundService extends Service {

    private MediaPlayer mMediaPlayer;
    String TAG = "NotificationSoundService";
    public static final String ACTION_START_PLAYBACK = "start_playback";
    public static final String ACTION_STOP_PLAYBACK = "stop_playback";
    public static final String EXTRA_SOUND_URI = "soundUri";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand");
        if (intent == null || intent.getAction() == null) {
            return START_NOT_STICKY;
        }

        String action = intent.getAction();
        Log.i(TAG, action);
        switch (action) {
            case ACTION_START_PLAYBACK:
                startSound(intent.getStringExtra(EXTRA_SOUND_URI));
                break;
            case ACTION_STOP_PLAYBACK:
                stopSound();
                break;
        }

        return START_NOT_STICKY;
    }

    private void startSound(String uriString) {

        Uri soundUri;
        try {
            soundUri = Uri.parse(uriString);
            Log.i(TAG, soundUri.toString());

            // play sound
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    AudioAttributes audioAttributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build();

                    mMediaPlayer.setAudioAttributes(audioAttributes);
                } else {
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                }

                mMediaPlayer.setOnPreparedListener(MediaPlayer::start);
                mMediaPlayer.setOnCompletionListener(mediaPlayer -> stopSound());
            }

            mMediaPlayer.setDataSource(this, soundUri);
            mMediaPlayer.prepareAsync();

        } catch (Exception e) {
            stopSound();
            Log.i(TAG, "ERROR: " + e.getMessage());
        }
    }

    private void stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        cleanup();
    }

    private void cleanup() {
        stopSelf();
    }
}