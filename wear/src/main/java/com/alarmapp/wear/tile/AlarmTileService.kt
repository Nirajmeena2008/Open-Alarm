package com.alarmapp.wear.tile

import androidx.wear.tiles.DimensionBuilders.dp
import androidx.wear.tiles.DimensionBuilders.sp
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import androidx.wear.tiles.TimelineBuilders.Timeline
import androidx.wear.tiles.TimelineBuilders.TimelineEntry
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class AlarmTileService : TileService() {

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<TileBuilders.Tile> {
        val rootLayout = LayoutElementBuilders.Layout.Builder()
            .setRoot(
                LayoutElementBuilders.Column.Builder()
                    .addContent(
                        LayoutElementBuilders.Text.Builder()
                            .setText("NEXT ALARM")
                            .setFontStyle(LayoutElementBuilders.FontStyle.Builder().setSize(sp(12f)).build())
                            .build()
                    )
                    .addContent(
                        LayoutElementBuilders.Spacer.Builder().setHeight(dp(4f)).build()
                    )
                    .addContent(
                        LayoutElementBuilders.Text.Builder()
                            .setText("07:00 AM")
                            .setFontStyle(LayoutElementBuilders.FontStyle.Builder().setSize(sp(24f)).build())
                            .build()
                    )
                    .build()
            )
            .build()

        val tile = TileBuilders.Tile.Builder()
            .setResourcesVersion("1")
            .setTimeline(
                Timeline.Builder()
                    .addTimelineEntry(
                        TimelineEntry.Builder()
                            .setLayout(rootLayout)
                            .build()
                    )
                    .build()
            )
            .build()

        return Futures.immediateFuture(tile)
    }

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ListenableFuture<ResourceBuilders.Resources> {
        return Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion("1")
                .build()
        )
    }
}
