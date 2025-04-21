package com.elysia.elysiajob.hook

import com.elysia.elysiajob.enums.SkillPointKetherType
import com.elysia.elysiajob.ketherexpand.MythicMobsKether
import com.elysia.elysiajob.ketherexpand.SkillPointKether
import taboolib.module.kether.KetherParser
import taboolib.module.kether.scriptParser

// kether脚本注册器
object KetherRegister {
    // 脚本 mm 解析
    @KetherParser(["ejmm"], shared = true)
    fun mm() = scriptParser {
        MythicMobsKether(it.nextParsedAction(), it.nextParsedAction())
    }
    // 脚本 技能点数 解析
    @KetherParser(["ejskillpoint_add"], shared = true)
    fun skillPointAdd() = scriptParser {
        SkillPointKether(it.nextParsedAction(), it.nextParsedAction(), SkillPointKetherType.ADD)
    }
    @KetherParser(["ejskillpoint_set"], shared = true)
    fun skillPointSet() = scriptParser {
        SkillPointKether(it.nextParsedAction(), it.nextParsedAction(), SkillPointKetherType.SET)
    }
    @KetherParser(["ejskillpoint_take"], shared = true)
    fun skillPointTake() = scriptParser {
        SkillPointKether(it.nextParsedAction(), it.nextParsedAction(), SkillPointKetherType.TAKE)
    }
    @KetherParser(["ejskillpoint_get"], shared = true)
    fun skillPointGet() = scriptParser {
        SkillPointKether(it.nextParsedAction(), SkillPointKetherType.GET)
    }
}