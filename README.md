# Boox Shortcuts Fix

Restore hidden system app shortcuts on Boox e-readers running Onyx Launcher.

## The Problem

Onyx Launcher on Boox devices (like P6 Pro 小彩马) uses an `appsFilter` list to hide certain system apps from the home screen. This includes essential apps like:

- 📞 Phone / Dialer
- 💬 SMS / Messages
- 👤 Contacts
- 📱 SIM Toolkit (STK)
- ⚙️ Android Settings

These apps are installed and functional, but you can't access them from the launcher.

## The Solution

This project provides 5 tiny APKs that act as launcher shortcuts. Each APK:

1. Uses a package name **not** in the Onyx filter list
2. Shows up as an icon on the home screen
3. Launches the corresponding hidden system app when tapped

## APK Files

| APK | Size | Function |
|-----|------|----------|
| `boox-phone.apk` | 19 KB | Opens the dialer (org.codeaurora.dialer) |
| `boox-sms.apk` | 16 KB | Opens SMS/Messages app |
| `boox-contacts.apk` | 16 KB | Opens Contacts app |
| `boox-stk.apk` | 16 KB | Opens SIM Toolkit |
| `boox-settings.apk` | 18 KB | Opens Android Settings |

## Installation

### Quick Install (All at once)

```bash
adb install boox-phone.apk
adb install boox-sms.apk
adb install boox-contacts.apk
adb install boox-stk.apk
adb install boox-settings.apk
```

### Or download from Releases

Download the APKs from the [Releases](https://github.com/dynamicfire/boox-shortcuts-fix/releases) page and install via your preferred method.

## Requirements

- Boox device with Onyx Launcher (tested on P6 Pro 小彩马, firmware 4.1)
- ADB access or ability to sideload APKs

## How It Works

Each APK contains a single transparent Activity that:

1. Receives the tap from the launcher
2. Starts the target system app using an Intent
3. Immediately finishes itself (no UI, no trace in recent apps)

The package names (`com.xuanzhou.booxphone`, `com.xuanzhou.booxsms`, etc.) are carefully chosen to avoid Onyx Launcher's filter patterns.

## Building from Source

Each shortcut has its own Gradle project under `boox-*/`:

```bash
cd boox-phone
echo "sdk.dir=$HOME/Library/Android/sdk" > local.properties
./gradlew assembleDebug
# APK at app/build/outputs/apk/debug/app-debug.apk
```

## Related Projects

- [boox-p6pro-root](https://github.com/dynamicfire/boox-p6pro-root) — Root guide for Boox P6 Pro
- [boox-ams-fix](https://github.com/dynamicfire/boox-ams-fix) — Fix Magisk App crash on Boox firmware 4.1
- [boox-telecom-fix](https://github.com/dynamicfire/boox-telecom-fix) — Unlock phone call and SMS functionality

## License

MIT

## Author

玄昼 ([@dynamicfire](https://github.com/dynamicfire))
