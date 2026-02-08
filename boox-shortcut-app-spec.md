# Boox P6 Pro 快捷方式 APP 技术规格

## 背景

Onyx Launcher（包名 `com.onyx`，APK 路径 `/system/priv-app/kcb-release/kcb-release.apk`）通过内嵌的设备配置文件 `res/raw/p6proc.json` 中的 `appsFilter` 数组来过滤应用。在此列表中的应用不会显示在桌面上。

被过滤的关键应用：
- `com.android.mms` — 短信
- `com.android.settings` — 系统设置
- `com.android.contacts` — 联系人
- `com.android.phone` — 电话服务
- `com.android.stk` — SIM 工具包
- `org.codeaurora.dialer` — Qualcomm 拨号器
- `com.google.android.dialer` — **设备上未安装**，且不在过滤列表中

APK 使用 v2 签名，无法修改资源后重新签名。

## 目标

制作一个小型 Android APP，安装后在 Onyx Launcher 桌面上显示 **五个独立图标**：电话、短信、联系人、SIM 工具包、设置。点击后分别启动对应的系统应用。

## 关键约束

### 包名
APP 的包名**不能**出现在 `appsFilter` 列表中。建议使用类似：
```
com.xuanzhou.booxshortcuts
```

### 过滤列表（完整）
以下包名会被 Onyx Launcher 隐藏，**必须避免**使用：
```
android, com.android.*, com.google.android.gms, com.google.android.gsf,
com.google.android.webview, com.google.android.tts, com.google.android.configupdater,
com.google.android.apps.restore, com.google.android.ext.shared,
com.onyx, com.onyx.*, com.qti.*, com.qualcomm.*, org.codeaurora.*,
vendor.qti.*
```

注意：`com.google.android.dialer` **不在**过滤列表中。

### 需要三个独立图标
使用 `<activity-alias>` 实现一个 APK 显示多个桌面图标。

## 技术实现

### AndroidManifest.xml 核心结构

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.xuanzhou.booxshortcuts">

    <application
        android:label="Boox Shortcuts"
        android:icon="@mipmap/ic_launcher"
        android:allowBackup="false">

        <!-- 主 Activity（透明，仅做跳板） -->
        <activity
            android:name=".LaunchActivity"
            android:theme="@android:style/Theme.NoDisplay"
            android:exported="true"
            android:excludeFromRecents="true">
            <!-- 不给主 Activity 加 LAUNCHER intent-filter，避免多余图标 -->
        </activity>

        <!-- 图标1：电话 -->
        <activity-alias
            android:name=".PhoneAlias"
            android:targetActivity=".LaunchActivity"
            android:label="电话"
            android:icon="@mipmap/ic_phone"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <meta-data
                android:name="launch_target"
                android:value="phone" />
        </activity-alias>

        <!-- 图标2：短信 -->
        <activity-alias
            android:name=".SmsAlias"
            android:targetActivity=".LaunchActivity"
            android:label="短信"
            android:icon="@mipmap/ic_sms"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <meta-data
                android:name="launch_target"
                android:value="sms" />
        </activity-alias>

        <!-- 图标3：联系人 -->
        <activity-alias
            android:name=".ContactsAlias"
            android:targetActivity=".LaunchActivity"
            android:label="联系人"
            android:icon="@mipmap/ic_contacts"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <meta-data
                android:name="launch_target"
                android:value="contacts" />
        </activity-alias>

        <!-- 图标4：SIM 工具包 -->
        <activity-alias
            android:name=".StkAlias"
            android:targetActivity=".LaunchActivity"
            android:label="SIM 工具包"
            android:icon="@mipmap/ic_stk"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <meta-data
                android:name="launch_target"
                android:value="stk" />
        </activity-alias>

        <!-- 图标5：设置 -->
        <activity-alias
            android:name=".SettingsAlias"
            android:targetActivity=".LaunchActivity"
            android:label="设置"
            android:icon="@mipmap/ic_settings"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <meta-data
                android:name="launch_target"
                android:value="settings" />
        </activity-alias>

    </application>
</manifest>
```

### LaunchActivity.java 逻辑

```java
package com.xuanzhou.booxshortcuts;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class LaunchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String target = "phone"; // 默认

        // 读取是哪个 alias 启动的
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(
                getComponentName(), PackageManager.GET_META_DATA);
            if (ai.metaData != null) {
                target = ai.metaData.getString("launch_target", "phone");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent;
        switch (target) {
            case "sms":
                // 启动短信应用
                intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName(
                    "com.android.mms",
                    "com.android.mms.ui.ConversationList"));
                break;
            case "contacts":
                // 启动联系人
                intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName(
                    "com.android.contacts",
                    "com.android.contacts.activities.PeopleActivity"));
                break;
            case "stk":
                // 启动 SIM 工具包
                intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName(
                    "com.android.stk",
                    "com.android.stk.StkLauncherActivity"));
                break;
            case "settings":
                // 启动系统设置
                intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                break;
            case "phone":
            default:
                // 启动拨号器
                intent = new Intent(Intent.ACTION_DIAL);
                break;
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(intent);
        } catch (Exception e) {
            // fallback：尝试 PackageManager 的 launchIntent
            e.printStackTrace();
        }

        finish();
    }
}
```

### activity-alias 的 meta-data 读取说明

上面的代码通过 `getComponentName()` 获取当前启动的组件名（即哪个 alias），然后读取对应的 `meta-data` 来决定要启动哪个应用。

**注意**：`getComponentName()` 在通过 alias 启动时，返回的是 alias 的组件名（如 `.PhoneAlias`），不是 targetActivity 的组件名。所以 `getActivityInfo()` 能正确获取到对应 alias 的 meta-data。

### 备选方案：不用 meta-data

如果 meta-data 读取有问题，可以直接判断 `getComponentName()`：

```java
String className = getComponentName().getClassName();
if (className.contains("Phone")) {
    // 启动电话
} else if (className.contains("Sms")) {
    // 启动短信
} else if (className.contains("Contacts")) {
    // 启动联系人
} else if (className.contains("Stk")) {
    // 启动 SIM 工具包
} else if (className.contains("Settings")) {
    // 启动设置
}
```

## 启动目标详情

### 电话（Phone / Dialer）
```
// 方案A：通用拨号 Intent（推荐，系统自动打开唯一拨号器）
Intent intent = new Intent(Intent.ACTION_DIAL);

// 方案B：直接启动 Qualcomm 拨号器（设备上唯一的拨号器）
ComponentName("org.codeaurora.dialer",
    "com.android.dialer.DialtactsActivity")
```
注意：设备上没有 Google Dialer，唯一的拨号器是 `org.codeaurora.dialer`。

### 短信（SMS / MMS）
```
// 方案A：通用短信 Intent
Intent intent = new Intent(Intent.ACTION_MAIN);
intent.addCategory(Intent.CATEGORY_APP_MESSAGING);

// 方案B：直接启动系统短信
ComponentName("com.android.mms",
    "com.android.mms.ui.ConversationList")
```

### 联系人（Contacts）
```
// 方案A：通用联系人 Intent
Intent intent = new Intent(Intent.ACTION_MAIN);
intent.addCategory(Intent.CATEGORY_APP_CONTACTS);

// 方案B：直接启动系统联系人
ComponentName("com.android.contacts",
    "com.android.contacts.activities.PeopleActivity")
```

### SIM 工具包（STK）
```
// 直接启动 STK
ComponentName("com.android.stk",
    "com.android.stk.StkLauncherActivity")
```

### 设置（Settings）
```
// 方案A：通用设置 Intent（推荐）
Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);

// 方案B：直接启动
ComponentName("com.android.settings",
    "com.android.settings.Settings")
```

## 图标

需要五套图标（放在 `mipmap-xxhdpi` 或 `mipmap-xxxhdpi`）：
- `ic_phone.png` — 电话图标
- `ic_sms.png` — 短信图标
- `ic_contacts.png` — 联系人图标
- `ic_stk.png` — SIM 工具包图标
- `ic_settings.png` — 设置图标

建议使用 Material Design 风格图标，推荐尺寸 192×192px。可以从 https://fonts.google.com/icons 获取 SVG 后转 PNG。

## 构建配置

- **minSdkVersion**: 30（Android 11）
- **targetSdkVersion**: 33（Android 13，P6 Pro C 的系统版本）
- **无需任何权限**

## 验证要点

1. 安装后 Onyx Launcher 桌面应出现三个独立图标
2. 点击"电话"应打开拨号界面（org.codeaurora.dialer）
3. 点击"短信"应打开短信应用
4. 点击"设置"应打开 Android 系统设置（非 Onyx 设置）
5. 点击后快捷方式 Activity 应立即 finish()，不残留在最近任务中

## 风险与注意事项

- Onyx Launcher 的 `appsFilter` 是按**包名**过滤的，不是按 activity-alias 过滤。只要 APP 的包名（`com.xuanzhou.booxshortcuts`）不在列表中，所有 alias 图标都会显示。
- 如果 Onyx Launcher 只显示第一个 LAUNCHER activity 而忽略 alias，可能需要拆成三个独立的极简 APK。
- `Theme.NoDisplay` 的 Activity 必须在 `onCreate()` 中调用 `finish()`，否则会 ANR。

## 来电问题说明

设备上唯一的拨号器是 `org.codeaurora.dialer`。Telecom.apk 的 `addNewIncomingCall()` 中有双重检查：当默认拨号器是 `org.codeaurora.dialer` 时会阻断来电。由于设备没有安装其他拨号器（如 Google Dialer），目前无法通过切换默认拨号器来修复来电。

如需修复来电，需从 APKMirror 下载安装 Google Phone（com.google.android.dialer），安装后 boox-telecom-fix v1.3 模块会自动检测并将其设为默认拨号器。
