@AGENTS.md

## Claude Code specifics

- Skills live in `.agents/skills/` (the cross-tool Agent Skills location); `.claude/skills/` holds symlinks to them so Claude Code loads them too. Edit the originals, not the links. Skills: `volvo-kotlin-api` (adding endpoints, config classes, live-API verification, releases) and `volvo-api-integration` (consumer guide). Use them instead of re-deriving the steps.
- The JetBrains MCP server is available when IntelliJ is running. `get_file_problems` and `build_project` give quick diagnostics, but Gradle is the authority: the IDE index lags behind cross-module changes and can report unresolved references that compile fine.
