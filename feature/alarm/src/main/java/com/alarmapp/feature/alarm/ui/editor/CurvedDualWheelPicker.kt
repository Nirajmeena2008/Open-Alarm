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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
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

    // Styling matching reference design
    val screenBg = if (isLight) Color(0xFFE8E8ED) else Color(0xFF121214)
    val wheelColor = if (isLight) Color(0xFFFFFFFF) else Color(0xFF1C1C1E)
    val capsuleBorder = if (isLight) Color(0x38000000) else Color(0x44FFFFFF)
    val capsuleFill = if (isLight) Color(0x06000000) else Color(0x08FFFFFF)
    val textColor = if (isLight) Color(0xFF111111) else Color(0xFFFFFFFF)

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
        val capsuleWidthPx = with(density) { 184.dp.toPx() }
        val capsuleHeightPx = with(density) { 72.dp.toPx() }
        val capsuleCornerRadius = with(density) { 18.dp.toPx() }

        // 1. Draw curved silhouette background discs and center selection capsule
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
        }

        // 2. Left Column: Hours (00..23)
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
                onValueChange = onHourChange,
                feedback = feedback
            )
        }

        // 3. Right Column: Minutes (00..59)
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

    // Geometry within this half:
    // If isLeft: peak is at halfWidthPx - centerGapPx, circle center is at peak - radiusPx
    // If !isLeft: peak is at centerGapPx, circle center is at peak + radiusPx
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
                            // Dragging down brings upper (larger) numbers into center
                            val deltaUnits = dragAmount / itemStepPx
                            val newPos = scrollPos.value + deltaUnits
                            scrollPos.snapTo(newPos)

                            // Accumulate movement for degree-by-degree "toci" sound
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

        // Visible range of numbers around the center
        val visibleRange = -4..4

        visibleRange.forEach { offset ->
            val numValueFloat = currentOffset + offset
            val nearestInt = round(numValueFloat).toInt()
            val numDisplay = ((nearestInt % maxCount) + maxCount) % maxCount

            // Offset positive (larger number) appears above center (y < midY)
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

                // y goes up for positive offset
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
