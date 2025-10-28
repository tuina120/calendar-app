# Calendar App (Android)

这是一个为 Android 设备准备的日历应用项目骨架，使用 Kotlin + Jetpack Compose + Room，实现基本的事件 CRUD 和导出为 ICS（RFC 5545）文件的功能。

已实现（MVP + 部分期望功能）:
- 事件实体（Room）
- 本地存储（Room Database, DAO, Repository）
- 简单 Compose UI：列出事件、添加示例事件
- ICS 导出工具（`IcsExporter`），将事件导出到 `.ics` 文件
- FileProvider 配置以便分享导出的文件

后续建议实现（未完成）:
- 完整的日/周/月视图（现在仅为事件列表）
- 添加/编辑/删除事件的详细 UI
- 重复规则 (RRULE) 解析、提醒/通知、时区更好支持
- 与 Android 系统日历双向同步（可选）
- 单元测试与 UI 测试

如何构建与运行

1. 使用 Android Studio 打开此目录（`/home/sunran/calendar-app`）
2. Gradle 同步并运行 `app` 模块

导出日程

在应用中点击导出按钮（在 UI 中有一个 Share FAB，当前逻辑仅生成文件，分享行为需在 Compose 层补全以获得 Activity context 并通过 FileProvider 分享文件）。

说明

- `IcsExporter` 位于 `app/src/main/java/com/example/calendarapp/util/IcsExporter.kt`，用于把 `Event` 列表写入 `.ics` 文件。
- 数据层在 `data/` 中，包含 `Event`, `EventDao`, `CalendarDatabase`, `EventRepository`。

如果你想让我继续：
- 我可以完善导出后立即打开分享面板（实现 `Activity` 级别的文件分享），并补全添加/编辑事件界面；
- 或者我可以实现月视图和 RRULE 支持。请选择下一步要我实现的功能。