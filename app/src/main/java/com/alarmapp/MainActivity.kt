package com.alarmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.alarmapp.feature.alarm.ui.editor.AlarmEditorScreen
import com.alarmapp.feature.alarm.ui.editor.AlarmEditorViewModel
import com.alarmapp.feature.alarm.ui.list.AlarmListScreen
import com.alarmapp.feature.alarm.ui.list.AlarmListViewModel
import com.alarmapp.feature.alarm.ui.theme.AlarmTheme
import com.alarmapp.feature.developer.ui.DeveloperSettingsScreen
import com.alarmapp.feature.developer.ui.DeveloperViewModel
import com.alarmapp.feature.onboarding.ui.OnboardingScreen
import com.alarmapp.feature.settings.ui.SettingsScreen
import com.alarmapp.feature.settings.ui.SettingsViewModel
import com.alarmapp.feature.sleep.ui.RoutinePlayerScreen
import com.alarmapp.feature.sleep.ui.SleepTabScreen
import com.alarmapp.feature.sleep.ui.SleepViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    data object Alarms : Screen("alarms", "Alarms", Icons.Default.Alarm)
    data object Sleep : Screen("sleep", "Sleep", Icons.Default.Bedtime)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    data object Editor : Screen("editor/{alarmId}", "Editor")
    data object Developer : Screen("developer", "Developer")
    data object Onboarding : Screen("onboarding", "Onboarding")
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as AlarmApplication

        setContent {
            AlarmTheme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                val bottomNavItems = listOf(
                    Screen.Alarms,
                    Screen.Sleep,
                    Screen.Settings
                )

                val showBottomBar = currentRoute in listOf(
                    Screen.Alarms.route,
                    Screen.Sleep.route,
                    Screen.Settings.route
                )

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar {
                                bottomNavItems.forEach { item ->
                                    val isSelected = currentRoute == item.route
                                    NavigationBarItem(
                                        icon = { item.icon?.let { Icon(it, contentDescription = item.title) } },
                                        label = { Text(item.title) },
                                        selected = isSelected,
                                        onClick = {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Alarms.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Alarms Tab
                        composable(Screen.Alarms.route) {
                            val listViewModel = remember {
                                AlarmListViewModel(
                                    app.alarmRepository,
                                    app.getAlarmsUseCase,
                                    app.toggleAlarmUseCase,
                                    app.deleteAlarmUseCase,
                                    app.alarmScheduler
                                )
                            }
                            val uiState by listViewModel.uiState.collectAsState()

                            AlarmListScreen(
                                uiState = uiState,
                                onToggleAlarm = { id, enabled -> listViewModel.toggleAlarm(id, enabled) },
                                onDeleteAlarm = { id -> listViewModel.deleteAlarm(id) },
                                onEditAlarm = { id -> navController.navigate("editor/$id") },
                                onAddAlarm = { navController.navigate("editor/new") }
                            )
                        }

                        // Alarm Editor
                        composable(
                            route = Screen.Editor.route,
                            arguments = listOf(navArgument("alarmId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val alarmId = backStackEntry.arguments?.getString("alarmId")
                            val editorViewModel = remember {
                                AlarmEditorViewModel(
                                    app.alarmRepository,
                                    app.saveAlarmUseCase,
                                    app.alarmScheduler
                                )
                            }
                            LaunchedEffect(alarmId) {
                                editorViewModel.loadAlarm(alarmId)
                            }
                            val state by editorViewModel.state.collectAsState()

                            AlarmEditorScreen(
                                state = state,
                                onTimeChange = { h, m -> editorViewModel.setTime(h, m) },
                                onToggleDay = { day -> editorViewModel.toggleDay(day) },
                                onLabelChange = { l -> editorViewModel.setLabel(l) },
                                onChallengeChange = { c -> editorViewModel.setChallenge(c) },
                                onVolumeRampChange = { r -> editorViewModel.setVolumeRamp(r) },
                                onVibrationChange = { v -> editorViewModel.setVibration(v) },
                                onSnoozeDurationChange = { d -> editorViewModel.setSnoozeDuration(d) },
                                onSave = {
                                    editorViewModel.saveAlarm {
                                        navController.popBackStack()
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // Sleep Tab
                        composable(Screen.Sleep.route) {
                            val sleepViewModel = remember {
                                SleepViewModel(
                                    app.sleepRepository,
                                    app.routineRepository,
                                    app.alarmRepository,
                                    app.startSleepSessionUseCase,
                                    app.endSleepSessionUseCase,
                                    app.getSleepHistoryUseCase,
                                    app.routineUseCases
                                )
                            }
                            val sleepUiState by sleepViewModel.uiState.collectAsState()

                            if (sleepUiState.activeRoutine != null) {
                                RoutinePlayerScreen(
                                    routine = sleepUiState.activeRoutine!!,
                                    currentStepIndex = sleepUiState.currentStepIndex,
                                    onNextStep = { sleepViewModel.nextRoutineStep() },
                                    onStopRoutine = { sleepViewModel.stopRoutine() }
                                )
                            } else {
                                SleepTabScreen(
                                    uiState = sleepUiState,
                                    onStartSleep = { sleepViewModel.startSleep() },
                                    onEndSleep = { sleepViewModel.endSleep() },
                                    onStartRoutine = { r -> sleepViewModel.startRoutine(r) }
                                )
                            }
                        }

                        // Settings Tab
                        composable(Screen.Settings.route) {
                            val settingsViewModel = remember {
                                SettingsViewModel(
                                    this@MainActivity,
                                    app.settingsRepository,
                                    app.alarmRepository,
                                    app.routineRepository,
                                    app.backupManager
                                )
                            }
                            val settingsState by settingsViewModel.uiState.collectAsState()

                            SettingsScreen(
                                uiState = settingsState,
                                onSetTheme = { t -> settingsViewModel.setTheme(t) },
                                onSetSnoozeDuration = { s -> settingsViewModel.setSnoozeDuration(s) },
                                onExportBackup = { pwd -> settingsViewModel.exportBackup(pwd) {} },
                                onImportBackup = { pwd -> settingsViewModel.importBackup(ByteArray(0), pwd) },
                                onNavigateToDeveloper = { navController.navigate(Screen.Developer.route) }
                            )
                        }

                        // Developer Screen
                        composable(Screen.Developer.route) {
                            val devViewModel = remember {
                                DeveloperViewModel(app.webhookRepository)
                            }
                            val devState by devViewModel.uiState.collectAsState()

                            DeveloperSettingsScreen(
                                uiState = devState,
                                onToggleServer = { enable -> devViewModel.toggleServer(enable) },
                                onGenerateApiKey = { devViewModel.generateNewApiKey() },
                                onAddWebhook = { url, sec -> devViewModel.addWebhook(url, sec) },
                                onDeleteWebhook = { id -> devViewModel.deleteWebhook(id) }
                            )
                        }

                        // Onboarding Screen
                        composable(Screen.Onboarding.route) {
                            OnboardingScreen(
                                onComplete = {
                                    navController.navigate(Screen.Alarms.route) {
                                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
