package com.example.geofencingproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            // 여기서 에러 처리를 합니다.
            return
        }

        // 지오펜싱 이벤트의 종류를 얻습니다.
        val transition = geofencingEvent.geofenceTransition

        when (transition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                // 지오펜싱 영역에 진입했을 때의 처리
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                // 지오펜싱 영역에서 나갔을 때의 처리
            }
            else -> {
                // 무시
            }
        }
    }
}