# Better Debug Keybinds

Client-side Fabric mod for binding Minecraft debug actions to independent keys.

## Options

These actions are available in **Options → Controls → Key Binds** under **Debug Keybinds**:

| Action | Default |
| --- | --- |
| Reload Chunks | Unbound |
| Reload Resource Pack | Unbound |
| Clear Chat | Unbound |
| Reload Sounds | Unbound |
| Toggle Chunk Borders | Unbound |
| Toggle Hitboxes | Unbound |

Bindings can be changed without restarting. Shortcuts are handled globally, including while a game screen is open, and invoke Minecraft client functionality directly. Hold **Reload Chunks** to repeat the reload every client tick; release it to stop. Other actions execute once per press. Assigned keys are consumed so they do not also type into chat or a text field. Input is left untouched while rebinding keys in the Controls screen.

## Build

Run `.\gradlew.bat build` on Windows.

## Formatting

Java formatting is checked by Spotless in GitHub Actions on every push and pull request. Run `.\gradlew.bat spotlessCheck` locally to check formatting, or `.\gradlew.bat spotlessApply` to format the Java sources.

## Releases

GitHub Actions builds Minecraft `26.3`, `26.2`, `26.1`, and `1.21.11` on pushes and pull requests. Releases are tag-driven so you can publish only the versions you choose. From a clean, up-to-date `main` branch, run the matching PowerShell script, for example `.\scripts\release\release-26.3.ps1`. It pushes a version-specific tag; GitHub Actions builds and publishes only that version. Each supported version has a script under `scripts\release`.

Release tags use `betterdebugkeybinds-v<Minecraft version>-<UTC year>.<day>.<month>.<daily counter>`, for example `betterdebugkeybinds-v26.3-2026.30.10.0`. The counter starts at `0` each UTC day and increments for additional releases of that Minecraft version that day.

## Test a Minecraft version

Run the matching script under `scripts\test` to launch Minecraft for manual testing, for example `.\scripts\test\run-1.21.11.ps1`. Gradle and Fabric Loom require Java 25; the 1.21.11 mod itself is compiled for Java 21. The supported version and dependency list is in `scripts\versions.json`. When adding a version, update that file, add release and test scripts, update both workflow matrices, and add or select a matching `src/client/java-<profile>` source profile if Minecraft APIs differ.

## VS Code

Open the repository root as the workspace and install the recommended Java and Gradle extensions. VS Code is configured to import dependencies and source sets from the Gradle wrapper automatically. If Minecraft imports are still unresolved, run **Java: Clean Java Language Server Workspace** from the Command Palette, let VS Code restart, then run **Gradle: Refresh Gradle Project**.

## Test in Minecraft

Run `.\gradlew.bat runClient`, create or load a world, and verify all six actions under **Options → Controls → Key Binds → Debug Keybinds**. Try each action in the world and while an anvil or inventory is open; the screen and container contents should remain unchanged. Hold **Reload Chunks** to verify it repeats, then release it. Change a binding in Controls and verify the new key works without restarting.

## License

Released under CC0-1.0; see [LICENSE](./LICENSE).

## Support me!

https://buymeacoffee.com/lorenzius
