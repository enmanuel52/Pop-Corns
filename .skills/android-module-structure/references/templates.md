# Reference: which file to copy for each piece

Open these in the repo and adapt. Paths are relative to the project root.

## Feature module (copy `feature/settings`, it's the smallest complete one)

| Piece | Copy from | Notes |
|---|---|---|
| `build.gradle.kts` | `feature/settings/build.gradle.kts` | change `namespace`; trim unused deps |
| `AndroidManifest.xml` | `feature/settings/src/main/AndroidManifest.xml` | empty `<manifest>` |
| Route + Screen split | `feature/settings/src/main/java/com/enmanuelbergling/feature/settings/home/SettingsScreen.kt` (lines ~101-145 show `SettingsRoute` → private `SettingsScreen`) | public Route uses `koinViewModel` + `collectAsStateWithLifecycle`; private Screen is stateless |
| ViewModel | `…/settings/home/SettingsVM.kt` | `internal class …VM`, injects `…UC` use cases, `onEvent(sealed event)` |
| Navigation | `…/settings/navigation/SettingsNavigation.kt` | `@Serializable` destinations, `navigateTo…Graph`, `NavGraphBuilder.…graph`, `topComposable` |
| Koin DI | `…/settings/di/SettingsModule.kt` (simple) or `feature/movies/di/MoviesModule.kt` (composed with `includes(...)`) | `viewModelOf`, `singleOf` |
| UI models / events | `feature/settings/.../model/` (`SettingUiState`, `SettingUiEvent`, mappers) | |
| Paging / multi-screen example | `feature/movies/` (sources, use cases, sections) | only if the feature needs paging |

## Core module

| Module kind | Copy from | Plugin(s) |
|---|---|---|
| Pure types | `core/model/build.gradle.kts` | `corntime.jvm.library` |
| Pure logic + deps | `core/domain/build.gradle.kts` (`api(project(":core:model"))`) | `corntime.jvm.library` |
| Android, no UI | `core/network/build.gradle.kts` or `core/datastore/build.gradle.kts` | `corntime.android.library` |
| Android + Compose UI | `core/ui/build.gradle.kts` | `corntime.android.library` + `corntime.android.compose` |

## App wiring (edit these existing files)

| File | Edit |
|---|---|
| `settings.gradle.kts` | `include(":feature:<name>")` / `include(":core:<name>")` |
| `app/src/main/java/com/enmanuelbergling/ktormovies/di/FeaturesModule.kt` | add `<name>Module` to `includes(...)` (features) |
| `app/src/main/java/com/enmanuelbergling/ktormovies/di/AppModule.kt` | add core Koin module to the loaded modules (core) |
| `app/src/main/java/com/enmanuelbergling/ktormovies/navigation/CtiNavHost.kt` | import + call `<name>Graph(...)` in the `NavHost {}` |
| `app/src/main/java/com/enmanuelbergling/ktormovies/navigation/TopDestination.kt` | only if it's a top-level drawer/nav destination |

## Convention plugin IDs (from `build-logic/convention/build.gradle.kts`)

- `corntime.android.application` → catalog alias `libs.plugins.corntime.android.application`
- `corntime.android.library` → `libs.plugins.corntime.android.library`
- `corntime.android.compose` → `libs.plugins.corntime.android.compose`
- `corntime.jvm.library` → `libs.plugins.corntime.jvm.library`
