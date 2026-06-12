---
name: git-commit-workflow
description: >-
  Manage the git branch-and-commit lifecycle for the Pop-Corns / CornsTime /
  "Ktor Movies" Android project. Use this skill whenever you START work on a new
  feature, fix, or refactor (create a branch off `dev`), whenever you FINISH a
  self-contained working chunk of code (commit it), and whenever the feature is
  DONE (merge the branch back into `dev`). Also use it when the user says
  anything like "commit this", "commit the changes", "save this work", "start a
  new feature", "merge this in", or "wrap this up". The skill creates a feature
  branch from `dev`, verifies the app actually builds and its tests pass before
  every commit (./gradlew assembleDebug test), fixes the build if it breaks,
  splits unrelated changes into separate logical commits using the project's
  `type: meaningful description` message format, and at the end merges the
  branch into `dev` — pausing for the user to resolve any merge conflicts.
  Reach for it even when the user doesn't say the exact words, as long as a
  piece of work is being started, completed, or wrapped up.
---

# Git branch & commit workflow

This project keeps a clean, trustworthy history. Each feature lives on its own
branch off `dev`; every commit builds and its tests pass; every message says
plainly what changed; and finished work merges back into `dev`. The payoff:
anyone scanning `git log` understands how the app evolved, `git bisect` always
lands on a working revision, and `dev` stays integrable.

The lifecycle has three stages: **start a feature → commit chunks → finish and
merge**. Follow whichever stage applies to the current moment.

## Stage 1 — Start a feature (branch off `dev`)

When you begin a new piece of work — a feature, a bug fix, a refactor — start it
on its own branch created from `dev`, so `dev` itself stays clean and the work
stays isolated until it's ready.

```bash
git checkout dev
git pull            # only if a remote is configured and reachable; skip if it fails
git checkout -b feat-<short-kebab-description>
```

**Branch naming** follows the existing convention in this repo — a type prefix,
a hyphen, then a short description: `feat-watchlist-sync`, `fix-null-poster`,
`refactor-search-usecase`. Match the prefix to the kind of work (`feat-`,
`fix-`, `refactor-`), mirroring the commit types below.

If you're already on a suitable feature branch, don't create another one — keep
going on the current branch. Only branch when starting genuinely new work.

## Stage 2 — Commit a functional chunk

Commit once a **functional chunk** is complete — a unit of work that stands on
its own and leaves the app in a working state (a bug fixed, a feature working
end-to-end, a refactor finished). Don't wait to be asked; if you've completed
such a chunk, checkpoint it. Also commit whenever the user explicitly asks.

Avoid committing half-finished work you know doesn't build or run — that defeats
the purpose of a trustworthy history. If you must stop mid-feature, say so
rather than committing something broken.

### 2a. See what changed

Run `git status` and `git diff` to understand the full set of changes. This
tells you which files belong together and whether the tree holds more than one
concern.

### 2b. Verify it builds and tests pass

**CRITICAL**: You MUST verify the project builds and tests pass before EVERY commit. Do not skip this step even for small changes.

```bash
./gradlew assembleDebug test
```

This compiles the app and runs the unit tests. A commit that fails this check
poisons the history, so this gate is non-negotiable.

**If it fails:** read the error, fix the underlying cause, and re-run until it
passes. Don't commit around a failure or disable a failing test to make it
green — fix the actual problem. Only once `./gradlew assembleDebug test`
succeeds do you proceed to commit. This is a large multi-module project, so the
full build can take a while; that's expected — let it finish.

### 2c. Split unrelated changes into logical commits

If the working tree mixes more than one concern — say, a bug fix *and* an
unrelated refactor — make a separate commit for each. Stage only the files for
one concern (`git add <specific files>`), commit, then repeat. A reader should
be able to understand each commit in isolation. If everything in the tree is one
coherent chunk, a single commit is correct — don't fragment a cohesive change.

The build/test gate in 2b already verified the **combined** working state, so
you don't need to re-run the build after staging each partial commit — splitting
is just about grouping the already-verified changes into readable commits.

### 2d. Write the commit message

Use the project's format: a **type**, a colon, then a meaningful description.

```
type: meaningful description of what was done
```

| Type       | Use it for                                   |
|------------|----------------------------------------------|
| `fix`      | Fixing a bug or error                        |
| `feat`     | Adding a new feature or functionality        |
| `refactor` | Restructuring code without changing behavior |

The description should say *what changed and why it matters*, not restate the
diff. Write it so someone reading `git log` six months from now understands the
intent. Match the plain tone of recent history (`git log --oneline -10`).

**Examples:**

```
feat: add watchlist sync across devices via datastore
fix: prevent crash when movie details API returns null poster
refactor: extract movie search logic into shared use case
```

Include the standard co-author trailer after the description (the subject line
stays in `type: description` form):

```bash
git commit -m "$(cat <<'EOF'
feat: add watchlist sync across devices via datastore

Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>
EOF
)"
```

**Do not push** — committing is local; leave pushing to the user.

## Stage 3 — Finish the feature (merge into `dev`)

When the feature is done — fully working, all chunks committed, the build and
tests green — merge the branch back into `dev`.

```bash
git checkout dev
git merge feat-<short-kebab-description>
```

**If the merge reports conflicts:** stop. Do not guess at a resolution — show
the user which files conflict (`git status`) and ask them to resolve the
conflicts, since they know which side of each change should win. Wait for their
direction before completing the merge. Once they've resolved everything, finish
the merge as they direct (e.g. `git add` the resolved files and
`git commit`/`git merge --continue`).

**If the merge is clean,** confirm it landed (`git log --oneline -5`) and tell
the user the feature is merged into `dev`. Offer to delete the now-merged
feature branch, but don't delete it unless they agree. As always, **don't
push** — leave that to the user.