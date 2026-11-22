package com.alarmapp.feature.developer.server

import com.alarmapp.domain.model.Alarm
import com.alarmapp.domain.model.AlarmId
import com.alarmapp.domain.repository.AlarmRepository
import com.alarmapp.domain.repository.SleepRepository
import com.alarmapp.domain.usecase.alarm.DeleteAlarmUseCase
import com.alarmapp.domain.usecase.alarm.DismissAlarmUseCase
import com.alarmapp.domain.usecase.alarm.SaveAlarmUseCase
import com.alarmapp.domain.usecase.alarm.SnoozeAlarmUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class HealthResponse(
    val status: String,
    val app: String,
    val version: String
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null
)

class DeveloperServer(
    private val port: Int = 18427,
    private val apiKey: String? = null,
    private val alarmRepository: AlarmRepository,
    private val sleepRepository: SleepRepository,
    private val saveAlarmUseCase: SaveAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val snoozeAlarmUseCase: SnoozeAlarmUseCase,
    private val dismissAlarmUseCase: DismissAlarmUseCase
) {
    private var engine: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>? = null

    val isRunning: Boolean get() = engine != null

    fun start() {
        if (engine != null) return

        engine = embeddedServer(CIO, port = port, host = "0.0.0.0") {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            routing {
                // Public Health check
                get("/api/v1/health") {
                    call.respond(HealthResponse("ok", "OpenAlarm", "1.0.0"))
                }

                // Authenticated routes
                route("/api/v1") {
                    intercept(ApplicationCallPipeline.Plugins) {
                        if (!apiKey.isNullOrBlank()) {
                            val authHeader = call.request.headers["Authorization"]
                            val token = authHeader?.removePrefix("Bearer ")?.trim()
                            if (token != apiKey) {
                                call.respond(
                                    HttpStatusCode.Unauthorized,
                                    ApiResponse<Unit>(success = false, error = "Invalid or missing API key")
                                )
                                finish()
                            }
                        }
                    }

                    // Alarms API
                    get("/alarms") {
                        val alarms = alarmRepository.getAllAlarms().first()
                        call.respond(ApiResponse(success = true, data = alarms))
                    }

                    get("/alarms/{id}") {
                        val id = call.parameters["id"] ?: return@get call.respond(
                            HttpStatusCode.BadRequest,
                            ApiResponse<Unit>(success = false, error = "Missing ID")
                        )
                        val alarm = alarmRepository.getAlarm(AlarmId(id))
                        if (alarm != null) {
                            call.respond(ApiResponse(success = true, data = alarm))
                        } else {
                            call.respond(
                                HttpStatusCode.NotFound,
                                ApiResponse<Unit>(success = false, error = "Alarm not found")
                            )
                        }
                    }

                    post("/alarms") {
                        val alarm = call.receive<Alarm>()
                        val savedId = saveAlarmUseCase(alarm)
                        call.respond(HttpStatusCode.Created, ApiResponse(success = true, data = savedId.value))
                    }

                    delete("/alarms/{id}") {
                        val id = call.parameters["id"] ?: return@delete call.respond(
                            HttpStatusCode.BadRequest,
                            ApiResponse<Unit>(success = false, error = "Missing ID")
                        )
                        deleteAlarmUseCase(AlarmId(id))
                        call.respond(ApiResponse(success = true, data = "Deleted $id"))
                    }

                    post("/alarms/{id}/snooze") {
                        val id = call.parameters["id"] ?: return@post call.respond(
                            HttpStatusCode.BadRequest,
                            ApiResponse<Unit>(success = false, error = "Missing ID")
                        )
                        val result = snoozeAlarmUseCase(AlarmId(id))
                        call.respond(ApiResponse(success = true, data = result.toString()))
                    }

                    post("/alarms/{id}/dismiss") {
                        val id = call.parameters["id"] ?: return@post call.respond(
                            HttpStatusCode.BadRequest,
                            ApiResponse<Unit>(success = false, error = "Missing ID")
                        )
                        dismissAlarmUseCase(AlarmId(id))
                        call.respond(ApiResponse(success = true, data = "Dismissed $id"))
                    }

                    // Sleep API
                    get("/sleep/history") {
                        val sessions = sleepRepository.getAllSessions().first()
                        call.respond(ApiResponse(success = true, data = sessions))
                    }
                }
            }
        }.start(wait = false)
    }

    fun stop() {
        engine?.stop(1000, 2000)
        engine = null
    }
}
