package com.alarmapp.feature.alarm.ui.ringing.challenges

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alarmapp.feature.alarm.ui.ringing.RingingChallengeState

@Composable
fun MemoryChallengeContent(
    challenge: RingingChallengeState.MemoryChallenge,
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Memory Challenge",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Step ${challenge.userSequence.size + 1} of ${challenge.sequence.size}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 3x3 Grid
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            for (row in 0 until 3) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (col in 0 until 3) {
                        val cellIndex = row * 3 + col
                        val isHighlighted = challenge.userSequence.lastOrNull() == cellIndex

                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isHighlighted) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surface
                                )
                                .clickable { onCellClick(cellIndex) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (cellIndex + 1).toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isHighlighted) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
