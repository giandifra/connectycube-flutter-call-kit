package com.connectycube.flutter.connectycube_flutter_call_kit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class EventReceiver : BroadcastReceiver() {
    private val TAG = "EventReceiver"
    override fun onReceive(context: Context, intent: Intent?) {

        if (intent == null || TextUtils.isEmpty(intent.action)) return

        when (intent.action) {
            ACTION_CALL_REJECT -> {
                stopNotificationSound(context);
                val extras = intent.extras
                val callId = extras?.getString(EXTRA_CALL_ID)
                val callType = extras?.getInt(EXTRA_CALL_TYPE)
                val callInitiatorId = extras?.getInt(EXTRA_CALL_INITIATOR_ID)
                val callInitiatorName = extras?.getString(EXTRA_CALL_INITIATOR_NAME)
                val callOpponents = extras?.getIntegerArrayList(EXTRA_CALL_OPPONENTS)
                val userInfo = extras?.getString(EXTRA_CALL_USER_INFO)
                Log.i(TAG, "NotificationReceiver onReceive Call REJECT, callId: $callId")

                val broadcastIntent = Intent(ACTION_CALL_REJECT)
                val bundle = Bundle()
                bundle.putString(EXTRA_CALL_ID, callId)
                bundle.putInt(EXTRA_CALL_TYPE, callType!!)
                bundle.putInt(EXTRA_CALL_INITIATOR_ID, callInitiatorId!!)
                bundle.putString(EXTRA_CALL_INITIATOR_NAME, callInitiatorName)
                bundle.putIntegerArrayList(EXTRA_CALL_OPPONENTS, callOpponents)
                bundle.putString(EXTRA_CALL_USER_INFO, userInfo)
                broadcastIntent.putExtras(bundle)

                LocalBroadcastManager.getInstance(context.applicationContext)
                    .sendBroadcast(broadcastIntent)

                NotificationManagerCompat.from(context).cancel(callId.hashCode())
            }

            ACTION_CALL_ACCEPT -> {
                stopNotificationSound(context);
                val extras = intent.extras
                val callId = extras?.getString(EXTRA_CALL_ID)
                val callType = extras?.getInt(EXTRA_CALL_TYPE)
                val callInitiatorId = extras?.getInt(EXTRA_CALL_INITIATOR_ID)
                val callInitiatorName = extras?.getString(EXTRA_CALL_INITIATOR_NAME)
                val callOpponents = extras?.getIntegerArrayList(EXTRA_CALL_OPPONENTS)
                val userInfo = extras?.getString(EXTRA_CALL_USER_INFO)
                val destinationRoute = extras?.getString(EXTRA_NOTIFICATION_TYPE)
                Log.i(TAG, "NotificationReceiver onReceive Call ACCEPT, callId: $callId")
                Log.i(TAG, "NotificationReceiver onReceive Call ACCEPT, destination route: $destinationRoute")

                val broadcastIntent = Intent(ACTION_CALL_ACCEPT)
                val bundle = Bundle()
                bundle.putString(EXTRA_CALL_ID, callId)
                bundle.putInt(EXTRA_CALL_TYPE, callType!!)
                bundle.putInt(EXTRA_CALL_INITIATOR_ID, callInitiatorId!!)
                bundle.putString(EXTRA_CALL_INITIATOR_NAME, callInitiatorName)
                bundle.putIntegerArrayList(EXTRA_CALL_OPPONENTS, callOpponents)
                bundle.putString(EXTRA_CALL_USER_INFO, userInfo)
                bundle.putString(EXTRA_NOTIFICATION_TYPE, destinationRoute)
                broadcastIntent.putExtras(bundle)

                LocalBroadcastManager.getInstance(context.applicationContext)
                    .sendBroadcast(broadcastIntent)

                NotificationManagerCompat.from(context).cancel(callId.hashCode())
            }

            ACTION_CALL_NOTIFICATION_CANCELED -> {
                stopNotificationSound(context);
                val extras = intent.extras
                val callId = extras?.getString(EXTRA_CALL_ID)
                val callType = extras?.getInt(EXTRA_CALL_TYPE)
                val callInitiatorId = extras?.getInt(EXTRA_CALL_INITIATOR_ID)
                val callInitiatorName = extras?.getString(EXTRA_CALL_INITIATOR_NAME)
                val userInfo = extras?.getString(EXTRA_CALL_USER_INFO)
                Log.i(
                    TAG,
                    "NotificationReceiver onReceive Delete Call Notification, callId: $callId"
                )
                LocalBroadcastManager.getInstance(context.applicationContext)
                    .sendBroadcast(
                        Intent(ACTION_CALL_NOTIFICATION_CANCELED).putExtra(
                            EXTRA_CALL_ID,
                            callId
                        )
                    )
            }
        }
    }

    private fun stopNotificationSound(context:Context){
        val intent = Intent(context, NotificationSoundService::class.java)
        intent.action = NotificationSoundService.ACTION_STOP_PLAYBACK;
        context.startService(intent)
    }
}