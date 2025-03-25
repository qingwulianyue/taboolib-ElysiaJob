package com.elysia.elysiajob.hook

import com.elysia.elysiajob.ketherexpand.MythicMobsKether
import taboolib.common.platform.function.info
import taboolib.module.kether.KetherParser
import taboolib.module.kether.scriptParser

object KetherRegister {
    @KetherParser(["mm"], shared = true)
    fun parser() = scriptParser {
        MythicMobsKether(it.nextParsedAction(), it.nextParsedAction())
    }
}