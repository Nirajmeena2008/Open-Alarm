package com.alarmapp.auto

import android.content.Intent
import androidx.car.app.CarAppService
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.model.*
import androidx.car.app.validation.HostValidator

class AlarmCarAppService : CarAppService() {

    override fun createHostValidator(): HostValidator {
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
    }

    override fun onCreateSession(): Session {
        return object : Session() {
            override fun onCreateScreen(intent: Intent): Screen {
                return AlarmCarScreen(carContext)
            }
        }
    }
}

class AlarmCarScreen(carContext: CarContext) : Screen(carContext) {

    override fun onGetTemplate(): Template {
        val row = Row.Builder()
            .setTitle("Next Alarm: 07:00 AM")
            .addText("Work Alarm (Active)")
            .build()

        val pane = Pane.Builder()
            .addRow(row)
            .addAction(
                Action.Builder()
                    .setTitle("Dismiss")
                    .setOnClickListener {
                        // Dismiss action
                    }
                    .build()
            )
            .build()

        return PaneTemplate.Builder(pane)
            .setHeaderAction(Action.APP_ICON)
            .setTitle("OpenAlarm")
            .build()
    }
}
