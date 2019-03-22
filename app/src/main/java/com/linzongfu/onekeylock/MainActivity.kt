package com.linzongfu.onekeylock

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 *由于Android系统安全设置问题，7.0以上版本的系统，点击此进行锁屏之后，只能使用密码解锁，而不能使用指纹和人脸等
 */
open class MainActivity : AppCompatActivity() {

    private val MY_REQUEST_CODE = 9999
    private var policyManager: DevicePolicyManager? = null
    private var lockComponentName: ComponentName? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setTheme(R.style.translucent)    //设置透明的Activity
//        transParentStatusBarAndBottomNavigationBar()   //透明状态栏和底部导航栏

        //获取设备管理服务
        policyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        //AdminReceiver 继承自 DeviceAdminReceiver
        lockComponentName = ComponentName(this, AdminReceiver::class.java)
        lockScreen()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("onActivityResult", "RESULT")
//        没收到设备激活的回调，为何？
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.e("onActivityResult", "RESULT_OK")
            Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show()
            if (!policyManager!!.isAdminActive(lockComponentName!!)) {   //若无权限
                killSelf()
            } else {
                Toast.makeText(this, "设置完成，请尝试锁屏", Toast.LENGTH_SHORT).show()
                finish()
//                policyManager!!.lockNow()//直接锁屏
            }
        } else {
            killSelf()
        }
    }

    /**
     * 锁屏
     */
    private fun lockScreen() {
        val active = policyManager!!.isAdminActive(lockComponentName!!)
        if (!active) {   //若无权限
            activeManage()//去获得权限
        } else {
            policyManager!!.lockNow()//直接锁屏
        }
        //killSelf ，锁屏之后就立即kill掉我们的Activity，避免资源的浪费;
        killSelf()
    }

    /**
     * 激活设备
     */
    private fun activeManage() {
        //启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        //权限列表
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, lockComponentName)
        //描述(additional explanation)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "激活后才可以使用锁屏功能 ")
        startActivityForResult(intent, MY_REQUEST_CODE)
    }

    /**
     * kill自己
     */
    private fun killSelf() {
        //killMyself ，锁屏之后就立即kill掉我们的Activity，避免资源的浪费;
        android.os.Process.killProcess(android.os.Process.myPid())
        finish()
    }
}
