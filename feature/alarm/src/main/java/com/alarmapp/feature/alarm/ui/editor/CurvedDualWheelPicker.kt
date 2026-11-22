package com.alarmapp.feature.alarm.ui.editor

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.*

@Composable
fun CurvedDualWheelTimePicker(
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val feedback = rememberDialTickFeedback()
    val isLight = MaterialTheme.colorScheme.background == com.alarmapp.feature.alarm.ui.theme.MinimalBackground

    // Refined color palette for high-end dial design
    val screenBg = if (isLight) Color(0xFFE8E8ED) else Color(0xFF101012)
    val wheelColor = if (isLight) Color(0xFFFFFFFF) else Color(0xFF1B1B1E)
    val capsuleBorder = if (isLight) Color(0x44000000) else Color(0x4DFFFFFF)
    val capsuleFill = if (isLight) Color(0x06000000) else Color(0x0AFFFFFF)
    val textColor = if (isLight) Color(0xFF111111) else Color(0xFFFFFFFF)
    val dialTrackColor = if (isLight) Color(0x1F000000) else Color(0x24FFFFFF)
    val labelColor = if (isLight) Color(0xFF71717A) else Color(0xFF9CA3AF)
    val accentRed = Color(0xFFFF3B30)

    // Calculate time until alarm fires
    val timeRemaining = remember(hour, minute) {
        val now = Calendar.getInstance()
        val curMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
        val targetMinutes = hour * 60 + minute
        var diff = targetMinutes - curMinutes
        if (diff <= 0) diff += 24 * 60
        val h = diff / 60
        val m = diff % 60
        when {
            h > 0 && m > 0 -> "Rings in ${h}h ${m}m"
            h > 0 -> "Rings in ${h}h"
            else -> "Rings in ${m}m"
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(screenBg),
        contentAlignment = Alignment.Center
    ) {
        val totalWidth = maxWidth
        val totalHeight = maxHeight
        val density = LocalDensity.current

        val widthPx = with(density) { totalWidth.toPx() }
        val heightPx = with(density) { totalHeight.toPx() }
        val midX = widthPx / 2f
        val midY = heightPx / 2f

        // Geometry parameters
        val radiusDp = 380.dp
        val radiusPx = with(density) { radiusDp.toPx() }
        val centerGapPx = with(density) { 3.dp.toPx() }
        val peakLeftX = midX - centerGapPx
        val centerLeftX = peakLeftX - radiusPx

        val peakRightX = midX + centerGapPx
        val centerRightX = peakRightX + radiusPx

        // Capsule dimensions
        val capsuleWidthPx = with(density) { 186.dp.toPx() }
        val capsuleHeightPx = with(density) { 74.dp.toPx() }
        val capsuleCornerRadius = with(density) { 18.dp.toPx() }

        // 1. Draw curved silhouette background discs, instrument track rings, and center capsule
        Canvas(modifier = Modifier.fillMaxSize()) {
            val safeHalfH = min(midY, radiusPx * 0.95f)
            val halfAngleRad = asin(safeHalfH / radiusPx)
            val halfAngleDeg = Math.toDegrees(halfAngleRad.toDouble()).toFloat()

            // Left Wheel Disc (Hours)
            val leftDisc = Path().apply {
                moveTo(0f, 0f)
                val topX = centerLeftX + radiusPx * cos(halfAngleRad)
                lineTo(topX, midY - safeHalfH)
                arcTo(
                    rect = Rect(
                        left = centerLeftX - radiusPx,
                        top = midY - radiusPx,
                        right = centerLeftX + radiusPx,
                        bottom = midY + radiusPx
                    ),
                    startAngleDegrees = -halfAngleDeg,
                    sweepAngleDegrees = 2 * halfAngleDeg,
                    forceMoveTo = false
                )
                lineTo(0f, heightPx)
                close()
            }
            drawPath(leftDisc, color = wheelColor)

            // Right Wheel Disc (Minutes)
            val rightDisc = Path().apply {
                moveTo(widthPx, 0f)
                val topX = centerRightX - radiusPx * cos(halfAngleRad)
                lineTo(topX, midY - safeHalfH)
                arcTo(
                    rect = Rect(
                        left = centerRightX - radiusPx,
                        top = midY - radiusPx,
                        right = centerRightX + radiusPx,
                        bottom = midY + radiusPx
                    ),
                    startAngleDegrees = 180f + halfAngleDeg,
                    sweepAngleDegrees = -2 * halfAngleDeg,
                    forceMoveTo = false
                )
                lineTo(widthPx, heightPx)
                close()
            }
            drawPath(rightDisc, color = wheelColor)

            // Inner Accent Concentric Gauge Tracks (Aeronautical / Chronograph instrument rings)
            val leftInnerTrackR1 = radiusPx - with(density) { 86.dp.toPx() }
            val leftInnerTrackR2 = radiusPx - with(density) { 128.dp.toPx() }

            // Dashed inner gauge arc for left wheel
            drawArc(
                color = dialTrackColor,
                startAngle = -halfAngleDeg * 0.9f,
                sweepAngle = 2 * halfAngleDeg * 0.9f,
                useCenter = false,
                topLeft = Offset(centerLeftX - leftInnerTrackR1, midY - leftInnerTrackR1),
                size = Size(leftInnerTrackR1 * 2, leftInnerTrackR1 * 2),
                style = Stroke(
                    width = with(density) { 1.dp.toPx() },
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f))
                )
            )
            drawArc(
                color = dialTrackColor.copy(alpha = dialTrackColor.alpha * 0.6f),
                startAngle = -halfAngleDeg * 0.85f,
                sweepAngle = 2 * halfAngleDeg * 0.85f,
                useCenter = false,
                topLeft = Offset(centerLeftX - leftInnerTrackR2, midY - leftInnerTrackR2),
                size = Size(leftInnerTrackR2 * 2, leftInnerTrackR2 * 2),
                style = Stroke(width = with(density) { 0.75f.dp.toPx() })
            )

            // Dashed inner gauge arc for right wheel
            val rightInnerTrackR1 = radiusPx - with(density) { 86.dp.toPx() }
            val rightInnerTrackR2 = radiusPx - with(density) { 128.dp.toPx() }

            drawArc(
                color = dialTrackColor,
                startAngle = 180f - halfAngleDeg * 0.9f,
                sweepAngle = 2 * halfAngleDeg * 0.9f,
                useCenter = false,
                topLeft = Offset(centerRightX - rightInnerTrackR1, midY - rightInnerTrackR1),
                size = Size(rightInnerTrackR1 * 2, rightInnerTrackR1 * 2),
                style = Stroke(
                    width = with(density) { 1.dp.toPx() },
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f))
                )
            )
            drawArc(
                color = dialTrackColor.copy(alpha = dialTrackColor.alpha * 0.6f),
                startAngle = 180f - halfAngleDeg * 0.85f,
                sweepAngle = 2 * halfAngleDeg * 0.85f,
                useCenter = false,
                topLeft = Offset(centerRightX - rightInnerTrackR2, midY - rightInnerTrackR2),
                size = Size(rightInnerTrackR2 * 2, rightInnerTrackR2 * 2),
                style = Stroke(width = with(density) { 0.75f.dp.toPx() })
            )

            // Outer Perimeter Rim Hairlines
            drawArc(
                color = dialTrackColor,
                startAngle = -halfAngleDeg,
                sweepAngle = 2 * halfAngleDeg,
                useCenter = false,
                topLeft = Offset(centerLeftX - radiusPx, midY - radiusPx),
                size = Size(radiusPx * 2, radiusPx * 2),
                style = Stroke(width = with(density) { 1.dp.toPx() })
            )
            drawArc(
                color = dialTrackColor,
                startAngle = 180f - halfAngleDeg,
                sweepAngle = 2 * halfAngleDeg,
                useCenter = false,
                topLeft = Offset(centerRightX - radiusPx, midY - radiusPx),
                size = Size(radiusPx * 2, radiusPx * 2),
                style = Stroke(width = with(density) { 1.dp.toPx() })
            )

            // Center Selection Capsule Box (Spans across both digits)
            val capsuleLeft = midX - capsuleWidthPx / 2f
            val capsuleTop = midY - capsuleHeightPx / 2f

            drawRoundRect(
                color = capsuleFill,
                topLeft = Offset(capsuleLeft, capsuleTop),
                size = Size(capsuleWidthPx, capsuleHeightPx),
                cornerRadius = CornerRadius(capsuleCornerRadius, capsuleCornerRadius)
            )

            drawRoundRect(
                color = capsuleBorder,
                topLeft = Offset(capsuleLeft, capsuleTop),
                size = Size(capsuleWidthPx, capsuleHeightPx),
                cornerRadius = CornerRadius(capsuleCornerRadius, capsuleCornerRadius),
                style = Stroke(
                    width = with(density) { 1.5.dp.toPx() },
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(
                            with(density) { 5.dp.toPx() },
                            with(density) { 4.dp.toPx() }
                        )
                    )
                )
            )

            // Center Viewfinder Separator: subtle vertical hair divider with center red pip
            val divHeight = capsuleHeightPx * 0.5f
            drawLine(
                color = capsuleBorder.copy(alpha = 0.25f),
                start = Offset(midX, midY - divHeight / 2f),
                end = Offset(midX, midY + divHeight / 2f),
                strokeWidth = with(density) { 1.dp.toPx() }
            )

            // Precision red center dot
            drawCircle(
                color = accentRed,
                radius = with(density) { 3.dp.toPx() },
                center = Offset(midX, midY)
            )
        }

        // 2. Floating Header Accent Elements
        // Time remaining pill at top center
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isLight) Color(0x18000000) else Color(0x33FFFFFF),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(accentRed, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = timeRemaining,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor.copy(alpha = 0.85f),
                    letterSpacing = 0.3.sp
                )
            }
        }

        // Column Labels (HOURS / MINUTES)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 58.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "• H O U R •",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = labelColor.copy(alpha = 0.75f),
                letterSpacing = 1.8.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "• M I N U T E •",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = labelColor.copy(alpha = 0.75f),
                letterSpacing = 1.8.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }

        // 3. Left Column: Hours (00..23) with Precision Radial Bezel Ticks
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(totalWidth / 2)
                .align(Alignment.CenterStart)
        ) {
            CurvedWheelHalf(
                initialValue = hour,
                maxCount = 24,
                isLeft = true,
                halfWidthPx = midX,
                radiusPx = radiusPx,
                midY = midY,
                centerGapPx = centerGapPx,
                textColor = textColor,
                tickColor = if (isLight) Color(0xFF55555A) else Color(0xFFAAAAAF),
                onValueChange = onHourChange,
                feedback = feedback
            )
        }

        // 4. Right Column: Minutes (00..59) with Precision Radial Bezel Ticks
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(totalWidth / 2)
                .align(Alignment.CenterEnd)
        ) {
            CurvedWheelHalf(
                initialValue = minute,
                maxCount = 60,
                isLeft = false,
                halfWidthPx = midX,
                radiusPx = radiusPx,
                midY = midY,
                centerGapPx = centerGapPx,
                textColor = textColor,
                tickColor = if (isLight) Color(0xFF55555A) else Color(0xFFAAAAAF),
                onValueChange = onMinuteChange,
                feedback = feedback
            )
        }
    }
}

@Composable
private fun BoxScope.CurvedWheelHalf(
    initialValue: Int,
    maxCount: Int,
    isLeft: Boolean,
    halfWidthPx: Float,
    radiusPx: Float,
    midY: Float,
    centerGapPx: Float,
    textColor: Color,
    tickColor: Color,
    onValueChange: (Int) -> Unit,
    feedback: DialTickFeedback
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollPos = remember { Animatable(initialValue.toFloat()) }
    val density = LocalDensity.current

    val itemStepDp = 54.dp
    val itemStepPx = with(density) { itemStepDp.toPx() }
    val marginPx = with(density) { 46.dp.toPx() }

    // Degree resolution: each full unit step is 30 degrees, so 1 degree = itemStepPx / 30f
    val pixelsPerDegree = itemStepPx / 30f
    var accumulatedDragForTicks by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(initialValue) {
        val currentMod = ((scrollPos.value.roundToInt() % maxCount) + maxCount) % maxCount
        if (currentMod != initialValue) {
            scrollPos.snapTo(initialValue.toFloat())
        }
    }

    val peakX = if (isLeft) halfWidthPx - centerGapPx else centerGapPx
    val circleCenterX = if (isLeft) peakX - radiusPx else peakX + radiusPx

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(maxCount) {
                detectVerticalDragGestures(
                    onDragStart = {
                        accumulatedDragForTicks = 0f
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            val target = scrollPos.value.roundToInt().toFloat()
                            scrollPos.animateTo(
                                targetValue = target,
                                animationSpec = spring(
                                    dampingRatio = 0.8f,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            ) {
                                val currentVal = this.value
                                val delta = currentVal * itemStepPx
                                if (abs(delta - accumulatedDragForTicks) >= pixelsPerDegree) {
                                    feedback.playTick(if (isLeft) 1.05f else 0.95f)
                                    accumulatedDragForTicks = delta
                                }
                            }
                            val finalVal = ((target.roundToInt() % maxCount) + maxCount) % maxCount
                            onValueChange(finalVal)
                        }
                    },
                    onVerticalDrag = { _, dragAmount: Float ->
                        coroutineScope.launch {
                            val deltaUnits = dragAmount / itemStepPx
                            val newPos = scrollPos.value + deltaUnits
                            scrollPos.snapTo(newPos)

                            accumulatedDragForTicks += dragAmount
                            while (abs(accumulatedDragForTicks) >= pixelsPerDegree) {
                                feedback.playTick(if (isLeft) 1.05f else 0.95f)
                                accumulatedDragForTicks -= sign(accumulatedDragForTicks) * pixelsPerDegree
                            }

                            val currentInt = ((newPos.roundToInt() % maxCount) + maxCount) % maxCount
                            onValueChange(currentInt)
                        }
                    }
                )
            }
    ) {
        val currentOffset = scrollPos.value

        // Draw mechanical watch bezel tick marks along the circular arc
        Canvas(modifier = Modifier.fillMaxSize()) {
            val totalSubSteps = 24 // Sub-tick divisions spanning above and below
            for (i in -totalSubSteps..totalSubSteps) {
                val subFraction = i * 0.2f // 5 ticks per integer number
                val subVal = currentOffset + subFraction
                val roundedSubVal = (round(subVal * 5f) / 5f)
                val dy = (roundedSubVal - currentOffset) * itemStepPx

                if (abs(dy) < radiusPx * 0.92f) {
                    val angleRad = asin(-dy / radiusPx)
                    val dx = radiusPx * cos(angleRad)
                    val rimX = if (isLeft) circleCenterX + dx else circleCenterX - dx
                    val rimY = midY + dy

                    val isMajor = abs(roundedSubVal - round(roundedSubVal)) < 0.05f
                    val isHalf = !isMajor && abs(roundedSubVal * 2f - round(roundedSubVal * 2f)) < 0.05f

                    val tickLen = when {
                        isMajor -> with(density) { 10.dp.toPx() }
                        isHalf -> with(density) { 6.5.dp.toPx() }
                        else -> with(density) { 4.dp.toPx() }
                    }

                    val tickStroke = when {
                        isMajor -> with(density) { 1.5.dp.toPx() }
                        isHalf -> with(density) { 1.2.dp.toPx() }
                        else -> with(density) { 0.8.dp.toPx() }
                    }

                    val distAlpha = 1f - (abs(dy) / (radiusPx * 0.92f)).pow(1.5f)
                    val alpha = when {
                        isMajor -> 0.70f * distAlpha
                        isHalf -> 0.45f * distAlpha
                        else -> 0.22f * distAlpha
                    }

                    val endX = if (isLeft) {
                        rimX - tickLen * cos(angleRad)
                    } else {
                        rimX + tickLen * cos(angleRad)
                    }
                    val endY = rimY - tickLen * sin(angleRad)

                    drawLine(
                        color = tickColor.copy(alpha = alpha),
                        start = Offset(rimX, rimY),
                        end = Offset(endX, endY),
                        strokeWidth = tickStroke
                    )
                }
            }
        }

        // Render Numbers
        val visibleRange = -4..4

        visibleRange.forEach { offset ->
            val numValueFloat = currentOffset + offset
            val nearestInt = round(numValueFloat).toInt()
            val numDisplay = ((nearestInt % maxCount) + maxCount) % maxCount

            val dy = (nearestInt - currentOffset) * itemStepPx

            if (abs(dy) < radiusPx * 0.92f) {
                val dx = sqrt(radiusPx * radiusPx - dy * dy)
                val arcX = if (isLeft) {
                    circleCenterX + dx
                } else {
                    circleCenterX - dx
                }

                val numCenterX = if (isLeft) {
                    arcX - marginPx
                } else {
                    arcX + marginPx
                }

                val distFromCenter = abs(dy)
                val alpha = when {
                    distFromCenter < itemStepPx * 0.35f -> 1.0f
                    distFromCenter < itemStepPx * 1.25f -> 0.70f
                    distFromCenter < itemStepPx * 2.25f -> 0.40f
                    distFromCenter < itemStepPx * 3.25f -> 0.20f
                    else -> 0.08f
                }

                val fontSize = when {
                    distFromCenter < itemStepPx * 0.4f -> 44.sp
                    distFromCenter < itemStepPx * 1.4f -> 32.sp
                    distFromCenter < itemStepPx * 2.4f -> 27.sp
                    else -> 23.sp
                }

                val fontWeight = if (distFromCenter < itemStepPx * 0.4f) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                }

                val itemY = midY - dy

                Box(
                    modifier = Modifier
                        .offset(
                            x = with(density) { (numCenterX - with(density) { 36.dp.toPx() }).toDp() },
                            y = with(density) { (itemY - with(density) { 26.dp.toPx() }).toDp() }
                        )
                        .size(width = 72.dp, height = 52.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            coroutineScope.launch {
                                scrollPos.animateTo(
                                    targetValue = nearestInt.toFloat(),
                                    animationSpec = spring(
                                        dampingRatio = 0.8f,
                                        stiffness = Spring.StiffnessMediumLow
                                    )
                                )
                                onValueChange(numDisplay)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "%02d".format(numDisplay),
                        fontSize = fontSize,
                        fontWeight = fontWeight,
                        color = textColor.copy(alpha = alpha),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
