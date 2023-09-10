package com.jspstudio.friend.util

import android.R
import android.content.Context
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import com.jspstudio.friend.model.Location
import kotlin.math.*

object Util {
    /**
    * 디바이스에 저장된 연락처목록 가져오는 메소드
    * */
    fun loadContacts(context: Context): ArrayList<String> {
        val contactsList = ArrayList<String>()

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val nameColumn = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberColumn = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val contactName = it.getString(nameColumn)
                val phoneNumber = it.getString(numberColumn).replace("/", "")
                contactsList.add("$contactName: $phoneNumber")
            }
        }

        return contactsList
    }

    /**
    * 지역간의 거리 계산
    * */
    fun calculateDistanceBetweenLocations(location1: Location, location2: Location): Double {
        val earthRadius = 6371 // 지구 반지름 (단위: km)

        val lat1 = Math.toRadians(location1.latitude)
        val lon1 = Math.toRadians(location1.longitude)
        val lat2 = Math.toRadians(location2.latitude)
        val lon2 = Math.toRadians(location2.longitude)

        val dlon = lon2 - lon1
        val dlat = lat2 - lat1

        val a = sin(dlat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dlon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }
}