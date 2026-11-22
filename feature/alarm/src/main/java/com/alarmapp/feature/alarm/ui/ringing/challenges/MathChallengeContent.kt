package com.alarmapp.feature.alarm.ui.ringing.challenges

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
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
fun MathChallengeContent(
    challenge: RingingChallengeState.MathChallenge,
    onDigitClick: (String) -> Unit,
    onBackspace: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentProblem = challenge.problems.getOrNull(challenge.currentIndex)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress: Problem 1 of 3
        Text(
            text = "Problem ${challenge.currentIndex + 1} of ${challenge.problems.size}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Equation Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (challenge.isError) MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentProblem?.let { "${it.question} = ?" } ?: "Done",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = challenge.currentInput.ifEmpty { "Enter answer" },
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (challenge.currentInput.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.primary
                )
            }
        }

        // Numeric Keypad (1-9, backspace, 0, check)
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            val rows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("DEL", "0", "OK")
            )

            for (row in rows) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (key in row) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    when (key) {
                                        "OK" -> MaterialTheme.colorScheme.primary
                                        "DEL" -> MaterialTheme.colorScheme.surfaceVariant
                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                )
                                .clickable {
                                    when (key) {
                                        "DEL" -> onBackspace()
                                        "OK" -> onSubmit()
                                        else -> onDigitClick(key)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            when (key) {
                                "DEL" -> Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                "OK" -> Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Submit",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                else -> Text(
                                    text = key,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
