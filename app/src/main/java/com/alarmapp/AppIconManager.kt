package com.alarmapp

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

object AppIconManager {
    fun updateAppIcon(context: Context, themeMode: String) {
        try {
            val pm = context.packageManager
            val packageName = context.packageName
            val lightAlias = ComponentName(packageName, "$packageName.MainActivityLight")
            val darkAlias = ComponentName(packageName, "$packageName.MainActivityDark")

            val isLight = themeMode == "Light"

            val targetLightState = if (isLight) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }

            val targetDarkState = if (isLight) {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            }

            if (pm.getComponentEnabledSetting(lightAlias) != targetLightState) {
                pm.setComponentEnabledSetting(lightAlias, targetLightState, PackageManager.DONT_KILL_APP)
            }
            if (pm.getComponentEnabledSetting(darkAlias) != targetDarkState) {
                pm.setComponentEnabledSetting(darkAlias, targetDarkState, PackageManager.DONT_KILL_APP)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
