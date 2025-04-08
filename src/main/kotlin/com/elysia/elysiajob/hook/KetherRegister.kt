package com.elysia.elysiajob.hook

import com.elysia.elysiajob.ketherexpand.MythicMobsKether
import taboolib.common.platform.function.info
import taboolib.module.kether.KetherParser
import taboolib.module.kether.scriptParser

// kether脚本注册器
object KetherRegister {
    // 脚本 mm 解析
    @KetherParser(["ejmm"], shared = true)
    fun parser() = scriptParser {
        info("ejmm")
        MythicMobsKether(it.nextParsedAction(), it.nextParsedAction())
    }
}