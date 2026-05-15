# 群峦传说：光阴荏苒

**其他语言: [English](README.md)**

这是 Time In A Bottle 与 TerraFirmaCraft / Firmalife 的联动模组。  
英文名：**TerraFirmaCraft: Time Flies**。

TerraFirmaCraft 与 Firmalife 中有许多长流程并不是单纯依靠每 tick 递增进度，而是记录 TFC 日历时间戳，再用当前日历时间与保存时间做差。因此，Time In A Bottle 原本“额外调用方块实体 tick / 随机 tick”的加速方式，对这些流程往往不会生效。本模组为这些时间戳流程添加兼容，同时保留机器自身的普通 tick 行为。

## 功能特性

- ✅ 让 Time In A Bottle 对 TFC 时间戳型流程生效
- ✅ 支持 TFC 作物、树苗、果树、浆果、堆肥桶、木桶、坑窑、高炉坩埚相关设备、冶铁炉等类似方块
- ✅ 支持 Firmalife 温室花盆、葡萄、晾晒/烟熏方块、堆肥滚筒、灯笼网、蜂箱等时间戳流程
- ✅ 对本来就使用类原版 tick 进度的机器，继续保留其方块实体 ticker 加速
- ✅ 不直接修改 Time In A Bottle、TerraFirmaCraft 或 Firmalife 本体
- ✅ 偏服务端逻辑；不添加新方块、物品或配方

## 前置要求

- Minecraft 1.20.1
- Forge 47.1.3+
- TerraFirmaCraft
- Time In A Bottle 4.0.0+
- Firmalife 可选

## 安装

1. 将本模组放入 `mods` 文件夹。
2. 确保已安装 TerraFirmaCraft 和 Time In A Bottle。
3. 如果需要 Firmalife 联动，请同时安装 Firmalife。
4. 启动游戏。

## 工作原理

Time In A Bottle 会在被右键的方块上生成一个临时加速实体。原版逻辑会重复调用目标方块实体的 ticker，或者对随机 tick 方块触发随机 tick。

这对许多 TFC 系统并不够，因为它们会比较当前 TFC 日历 tick 与保存的时间戳。本模组会在加速目标属于 `tfc` 或 `firmalife` 时接管该加速实体，然后：

- 局部推进目标相关的时间戳状态；
- 目标有方块实体 ticker 时，仍然调用原本 ticker；
- 目标是随机 tick 方块时，仍然进行随机 tick 尝试；
- 对 `ICalendarTickable` 机器做当前 tick 对齐，避免连续额外 tick 产生无效的负数日历差。

设计目标是：让 TFC 的时间戳流程能被加速，同时不粗暴破坏本来就依靠普通 tick 运作的机器。

## 配置

服务端配置文件会生成在：

```text
config/tfc_tiab-server.toml
```

可用选项：

- `averageRandomTickInterval`：加速期间额外随机 tick 尝试的平均间隔。默认值为 `1365`，与 Time In A Bottle 默认随机 tick 估算保持一致。

## 兼容说明

本模组面向 Forge 1.20.1 版本的 TerraFirmaCraft、Firmalife 与 Time In A Bottle。

当前覆盖的流程类型包括：

- 基于 `TickCounterBlockEntity` 的方块，例如 TFC 树苗和部分放置后计时方块
- `CropBlockEntity` 作物生长时间戳
- 浆果灌木更新时间戳
- 部分 TFC 机器/流程时间戳，例如木桶、冶铁炉、坑窑、堆肥桶
- 部分 Firmalife 时间戳，例如大型花盆、葡萄、晾晒/烟熏开始时间、堆肥滚筒、灯笼网、蜂箱

## 构建

使用仓库内置的 Gradle Wrapper：

```bash
./gradlew build
```

构建产物位于：

```text
build/libs/
```

## 许可证

MIT License。
