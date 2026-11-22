# Alarm App — Product Requirements Document

> **Project**: Open-source Android Alarm App for Heavy Sleepers, Productivity Users & Developers  
> **Version**: 1.0  
> **Date**: 2026-11-22  
> **Status**: Draft — Pending Approval

---

## 1. Executive Summary

### 1.1 Product Vision
A **privacy-first, open-source Android alarm clock** that combines aggressive wake-up mechanics for heavy sleepers, sleep science-backed productivity features, and a developer-friendly automation surface — all running **fully offline** with optional self-hosted sync.

### 1.2 Target Audiences (Primary → Secondary)

| Audience | Core Need | Key Differentiator |
|----------|-----------|-------------------|
| **Heavy Sleepers** | Actually wake up | Math/puzzle dismiss, volume ramp, NFC/QR challenges, "no snooze" modes |
| **Productivity Users** | Optimize sleep & routines | Sleep tracking, bedtime reminders, circadian rhythm alignment, statistics |
| **Developers/Power Users** | Automate & integrate | REST API, Tasker/Termux intents, webhooks, MQTT, plugin architecture |

### 1.3 Guiding Principles
- **Local-first**: Zero required cloud, works 100% offline
- **Open source**: MIT/Apache-2.0, community-funded, no telemetry
- **Modular**: Clean Architecture enables feature toggles & plugins
- **Extensible**: Developer APIs are first-class, not afterthoughts
- **Accessible**: Large targets, TalkBack, high contrast, reduced motion

---

## 2. Goals, Non-Goals & Anti-Goals

### 2.1 Goals (Must-Have for v1.0)
- [ ] Reliable alarm scheduling using `AlarmManager` + `WorkManager` fallback
- [ ] Heavy-sleeper dismiss challenges: math, memory, shake, NFC tag, QR code
- [ ] Volume ramp, vibration patterns, custom ringtones (system + local files)
- [ ] Multiple alarms with independent schedules (daily, weekdays, custom days, intervals)
- [ ] Sleep tracking: bedtime reminder, sleep debt, weekly/monthly stats, charts
- [ ] Bedtime routine builder (steps with timers, wind-down sounds)
- [ ] Local backup/export (JSON, CSV) + encrypted import
- [ ] Material 3 theming: light/dark/system, custom accent colors, AMOLED black
- [ ] Android Auto + Wear OS companion apps (alarm control, dismiss from wrist)
- [ ] Developer API: local HTTP server (REST), Tasker intents, webhook dispatcher
- [ ] Room database with full schema migration strategy
- [ ] Comprehensive test coverage (unit, integration, UI)

### 2.2 Non-Goals (Explicitly Out of Scope for v1.0)
- ❌ Cloud sync / account system (optional sync is post-v1)
- ❌ Social features (sharing, leaderboards, challenges)
- ❌ AI/ML sleep analysis (rule-based only)
- ❌ iOS, Web, Desktop ports
- ❌ Paid tiers, ads, analytics, crash reporting (opt-in only, self-hosted)

### 2.3 Anti-Goals (What Would Make This a Failure)
- 💥 Alarm fails to fire (Doze mode, battery optimization edge cases)
- 💥 Data loss on update/uninstall
- 💥 Battery drain > 1%/day idle
- 💥 APK > 15 MB (target < 10 MB)
- 💥 Non-deterministic dismiss challenges

---

## 3. Constraints & Assumptions

| Constraint | Detail |
|------------|--------|
| **Min SDK** | API 24 (Android 7.0) — covers 95%+ active devices |
| **Target SDK** | API 35 (Android 15) |
| **Language** | Kotlin 2.0+ |
| **UI** | Jetpack Compose (Material 3) + Compose Multiplatform-ready structure |
| **DI** | Hilt (or Koin if team prefers lighter weight) |
| **Database** | Room (SQLite) with type converters, full-text search for alarms |
| **Background** | `AlarmManager.setExactAndAllowWhileIdle` + `WorkManager` for retries |
| **Audio** | `MediaPlayer`/`AudioAttributes.USAGE_ALARM` + `RingtoneManager` |
| **Permissions** | `SCHEDULE_EXACT_ALARM`, `USE_EXACT_ALARM`, `POST_NOTIFICATIONS`, `VIBRATE`, `FOREGROUND_SERVICE` (media playback) |
| **Distribution** | GitHub Releases + F-Droid + Play Store (same signed artifact) |
| **License** | Apache-2.0 (code), CC-BY-4.0 (assets) |

---

## 4. Architecture

### 4.1 High-Level Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        app module                                │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐  │
│  │   alarm      │  │   sleep      │  │   developer          │  │
│  │   feature    │  │   feature    │  │   feature            │  │
│  └──────┬───────┘  └──────┬───────┘  └──────────┬───────────┘  │
│         │                 │                      │              │
├─────────┼─────────────────┼──────────────────────┼──────────────┤
│         ▼                 ▼                      ▼              │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                    core:domain                           │   │
│  │  Entities  │  Repository Interfaces  │  Use Cases        │   │
│  └──────────────────────────────────────────────────────────┘   │
│         │                 │                      │              │
├─────────┼─────────────────┼──────────────────────┼──────────────┤
│         ▼                 ▼                      ▼              │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                    core:data                             │   │
│  │  Repository Impl  │  Room DAOs  │  DataSources          │   │
│  │  (Alarm, Sleep,   │  (Schema    │  (Preferences,        │   │
│  │   Settings,       │   Migrations)│   FileSystem,        │   │
│  │   Challenges)     │             │   Network)             │   │
│  └──────────────────────────────────────────────────────────┘   │
│         │                                                          │
├─────────┼──────────────────────────────────────────────────────────┤
│         ▼                                                          │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                    core:platform                         │   │
│  │  AlarmScheduler │ NotificationManager │ AudioManager    │   │
│  │  WearOS/Autocom │  BiometricPrompt    │  IntentHandler  │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

### 4.2 Module Structure

```
alarm-app/
├── app/                          # Application entry point, Hilt setup, navigation graph
├── core/
│   ├── domain/                   # Pure Kotlin: entities, repository interfaces, use cases
│   ├── data/                     # Repository implementations, Room, DataStore, File IO
│   └── platform/                 # Android-specific: AlarmScheduler, Notifications, Wear/Auto
├── feature/
│   ├── alarm/                    # Alarm CRUD, scheduling, dismiss flows, challenges
│   ├── sleep/                    # Sleep tracking, bedtime routine, statistics, charts
│   ├── developer/                # Local HTTP API, Tasker intents, webhook config, plugins
│   ├── settings/                 # Preferences, themes, backup/export, about
│   └── onboarding/               # Permissions, first-run setup, feature tour
├── wear/                         # Wear OS module (Compose for Wear OS)
├── auto/                         # Android Auto module (CarApp library)
├── plugin-api/                   # Published artifact for 3rd-party challenge plugins
└── build-logic/                  # Convention plugins (Kotlin, Compose, Hilt, Detekt, etc.)
```

### 4.3 Key Architectural Decisions

| Decision | Rationale |
|----------|-----------|
| **Clean Architecture + MVVM** | Testability, separation of concerns, team scalability |
| **Feature modules** | Independent compilation, dynamic delivery ready, clear ownership |
| **Hilt DI** | Compile-time validation, Android lifecycle integration, ViewModel injection |
| **Room + Flow** | Reactive UI, offline-first, type-safe migrations |
| **Sealed classes for State/Event** | Exhaustive when handling, predictable UI updates |
| **Plugin API (separate module)** | 3rd-party dismiss challenges without core app updates |
| **Local HTTP server (Ktor/NanoHTTPD)** | Developer API works offline, no cloud dependency |

---

## 5. Feature Specification

### 5.1 Alarm Management (feature:alarm)

#### 5.1.1 Alarm Entity
```kotlin
data class Alarm(
    val id: AlarmId = AlarmId.generate(),
    val label: String = "",
    val time: LocalTime,
    val days: Set<DayOfWeek> = emptySet(),        // empty = one-shot
    val intervalDays: Int = 0,                     // 0 = daily/weekly, >0 = every N days
    val enabled: Boolean = true,
    val ringtone: RingtoneSource = RingtoneSource.SystemDefault,
    val volume: Int = 80,                          // 0-100
    val volumeRamp: VolumeRamp = VolumeRamp.Gradual(30), // seconds to max
    val vibration: VibrationPattern = VibrationPattern.Default,
    val dismissType: DismissType = DismissType.Simple,
    val challengeConfig: ChallengeConfig? = null,
    val snoozeEnabled: Boolean = true,
    val snoozeDuration: Duration = Duration.ofMinutes(5),
    val maxSnoozes: Int = 3,
    val skipNextOccurrence: Boolean = false,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now()
)
```

#### 5.1.2 Dismiss Types & Challenges

| Type | Description | Configuration |
|------|-------------|---------------|
| `Simple` | Tap dismiss button | — |
| `Math` | Solve arithmetic problem | Difficulty: Easy/Medium/Hard, operand range |
| `Memory` | Repeat a sequence (Simon-style) | Length: 4-12, speed |
| `Shake` | Shake phone N times | Count: 10-100, sensitivity |
| `NFC` | Tap registered NFC tag | Tag ID(s) registered per alarm |
| `QR` | Scan registered QR code | Code content registered per alarm |
| `Barcode` | Scan barcode (EAN/ISBN) | Barcode value registered |
| `Captcha` | Image selection / text entry | Plugin-provided |
| `Plugin` | Custom challenge via Plugin API | Plugin ID + config JSON |

> **Plugin API**: `plugin-api` module exposes `DismissChallenge` interface. Apps can install companion APKs that provide custom challenges (e.g., "walk 100 steps", "take a photo of your bathroom mirror").

#### 5.1.3 Scheduling Strategy
- **Primary**: `AlarmManager.setExactAndAllowWhileIdle()` for API 23+
- **Fallback**: `WorkManager` with `setExpedited()` for Doze edge cases
- **Reschedule**: On boot (`BOOT_COMPLETED`), timezone change, locale change
- **Next Occurrence Calculation**: Pure Kotlin (no Android deps) in `domain` — unit testable

#### 5.1.4 Dismiss Flow (State Machine)
```
RINGING → [Challenge Active] → CHALLENGE_PASSED → DISMISSED
                ↓ (fail/skip)
           SNOOZED → [snoozeDuration] → RINGING
                ↓ (maxSnoozes reached)
           DISMISSED
```

### 5.2 Sleep Tracking & Productivity (feature:sleep)

#### 5.2.1 Sleep Session
```kotlin
data class SleepSession(
    val id: SessionId,
    val bedtimeReminderAt: Instant?,      // When notification fired
    val intendedSleepAt: Instant,         // User's target
    val actualSleepAt: Instant?,          // Confirmed (manual/auto)
    val wakeUpAt: Instant?,               // Alarm dismissed
    val wakeUpAlarmId: AlarmId?,          // Linked alarm
    val quality: SleepQuality?,           // User-rated 1-5
    val notes: String = "",
    val stages: List<SleepStage> = emptyList() // Future: if sensor access granted
)
```

#### 5.2.2 Bedtime Routine
- **Steps**: Ordered list with duration (e.g., "Stretch 5min", "Read 15min", "Lights out")
- **Wind-down sounds**: White noise, nature, custom audio (fade out timer)
- **Auto-start**: Triggered by bedtime reminder dismissal or manual tap
- **Completion**: Logs routine adherence % in stats

#### 5.2.3 Statistics & Charts
- **Weekly/Monthly**: Sleep duration, consistency, debt, wake-up mood
- **Charts**: Bar (duration), Line (consistency), Heatmap (bed/wake times)
- **Export**: CSV/JSON for external analysis

### 5.3 Developer API (feature:developer)

#### 5.3.1 Local HTTP Server
- **Port**: Configurable (default 18427), binds to `localhost` only
- **Auth**: Optional token (generated, shown in UI), disabled by default
- **Endpoints**:
  ```
  GET    /api/v1/alarms              # List all
  POST   /api/v1/alarms              # Create
  GET    /api/v1/alarms/{id}         # Get one
  PATCH  /api/v1/alarms/{id}         # Update
  DELETE /api/v1/alarms/{id}         # Delete
  POST   /api/v1/alarms/{id}/enable  # Toggle
  POST   /api/v1/alarms/{id}/trigger # Fire immediately (test)
  GET    /api/v1/sleep/sessions      # Query with filters
  GET    /api/v1/sleep/stats         # Aggregated stats
  GET    /api/v1/settings            # Current config
  PATCH  /api/v1/settings            # Update config
  GET    /api/v1/health              # Liveness probe
  WS     /api/v1/events              # Server-sent events (alarm fired, dismissed, etc.)
  ```

#### 5.3.2 Tasker / Automate / MacroDroid Intents
```kotlin
// Broadcast sent on alarm events
action: "com.alarmapp.ACTION_ALARM_EVENT"
extras:
  - "alarm_id" (String)
  - "event" (String: "RINGING" | "DISMISSED" | "SNOOZED" | "SKIPPED")
  - "timestamp" (Long, epoch ms)
  - "label" (String)

// Intent to trigger alarm immediately
action: "com.alarmapp.ACTION_TRIGGER_ALARM"
extras:
  - "alarm_id" (String, required)

// Intent to set next alarm
action: "com.alarmapp.ACTION_SET_ALARM"
extras:
  - "time" (Long, epoch ms, required)
  - "label" (String)
  - "days" (Array<String> DayOfWeek names)
```

#### 5.3.3 Webhook Dispatcher
- **Config**: Multiple endpoints, per-event filters, retry policy (exponential backoff)
- **Payload**: JSON with event type, alarm/session data, timestamp
- **Security**: HMAC-SHA256 signature header (`X-AlarmApp-Signature`)

#### 5.3.4 MQTT Integration (Optional Module)
- **Broker**: User-configured (local LAN or remote)
- **Topics**: `alarmapp/alarms/{id}/state`, `alarmapp/sleep/sessions`, `alarmapp/commands`
- **Auto-discovery**: Home Assistant MQTT discovery payload

### 5.4 Settings & Customization (feature:settings)

| Category | Options |
|----------|---------|
| **Appearance** | Theme (Light/Dark/System/AMOLED), Accent color, Font scale, Compact mode |
| **Alarms** | Default volume, default ringtone, default snooze, vibration default, dismiss default |
| **Sleep** | Bedtime reminder offset, sleep goal (hrs), auto-track toggle, quality prompt |
| **Developer** | HTTP server port/token, webhook endpoints, MQTT config, Tasker intent toggle |
| **Backup** | Auto-backup schedule (daily/weekly), encrypt (AES-256, user passphrase), export path |
| **Advanced** | Battery optimization whitelist guide, alarm timeout (max ring duration), log level |

### 5.5 Wear OS Companion (wear module)
- **Tile**: Next alarm, quick toggle enable/disable
- **App**: Full alarm list, dismiss/snooze from wrist, volume control
- **Complication**: Next alarm time
- **Sync**: DataStore sync via `WearableListenerService` (no phone app required for dismiss)

### 5.6 Android Auto Companion (auto module)
- **Screen**: Next alarm, enable/disable, snooze/dismiss (voice + touch)
- **Voice**: "Hey Google, disable my 7 AM alarm"

---

## 6. Data Model (Room Schema)

### 6.1 Tables

```sql
-- alarms
CREATE TABLE alarms (
    id TEXT PRIMARY KEY,
    label TEXT NOT NULL DEFAULT '',
    time_minutes INTEGER NOT NULL,           -- minutes from midnight (0-1439)
    days_bitmask INTEGER NOT NULL DEFAULT 0, -- bit 0=Mon ... 6=Sun
    interval_days INTEGER NOT NULL DEFAULT 0,
    enabled INTEGER NOT NULL DEFAULT 1,
    ringtone_type INTEGER NOT NULL DEFAULT 0, -- 0=system, 1=custom, 2=uri
    ringtone_value TEXT NOT NULL DEFAULT '',
    volume INTEGER NOT NULL DEFAULT 80,
    volume_ramp_type INTEGER NOT NULL DEFAULT 1, -- 0=none, 1=gradual, 2=stepped
    volume_ramp_seconds INTEGER NOT NULL DEFAULT 30,
    vibration_pattern TEXT NOT NULL DEFAULT 'default',
    dismiss_type INTEGER NOT NULL DEFAULT 0,
    challenge_config TEXT,                   -- JSON
    snooze_enabled INTEGER NOT NULL DEFAULT 1,
    snooze_minutes INTEGER NOT NULL DEFAULT 5,
    max_snoozes INTEGER NOT NULL DEFAULT 3,
    skip_next INTEGER NOT NULL DEFAULT 0,
    created_at INTEGER NOT NULL,             -- epoch ms
    updated_at INTEGER NOT NULL
);

-- sleep_sessions
CREATE TABLE sleep_sessions (
    id TEXT PRIMARY KEY,
    bedtime_reminder_at INTEGER,
    intended_sleep_at INTEGER NOT NULL,
    actual_sleep_at INTEGER,
    wake_up_at INTEGER,
    wake_up_alarm_id TEXT REFERENCES alarms(id),
    quality INTEGER,                         -- 1-5
    notes TEXT NOT NULL DEFAULT '',
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL
);

-- sleep_stages (future sensor integration)
CREATE TABLE sleep_stages (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    session_id TEXT NOT NULL REFERENCES sleep_sessions(id),
    stage_type INTEGER NOT NULL,             -- 0=awake, 1=light, 2=deep, 3=rem
    start_at INTEGER NOT NULL,
    end_at INTEGER NOT NULL
);

-- routines
CREATE TABLE routines (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    steps TEXT NOT NULL,                     -- JSON array of RoutineStep
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL
);

-- settings (key-value, encrypted values flagged)
CREATE TABLE settings (
    key TEXT PRIMARY KEY,
    value TEXT NOT NULL,
    encrypted INTEGER NOT NULL DEFAULT 0,
    updated_at INTEGER NOT NULL
);

-- webhooks
CREATE TABLE webhooks (
    id TEXT PRIMARY KEY,
    url TEXT NOT NULL,
    events TEXT NOT NULL,                    -- JSON array of event names
    secret TEXT,                             -- HMAC secret
    retry_policy TEXT NOT NULL,              -- JSON
    enabled INTEGER NOT NULL DEFAULT 1,
    created_at INTEGER NOT NULL
);

-- plugin_challenges (installed 3rd-party challenges)
CREATE TABLE plugin_challenges (
    plugin_package_name TEXT PRIMARY KEY,
    challenge_id TEXT NOT NULL,
    display_name TEXT NOT NULL,
    description TEXT,
    config_schema TEXT,                      -- JSON Schema
    version_code INTEGER NOT NULL,
    enabled INTEGER NOT NULL DEFAULT 1
);
```

### 6.2 Migration Strategy
- **Version 1**: Initial schema
- **Version N**: Auto-migration via `RoomDatabase.Migration` + `Pre-packaged database` for major restructures
- **Export/Import**: Versioned JSON schema with `schemaVersion` field for cross-device restore

---

## 7. User Flows

### 7.1 First Launch (Onboarding)
1. Welcome screen → "Get Started"
2. Permission requests (batched, explained):
   - Exact alarms (`SCHEDULE_EXACT_ALARM`)
   - Notifications (`POST_NOTIFICATIONS`)
   - Battery optimization exemption (guide to settings)
   - NFC (optional, for NFC dismiss)
   - Camera (optional, for QR/barcode dismiss)
3. Quick setup: Default wake time, heavy-sleeper mode toggle
4. Feature tour (3 screens): Alarms, Sleep, Developer
5. Land on Alarm list (empty state → "Create your first alarm")

### 7.2 Create/Edit Alarm
```
Alarm List → FAB → Alarm Editor (Bottom Sheet / Full Screen)
  ├─ Time Picker (Material 3)
  ├─ Repeat: Daily / Weekdays / Weekends / Custom Days / Interval / One-shot
  ├─ Label (optional)
  ├─ Ringtone: System / Custom file / Silent (vibration only)
  ├─ Volume + Ramp (slider + preview button)
  ├─ Vibration: Preset / Custom pattern editor
  ├─ Dismiss Type → [Challenge Config if applicable]
  ├─ Snooze: Duration, Max Count, Enable/Disable
  └─ Save → Schedules immediately, returns to list
```

### 7.3 Alarm Ringing (Full-Screen Activity)
- **Lifecycle**: `AlarmActivity` launched via `setExactAndAllowWhileIdle` → `Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_NO_USER_ACTION`
- **UI**: Full-screen Compose, `WindowManager.LayoutParams.FLAG_FULLSCREEN | FLAG_DISMISS_KEYGUARD | FLAG_SHOW_WHEN_LOCKED | FLAG_TURN_SCREEN_ON`
- **Audio**: `AudioAttributes.USAGE_ALARM`, `STREAM_ALARM`, focus request
- **Challenge**: Compose screen per type, timer visible, "Skip" → snooze (if enabled)
- **Dismiss**: On success → `AlarmManager.cancel`, notification removed, `SleepSession` updated if linked

### 7.4 Sleep Tracking
```
Sleep Tab → Bedtime Card (shows reminder time, "Start Routine")
  → Routine Player (stepper with timer, audio controls)
  → "Mark Asleep" → logs actualSleepAt
  → Morning: Alarm dismisses → prompts quality (1-5 stars) + notes
  → Session saved, stats updated
```

### 7.5 Developer API Setup
```
Settings → Developer → Local API
  → Toggle "Enable HTTP Server"
  → Port / Token (auto-generated, copy button)
  → "Test Endpoint" button (opens browser to /health)
  → Webhook: Add URL, select events, test payload
  → MQTT: Broker, topic prefix, username/password
  → Tasker: "Copy Intent Examples" button
```

---

## 8. Technical Requirements

### 8.1 Performance Budgets
| Metric | Target |
|--------|--------|
| Cold start (no alarms) | < 800 ms |
| Cold start (50 alarms) | < 1200 ms |
| Alarm ring latency (scheduled → UI) | < 500 ms |
| DB query (alarm list) | < 16 ms (60 fps) |
| APK size (universal) | < 10 MB |
| Idle battery drain | < 0.5% / 24h |
| Memory (idle) | < 50 MB |

### 8.2 Reliability Requirements
- **Alarm fire rate**: 99.99% (1 missed alarm per 10,000 scheduled)
- **Doze/Standby survival**: Tested on Pixel, Samsung, Xiaomi, OnePlus
- **Boot recovery**: All alarms rescheduled within 10s of `BOOT_COMPLETED`
- **Data integrity**: Zero data loss on crash, OOM, power loss (WAL mode + fsync)

### 8.3 Accessibility
- TalkBack: All actions labeled, dismiss challenges announced
- Switch Access: All targets ≥ 48×48 dp, focus traversal logical
- High Contrast: Force-enabled theme variant
- Reduced Motion: Disable volume ramp animation, challenge animations
- Font Scale: Layout adapts to 200% system font

### 8.4 Security & Privacy
- **No network permission** in base app (developer module declares `INTERNET`)
- **Encrypted backup**: AES-256-GCM, PBKDF2 (100k iterations) from user passphrase
- **HTTP server**: Binds to `127.0.0.1` only, optional token auth
- **Webhooks**: HMAC verification, no automatic redirects
- **NFC/QR**: No data leaves device, tags/codes stored hashed (SHA-256)
- **Crash logs**: Opt-in only, written to local file, user chooses to share

---

## 9. Testing Strategy

### 9.1 Test Pyramid
```
           ┌─────────────┐
           │  E2E (10%)  │  → Compose UI tests, Hilt integration, real AlarmManager
           ├─────────────┤
           │Integration  │  → Repository tests (Room), UseCase tests, Scheduler tests
           │   (30%)     │
           ├─────────────┤
           │   Unit (60%)│  → Domain logic, challenge generators, date math, parsers
           └─────────────┘
```

### 9.2 Key Test Scenarios
| Area | Critical Tests |
|------|----------------|
| **Alarm Scheduling** | DST transitions, timezone change, Doze mode, boot, locale change |
| **Dismiss Challenges** | Deterministic math generation, NFC tag matching, QR parsing, plugin contract |
| **Sleep Calculations** | Debt across DST, leap year, timezone travel, missing sessions |
| **Backup/Restore** | Round-trip fidelity, encryption/decryption, version migration |
| **Developer API** | OpenAPI spec conformance, WebSocket events, HMAC verification |
| **Wear/Auto** | Dismiss sync, complication update, voice intent handling |

### 9.3 CI Pipeline (GitHub Actions)
```yaml
# On every PR
- ktlint + detekt (static analysis)
- Unit tests (JVM + Robolectric)
- Integration tests (Room on Android emulator)
- Compose UI tests (screenshot + golden)
- APK build + size check
- Dependency vulnerability scan (OWASP)

# On release tag
- Signed universal APK + AAB
- Publish to GitHub Releases
- Submit to F-Droid (automated)
- Publish to Play Store (internal track)
- Generate changelog from conventional commits
```

---

## 10. Distribution & Release

### 10.1 Channels
| Channel | Artifact | Signing | Updates |
|---------|----------|---------|---------|
| **GitHub Releases** | Universal APK + AAB | Release keystore | Manual tag push |
| **F-Droid** | Reproducible build | F-Droid key | Automatic (fdroiddata MR) |
| **Play Store** | AAB | Play App Signing | Internal → Closed → Production |

### 10.2 Versioning
- **Scheme**: SemVer (`MAJOR.MINOR.PATCH`)
- **Major**: Breaking API, schema migration, min SDK bump
- **Minor**: New features, new API endpoints, new challenge types
- **Patch**: Bug fixes, translations, dependency updates

### 10.3 Release Cadence
- **Patch**: As needed (hotfix within 48h for alarm failures)
- **Minor**: ~6-8 weeks
- **Major**: ~12-18 months

---

## 11. Open Questions & Decisions Needed

| # | Question | Impact | Recommendation |
|---|----------|--------|----------------|
| 1 | **Hilt vs Koin** | DI boilerplate, build speed | Hilt (compile-time safety, ViewModel injection) |
| 2 | **Ktor vs NanoHTTPD** for local server | Binary size, coroutines support | Ktor (smaller with `ktor-server-netty` ~200KB, full coroutine) |
| 3 | **Charts library** | Binary size, Compose native | `compose-charts` (abandoned?) or `Vico` (active, Compose) or custom Canvas |
| 4 | **NFC tag writing** | UX for registering tags | In-app "Write Tag" flow + "Read Tag" for registration |
| 5 | **QR/Barcode scanner** | Dependency (ML Kit vs ZXing) | ML Kit Barcode Scanning (dynamic module, ~2MB) |
| 6 | **White noise / sleep sounds** | Bundled assets size | 3-4 short loopable assets (<500KB total), user can add own |
| 7 | **Plugin discovery** | How users find 3rd-party challenges | In-app "Challenge Store" (indexed from GitHub topics) or F-Droid style repo |
| 8 | **Widget** | Glance (Compose) vs RemoteViews | Glance (modern, Compose-based, supports Material 3) |

---

## 12. Appendix: Competitive Landscape

| App | Strengths | Gaps (Our Opportunity) |
|-----|-----------|------------------------|
| **Alarmy** | Aggressive challenges | Closed source, ads, no dev API, no sleep tracking |
| **Sleep as Android** | Sleep tracking, Wear, automation | Paid, complex UI, no local HTTP API |
| **Alarmy (open forks)** | Community features | Abandoned, no modern architecture |
| **Kello / Rise** | Design, sleep science | iOS only, subscription, no automation |
| **Custom Tasker profiles** | Full control | High effort, no UI, fragile |

---

## 13. Approval & Next Steps

> **This PRD is a living document.** Upon approval, the next step is invoking `writing-plans` to produce an implementation plan with milestones, task breakdown, and ownership.

### Approval Checklist
- [ ] Product vision & audiences confirmed
- [ ] Feature scope (v1.0) locked
- [ ] Architecture & tech stack approved
- [ ] Non-goals / anti-goals acknowledged
- [ ] Open questions resolved or deferred with owners

---

*Generated with ❤️ for the open-source community. Questions? Open an issue on GitHub.*