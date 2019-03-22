package com.linzongfu.onekeylock

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_icon.*

/**
 *选择桌面图标的页面
 */
class IconActivity : AppCompatActivity() {

    private var mPackageManager: PackageManager? = null

    private lateinit var mDefault: ComponentName
    private lateinit var mComponent1: ComponentName
    private lateinit var mComponent2: ComponentName
    private lateinit var mComponent3: ComponentName
    private lateinit var mComponent4: ComponentName
    private lateinit var mComponent5: ComponentName

    private var selectIconIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icon)

        initComponent()

        rgButton.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbIcon1 -> selectIconIndex = 1
                R.id.rbIcon2 -> selectIconIndex = 2
                R.id.rbIcon3 -> selectIconIndex = 3
                R.id.rbIcon4 -> selectIconIndex = 4
                R.id.rbIcon5 -> selectIconIndex = 5
            }
        }

        btnConfirm.setOnClickListener {
            if (selectIconIndex == -1) {
                Toast.makeText(this, getString(R.string.selected_icon_before), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            changeIcon(selectIconIndex)
            Toast.makeText(this, getString(R.string.success_and_wait), Toast.LENGTH_LONG).show()
            finish()
        }
    }


    private fun initComponent() {
        mPackageManager = applicationContext.packageManager

//        修改 MainActivity 的 Alias 入口与icon
        mDefault = ComponentName(
                baseContext,
                "com.linzongfu.onekeylock.MainActivity"
        )
//        若直接取componentName，则修改的是本Activity的Alias 入口与icon
//        mDefault = componentName
        mComponent1 = ComponentName(
                baseContext,
                "com.linzongfu.onekeylock.lock1"
        )
        mComponent2 = ComponentName(
                baseContext,
                "com.linzongfu.onekeylock.lock2"
        )
        mComponent3 = ComponentName(
                baseContext,
                "com.linzongfu.onekeylock.lock3"
        )
        mComponent4 = ComponentName(
                baseContext,
                "com.linzongfu.onekeylock.lock4"
        )
        mComponent5 = ComponentName(
                baseContext,
                "com.linzongfu.onekeylock.lock5"
        )
    }

    private fun changeIcon(iconIndex: Int) {
        disableComponent(mDefault)
        disableComponent(mComponent1)
        disableComponent(mComponent2)
        disableComponent(mComponent3)
        disableComponent(mComponent4)
        disableComponent(mComponent5)

        when (iconIndex) {
            1 -> enableComponent(mComponent1)
            2 -> enableComponent(mComponent2)
            3 -> enableComponent(mComponent3)
            4 -> enableComponent(mComponent4)
            5 -> enableComponent(mComponent5)
        }
    }

    private fun disableComponent(componentName: ComponentName) {
        mPackageManager?.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        )
    }

    private fun enableComponent(componentName: ComponentName) {
        mPackageManager?.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        )
    }
}
