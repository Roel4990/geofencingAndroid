package com.example.geofencingproject

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.geofencingproject.databinding.ActivityMainBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.webkit.WebChromeClient
import android.webkit.WebViewClient

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private var target_url = "http://192.168.20.240:3000"
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var binding: ActivityMainBinding

    // Geofencing 권한
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webView = binding.webView

        geofencingClient = LocationServices.getGeofencingClient(this)
        checkAndRequestLocationPermission()
        // WebView 설정
        webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
            // 웹페이지 불러오기
            loadUrl(target_url)
        }
        // 지오펜스 설정: 해당 예제에서는 특정 위도, 경도를 중심으로 반경 100m의 원 형태로 지오펜싱 영역을 정의합니다.
        val geofence = Geofence.Builder()
            .setRequestId("myGeofenceId")  // 지오펜스 ID 지정
            .setCircularRegion(
                37.422,   // latitude
                -122.084, // longitude
                100f      // radius in meters
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE) // 만료 시간 설정. 이 경우에는 만료하지 않도록 설정됨
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT) // 어떤 전이를 감지할지 설정
            .build()

        // 지오펜싱 요청 객체 생성
        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)  // 초기 트리거 설정
            .addGeofence(geofence)
            .build()

        // 지오펜싱 이벤트를 받을 BroadcastReceiver를 위한 PendingIntent 설정
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


        // 지오펜싱 추가
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)?.run {
            addOnSuccessListener {
                // 지오펜싱이 성공적으로 추가됨
            }
            addOnFailureListener {
                // 지오펜싱 추가 실패
            }
        }
    }
    private fun checkAndRequestLocationPermission() {
        // 권한 확인 및 요청 코드
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }
    // onRequestPermissionsResult 메서드에서 사용자의 응답을 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // todo 권한이 승인될 경우 코드
                } else {
                    // todo 권한이 거부될 경우 코드
                }
            }
        }
    }
}