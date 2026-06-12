# Android / KMP Architecture Skills for Claude Code

A collection of six opinionated architecture skills that teach Claude Code how to write Android and Kotlin Multiplatform code the way you'd write it yourself. Once installed, Claude will automatically follow these patterns whenever you ask it to scaffold features, write tests, set up navigation, and more.

## Prerequisites: Installing Claude Code

If you don't have Claude Code yet, install it first.

### macOS / Linux

Open a terminal and run:

```bash
curl -fsSL https://claude.ai/install.sh | bash
```

Then run `claude` to launch Claude Code and complete the authentication flow.

### Windows

Open **PowerShell** and run:

```powershell
irm https://claude.ai/install.ps1 | iex
```

Then run `claude` to launch Claude Code and complete the authentication flow.

> [Git for Windows](https://git-scm.com/downloads/win) is required. See the [official docs](https://docs.claude.com/en/docs/claude-code/quickstart) for detailed setup instructions.

## Installing the Skills

Each skill is a single markdown file (`SKILL.md`) inside its own folder. To install them, copy the six skill folders into your Claude Code skills directory:

```
~/.claude/skills/
├── android-data-layer/
│   └── SKILL.md
├── android-di-koin/
│   └── SKILL.md
├── android-module-structure/
│   └── SKILL.md
├── android-navigation/
│   └── SKILL.md
├── android-presentation-mvi/
│   └── SKILL.md
└── android-testing/
    └── SKILL.md
```

Simply paste the six folders into `~/.claude/skills/` and they'll be picked up automatically the next time you start a Claude Code session. No restart or configuration is needed beyond having the files in place.

## What Each Skill Does

### android-module-structure

Defines the project-level blueprint: feature-layered modularization, Gradle convention plugins, version catalogs, and dependency rules. Claude will follow this structure whenever you ask it to create a new module, set up a project, or decide where code should live.

### android-data-layer

Covers everything below the domain boundary: repository implementations, DTOs, Room entities and DAOs, Ktor `HttpClient` setup, safe-call helpers, token storage, offline-first patterns, and the shared `Result` / `DataError` types.

### android-presentation-mvi

The MVI presentation layer pattern: a single `State` data class, a sealed `Action` interface, one-time `Event` side-effects via `Channel`, ViewModel wiring, the Root/Screen composable split, `UiText` error mapping, and process-death handling with `SavedStateHandle`.

### android-navigation

Type-safe Compose Navigation using `@Serializable` route objects, one nav graph per feature, cross-feature navigation through callbacks, and assembly in the `:app` module.

### android-di-koin

Koin dependency injection conventions: one module per feature layer, `single` / `viewModel` / `factory` scoping, assembling modules in `:app`, and injecting ViewModels in composables with `koinViewModel()`.

### android-testing

Testing patterns and stack: JUnit 5 + AssertK + Turbine for ViewModel unit tests, `UnconfinedTestDispatcher`, fake repositories, `SavedStateHandle` testing, and Compose UI tests with `ComposeTestRule`.

## How Skills Work

When you give Claude Code a task — for example, *"create a new notes feature with a list and detail screen"* — it checks whether any installed skills match the request. If they do, Claude reads the corresponding `SKILL.md` files and follows the patterns described in them. Multiple skills can activate at once, so a feature-scaffolding request might pull in module-structure, data-layer, presentation-mvi, navigation, DI, and testing all together.

You don't need to reference skills by name. Just describe what you want to build and Claude will apply the right patterns automatically.