# AlarmApp

A production-ready, open-source Android alarm app with heavy-sleeper challenges, sleep tracking, and developer APIs — all local-first, modular, and extensible.

## Features

- **Heavy-sleeper challenges**: Math, Memory, Shake, NFC, QR, and plugin-based dismiss challenges
- **Sleep tracking**: Bedtime reminders, sleep routines, sleep statistics with charts
- **Developer API**: Local HTTP server (REST + WebSocket), webhooks, MQTT, Home Assistant integration
- **Cross-platform**: Wear OS tiles/complications, Android Auto support
- **Widgets**: Glance-powered app widget with next alarm and quick actions
- **Accessibility**: TalkBack, Switch Access, High Contrast, Reduced Motion, 200% font scale
- **Privacy-first**: No network permission in base app, all data local

## Architecture

```
AlarmApp/
├── app/                    # Application entry point, navigation, theming
├── core/
│   ├── domain/             # Pure Kotlin domain layer (entities, use cases, repositories)
│   ├── data/               # Room database, DataStore, repositories implementations
│   └── platform/           # Android platform implementations (AlarmManager, Notifications, Wear, Auto)
├── feature/
│   ├── alarm/              # Alarm list, editor, ringing flow, challenges
│   ├── sleep/              # Sleep tab, bedtime, routines, statistics
│   ├── settings/           # Settings UI, backup/export, appearance
│   ├── developer/          # Local HTTP server, webhooks, MQTT, Tasker
│   └── onboarding/         # First-run flow, permissions
├── wear/                   # Wear OS module (tiles, complications, standalone app)
├── auto/                   # Android Auto module (CarApp Library)
├── plugin-api/             # Published AAR for 3rd-party challenge plugins
└── build-logic/            # Gradle convention plugins
```

## Tech Stack

- **Language**: Kotlin 2.0+
- **UI**: Jetpack Compose (Material 3), Compose Navigation
- **Architecture**: Clean Architecture + MVVM
- **DI**: Hilt
- **Database**: Room 2.6
- **Networking**: Ktor 3.0 (local HTTP server only)
- **Async**: Coroutines 1.8 + Flow
- **Testing**: JUnit5, MockK, Robolectric, Compose UI Tests
- **Static Analysis**: Detekt, ktlint
- **Widgets**: Glance
- **Wear OS**: Wear Compose, Tiles, Complications
- **Android Auto**: CarApp Library

## Requirements

- Android Studio Koala (2024.1.1) or later
- JDK 17
- Android SDK 35 (compile), min SDK 24
- Gradle 8.7+ (via wrapper)

## Getting Started

### Clone and Build

```bash
git clone https://github.com/alarmapp/alarmapp.git
cd alarmapp
./gradlew assembleDebug
```

### Run Tests

```bash
# Unit tests
./gradlew testDebugUnitTest

# Static analysis
./gradlew ktlintCheck detekt

# Integration tests (requires emulator)
./gradlew connectedDebugAndroidTest
```

### CI Pipeline

The CI pipeline runs on every push/PR:
1. **Lint** → `ktlintCheck` + `detekt`
2. **Unit Tests** → `testDebugUnitTest`
3. **Integration Tests** → Emulator (API 35)
4. **UI Tests** → Compose UI tests on emulator
5. **APK Size Check** → Must be < 10 MB

## Project Structure

### Core Modules

| Module | Description |
|--------|-------------|
| `core:domain` | Pure Kotlin domain — entities, repository interfaces, use cases, utilities |
| `core:data` | Room database, DAOs, DataStore, Repository implementations, Backup manager |
| `core:platform` | Android implementations — AlarmManager, Notifications, Audio, Wear/Auto, Plugins |

### Feature Modules

| Module | Description |
|--------|-------------|
| `feature:alarm` | Alarm CRUD, editor, ringing activity, all 6 built-in challenges |
| `feature:sleep` | Sleep tracking, bedtime reminders, routines, statistics charts |
| `feature:settings` | Full settings UI, backup/export, theme, advanced options |
| `feature:developer` | Local HTTP server, webhooks, MQTT, Home Assistant, Tasker |
| `feature:onboarding` | First-run flow, permission requests, quick setup |

### Platform Modules

| Module | Description |
|--------|-------------|
| `wear` | Wear OS tiles, complications, standalone app (dismiss/snooze from wrist) |
| `auto` | Android Auto screen (next alarm, enable/disable, voice intents) |

## Configuration

### Gradle Properties

Key properties in `gradle.properties`:

```properties
compileSdk=35
minSdk=24
targetSdk=35
jvmTarget=17
composeCompilerVersion=1.5.14
```

### Version Catalog

All dependencies managed in `gradle/libs.versions.toml`. Update versions there.

### Local Properties

Copy `local.properties.template` to `local.properties` and configure:

```properties
sdk.dir=/path/to/Android/Sdk
storeFile=../signing/release.keystore
storePassword=***
keyAlias=release
keyPassword=***
```

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on:
- Code style (ktlint + detekt)
- Commit message format
- Pull request process
- Testing requirements
- Accessibility requirements

## License

Apache-2.0 © AlarmApp Contributors

See [LICENSE](LICENSE) for full text.

## Distribution

- **GitHub Releases**: Signed universal APK + AAB
- **F-Droid**: Reproducible builds via fdroiddata
- **Play Store**: Internal → Closed → Open testing

All distributions use the same signed artifact.

## Acknowledgments

- Built with ❤️ for heavy sleepers everywhere
- Icons from Material Icons Extended
- Charts via Compose Canvas (no heavy deps)