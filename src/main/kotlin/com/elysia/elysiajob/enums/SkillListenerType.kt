package com.elysia.elysiajob.enums

enum class SkillListenerType {
    PLAYER_ATTACK,
    PLAYER_DEFENCE;

    companion object {
        // 通过字符串名称获取枚举值（不区分大小写）
        fun fromString(value: String): SkillListenerType {
            return entries.find { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("未知的监听器类型: $value")
        }
    }
}