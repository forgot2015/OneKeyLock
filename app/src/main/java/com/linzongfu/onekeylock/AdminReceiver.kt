package com.linzongfu.onekeylock

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * 继承DeviceAdminReceiver的广播
 *
 */
class AdminReceiver : DeviceAdminReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        Log.e("AdminReceiver", "接收到广播~")
    }
}
