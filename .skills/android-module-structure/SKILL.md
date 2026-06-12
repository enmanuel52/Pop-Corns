---
name: popcorns-module
description: >-
  Scaffolds a new Gradle module in the Pop-Corns / CornsTime / "Ktor Movies"
  Android app following its clean-architecture, multi-module conventions (Koin,
  Navigation 3, Ktor, Compose). Use this whenever the user asks to add, create,
  or scaffold a new feature or core module in this project — e.g. "add a feature
  for reviews", "create a new feature module", "scaffold core:analytics", "I need
  a new screen module for X", "wire up a new feature" — even if they don't say the
  word "module". The skill works by copying the patterns from the existing modules
  in the repo rather than inventing structure, so the new module matches the rest
  of the codebase and the dependency rules stay intact.
---

# Pop-Corns module scaffolding

This project is a multi-module Android app built on clean architecture. New
modules must match the existing ones exactly — same plugins, same package
layout, same naming, same wiring. The fastest reliable way to do that is to
**copy a real module that already works and adapt it**, not to write structure
from memory. Always open the reference module and mirror it.

## The cardinal rule: dependencies point inward

Layers, innermost first. An outer layer may depend on inner ones; never the
reverse, and never sideways across siblings except through `core:common`.

1. `core:model` — pure data types. Depends on nothing.
2. `core:domain` — use cases. `api(project(":core:model"))`, Koin, coroutines.
3. `core:ui` + `core:common:*` (same level). **`core:ui` depends on `core:model`, NOT `core:domain`** — keep domain types out of UI.
   On-demand siblings here: `core:database`, `core:datastore`, `core:network`, `core:testing`.
4. `feature:*` and `:app` — the outermost layer.

Rules that are easy to get wrong, so check them:
- Every **feature** depends on `core:ui` (and usually `core:domain`, `core:common:util`).
- `core:common:*` depends on nothing app-specific; anything may depend on it.
- If you're about to make an inner module depend on an outer one, stop — the design is wrong, not the rule.

## Which convention plugin?

`build-logic` defines one plugin per kind of module. Pick by what the module *is*:

| Module kind | Plugins (in `build.gradle.kts`) | Examples |
|---|---|---|
| Pure Kotlin/JVM (no Android, no Compose) | `corntime.jvm.library` | `core:model`, `core:domain`, `core:common:*` |
| Android library, no UI | `corntime.android.library` | `core:network`, `core:database`, `core:datastore` |
| Android library **with Compose UI** | `corntime.android.library` + `corntime.android.compose` | `core:ui`, **every `feature:*`** |

Reference by catalog alias, e.g. `alias(libs.plugins.corntime.android.library)`.
Never add raw `com.android.library` / kotlin-android config in a module — the
convention plugin already applies it.

## Scaffolding a new FEATURE module

This is the common case. **Template to copy: `feature/settings`** — it's the
smallest complete feature (one screen, VM, navigation, DI). For a feature with
paging or multiple screens, also look at `feature/movies`. Read the template
files before writing anything.

Package root is always `com.enmanuelbergling.feature.<name>`. Replace `<name>`
and the PascalCase `<Name>` consistently below.

### 1. Register the module
`settings.gradle.kts` → add `include(":feature:<name>")` next to the other features.

### 2. `feature/<name>/build.gradle.kts`
Copy `feature/settings/build.gradle.kts`. Change `namespace` to
`com.enmanuelbergling.feature.<name>`. Keep only the dependencies the feature
actually needs — most need `core:ui`, `core:domain`, `core:common:util`; add
`core:network` / paging libs only if used.

### 3. `feature/<name>/src/main/AndroidManifest.xml`
An empty `<manifest>` element (see any feature). Features declare no components.

### 4. Source files under `…/feature/<name>/`
Mirror this layout (folder per screen, plus `navigation/` and `di/`):

- **`home/<Name>Screen.kt`** — the Route/Screen split:
  - public `@Composable fun <Name>Route(...callbacks)` → gets the VM via
    `koinViewModel<...>()`, collects state with `collectAsStateWithLifecycle`,
    and calls the private stateless `<Name>Screen`.
  - private `@Composable fun <Name>Screen(uiState, onEvent, ...callbacks)` —
    no Koin, no ViewModel; takes state + an `onEvent: (<Name>UiEvent) -> Unit`.
    This keeps screens previewable and testable. Copy `SettingsScreen.kt`.
- **`home/<Name>VM.kt`** — `internal class <Name>VM(...useCases) : ViewModel()`.
  Inject use cases (suffix `UC`), expose `StateFlow`/flows, handle a sealed
  `<Name>UiEvent` in `onEvent`. Copy `SettingsVM.kt`.
- **`navigation/<Name>Navigation.kt`** — copy `SettingsNavigation.kt`:
  - `@Serializable data object <Name>GraphDestination` and screen destinations
    (`data class …(val id: Int)` when they carry args).
  - `fun NavHostController.navigateTo<Name>Graph(navOptions: NavOptions? = null)`.
  - `fun NavGraphBuilder.<name>Graph(...callbacks)` using
    `navigation<…GraphDestination>(startDestination = …)` and `topComposable<…>`
    (from `core:ui`) for top-level screens, `composable<…>` for inner ones.
- **`di/<Name>Module.kt`** — `val <name>Module = module { viewModelOf(::<Name>VM); singleOf(::SomeUC) }`.
  Copy `MoviesModule.kt`/`SettingsModule.kt`. Use `viewModelOf`, `singleOf`, and
  `includes(...)` to compose sub-modules. The top-level `<name>Module` is public;
  sub-modules can be `internal`/`private`.
- **`model/`** (optional) — UI models, `…UiState`, sealed `…UiEvent`, mappers.

### 5. Wire the feature into the app
Two edits in `:app`:
- `app/.../di/FeaturesModule.kt` — add `<name>Module` to the `includes(...)` list.
- `app/.../navigation/CtiNavHost.kt` — add `import` and call `<name>Graph(...)`
  inside the `NavHost {}`, passing navigation callbacks (e.g.
  `onBack = navController::navigateUp`, `onMovie = navController::navigateToMoviesDetails`).
- Only if the feature is a top-level drawer/bottom-nav destination, add it to
  `app/.../navigation/TopDestination.kt`.

## Scaffolding a new CORE module

Less common. Match the closest existing core module:
- Pure logic/types (`corntime.jvm.library`): copy `core/domain` (has deps) or
  `core/model` (bare). Namespace not needed for JVM modules.
- Android, no UI (`corntime.android.library`): copy `core/network` or
  `core/datastore`; set `namespace = "com.enmanuelbergling.core.<name>"`.
- Add `include(":core:<name>")` to `settings.gradle.kts`.
- If it exposes Koin bindings, follow the existing `di/` module pattern and wire
  it where the others are loaded (`app/.../di/AppModule.kt`).

## Naming conventions (consistency matters here)
- ViewModels: `<Name>VM`, `internal`.
- Use cases: `<Name>UC`.
- Koin modules: `<name>Module` (lowerCamel, matches feature/module name).
- Nav destinations: `@Serializable`, PascalCase, suffix `Destination`; graph
  suffix `GraphDestination`. Navigate helpers: `navigateTo<Name>…`.
- Route vs Screen: public stateful `<Name>Route`, private stateless `<Name>Screen`.

## Before you finish
- Re-check the dependency direction against the cardinal rule above.
- Confirm the new module is in `settings.gradle.kts`, its Koin module is in
  `FeaturesModule.kt` (features) or `AppModule.kt` (core), and its nav graph is
  called in `CtiNavHost.kt`.
- Build the new module to catch wiring mistakes:
  `./gradlew :feature:<name>:assembleDebug` (or `:core:<name>:assemble`).

For a deeper reference list of which existing file to copy for each piece, see
`references/templates.md`.
