package com.connectycube.flutter.connectycube_flutter_call_kit

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log

class NotificationSoundService : Service() {
    private var mMediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        if (intent.action == null) {
            return START_NOT_STICKY
        }
        val action = intent.action
        Log.i(TAG, action)
        when (action) {
            ACTION_START_PLAYBACK -> startSound(intent.getStringExtra(EXTRA_SOUND_URI))
            ACTION_STOP_PLAYBACK -> stopSound()
        }
        return START_NOT_STICKY
    }

    private fun startSound(uriString: String) {
        val soundUri: Uri
        try {
            soundUri = Uri.parse(uriString)
            Log.i(TAG, soundUri.toString())

            // play sound
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val audioAttributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                    mMediaPlayer!!.setAudioAttributes(audioAttributes)
                } else {
                    mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_NOTIFICATION)
                }
                mMediaPlayer!!.setOnPreparedListener { obj: MediaPlayer -> obj.start() }
                mMediaPlayer!!.setOnCompletionListener { mediaPlayer: MediaPlayer? -> stopSound() }
            }
            mMediaPlayer!!.setDataSource(this, soundUri)
            mMediaPlayer!!.prepareAsync()
        } catch (e: Exception) {
            stopSound()
            Log.i(TAG, "ERROR: " + e.message)
        }
    }

    private fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.reset()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
        cleanup()
    }

    private fun cleanup() {
        stopSelf()
    }

    companion object {
        const val ACTION_START_PLAYBACK = "start_playback"
        const val ACTION_STOP_PLAYBACK = "stop_playback"
        const val EXTRA_SOUND_URI = "soundUri"
        const val TAG = "NotificationSound"
    }
}