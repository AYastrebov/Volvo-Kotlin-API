@AGENTS.md

## Claude Code specifics

- Procedures live in `.claude/skills/`: `volvo-kotlin-api` (adding endpoints, config classes, live-API verification, releases) and `volvo-api-integration` (consumer guide). Use them instead of re-deriving the steps.
- The JetBrains MCP server is available when IntelliJ is running. `get_file_problems` and `build_project` give quick diagnostics, but Gradle is the authority: the IDE index lags behind cross-module changes and can report unresolved references that compile fine.
