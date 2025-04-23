package com.elysia.elysiajob.filemanager.data

import com.elysia.elysiajob.enums.SkillListenerType

data class SkillListenerData(
    val skillListenerType: SkillListenerType,
    val time: Double,
    val condition: String,
    val action: String
) {
    // 通过字符串类型初始化
    constructor(
        typeString: String,
        time: Double,
        condition: String,
        action: String
    ) : this(
        skillListenerType = SkillListenerType.fromString(typeString),
        time = time,
        condition = condition,
        action = action
    )
}