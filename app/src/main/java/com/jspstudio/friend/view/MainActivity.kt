package com.jspstudio.friend.view

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_CONTACTS
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jspstudio.friend.R
import com.jspstudio.friend.model.Location
import com.jspstudio.friend.util.Util
import kotlin.math.*

// 메뉴 : 동네생활, 주변친구
// 1. 사용자간 거리 비교
// 2. 채팅, 댓글
// 3. 동네생활
// 4. sns로그인

class MainActivity : AppCompatActivity() {
    private lateinit var contactsListView: ListView

    companion object {
        private const val PERMISSION_REQUEST = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactsListView = findViewById(R.id.list_view)

        val seoul = Location(37.5665, 126.9780)
        val daejeon = Location(36.3504, 127.3845)

        val distance = Util.calculateDistanceBetweenLocations(seoul, daejeon)
        println("서울과 대전 간의 거리: $distance km")

        requestPermission(arrayListOf(READ_CONTACTS, ACCESS_FINE_LOCATION), PERMISSION_REQUEST){
            getList()
        }
    }

    private fun requestPermission(permissions: ArrayList<String>, requestCode: Int, onPermissionGranted: () -> Unit) {

        // 모든 권한이 부여되어 있는지 확인
        val permissionsToRequest = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        if (permissionsToRequest.isEmpty()) {
            // 모든 권한이 이미 허용된 경우
            onPermissionGranted()
        } else {
            // 권한을 요청
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // READ_CONTACTS 권한이 사용자에 의해 허용됨
                    // 수행할 작업

                }
            }
        }
    }


    private fun getList(){
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Util.loadContacts(this))
        contactsListView.adapter = adapter
    }

}