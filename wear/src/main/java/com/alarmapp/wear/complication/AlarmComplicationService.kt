package com.alarmapp.wear.complication

import androidx.wear.watchface.complications.data.*
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceService
import androidx.wear.watchface.complications.datasource.ComplicationRequest

class AlarmComplicationService : ComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        if (type != ComplicationType.SHORT_TEXT) return null
        return ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder("07:00").build(),
            contentDescription = PlainComplicationText.Builder("Next Alarm").build()
        ).build()
    }

    override fun onComplicationRequest(
        request: ComplicationRequest,
        listener: ComplicationRequestListener
    ) {
        val data = ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder("07:00").build(),
            contentDescription = PlainComplicationText.Builder("Next Alarm").build()
        ).build()
        listener.onComplicationData(data)
    }
}
