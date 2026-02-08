# Boox 快捷方式修复

恢复被 Onyx Launcher 隐藏的系统应用图标。

## 问题背景

文石（Boox）设备（如 P6 Pro 小彩马）的 Onyx Launcher 使用 `appsFilter` 列表过滤掉了部分系统应用，导致以下应用在桌面上不可见：

- 📞 电话 / 拨号器
- 💬 短信
- 👤 联系人
- 📱 SIM 工具包
- ⚙️ 系统设置

这些应用实际上是安装好的，只是被 Launcher 隐藏了。

## 解决方案

本项目提供 5 个极简 APK 作为启动器快捷方式。每个 APK：

1. 使用**不在** Onyx 过滤列表中的包名
2. 在桌面显示为独立图标
3. 点击后启动对应的系统应用

## APK 文件

| APK | 大小 | 功能 |
|-----|------|------|
| `boox-phone.apk` | 19 KB | 打开拨号器 |
| `boox-sms.apk` | 16 KB | 打开短信应用 |
| `boox-contacts.apk` | 16 KB | 打开联系人 |
| `boox-stk.apk` | 16 KB | 打开 SIM 工具包 |
| `boox-settings.apk` | 18 KB | 打开 Android 系统设置 |

## 安装方法

### 方法一：ADB 安装

```bash
adb install boox-phone.apk
adb install boox-sms.apk
adb install boox-contacts.apk
adb install boox-stk.apk
adb install boox-settings.apk
```

### 方法二：下载安装

从 [Releases](https://github.com/dynamicfire/boox-shortcuts-fix/releases) 页面下载 APK，通过文件管理器安装。

## 系统要求

- 文石设备 + Onyx Launcher（在 P6 Pro 小彩马固件 4.1 上测试通过）
- ADB 或其他安装 APK 的方式

## 工作原理

每个 APK 包含一个透明的 Activity：

1. 接收桌面点击
2. 通过 Intent 启动目标系统应用
3. 立即结束自己（无界面，不残留在最近任务中）

包名（如 `com.xuanzhou.booxphone`）经过精心选择，避开 Onyx Launcher 的过滤规则。

## 从源码构建

每个快捷方式都有独立的 Gradle 项目，位于 `boox-*/` 目录下：

```bash
cd boox-phone
echo "sdk.dir=$HOME/Library/Android/sdk" > local.properties
./gradlew assembleDebug
# APK 位于 app/build/outputs/apk/debug/app-debug.apk
```

## 相关项目

- [boox-p6pro-root](https://github.com/dynamicfire/boox-p6pro-root) — 文石 P6 Pro 小彩马 Root 指南
- [boox-ams-fix](https://github.com/dynamicfire/boox-ams-fix) — 修复文石 4.1 固件上 Magisk App 崩溃
- [boox-telecom-fix](https://github.com/dynamicfire/boox-telecom-fix) — 解锁电话和短信功能

## 许可证

MIT

## 作者

玄昼 ([@dynamicfire](https://github.com/dynamicfire))
