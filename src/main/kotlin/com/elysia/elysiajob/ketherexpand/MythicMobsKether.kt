package com.elysia.elysiajob.ketherexpand

import io.lumine.xikage.mythicmobs.MythicMobs
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import taboolib.common.platform.function.info
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.ScriptAction
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.player
import java.util.concurrent.CompletableFuture

// mm kether脚本执行器
class MythicMobsKether(val key: ParsedAction<*>, val value: ParsedAction<*>? = null): ScriptAction<Any?>() {
    override fun run(frame: ScriptFrame): CompletableFuture<Any?> {
        val type = frame.newFrame(key).run<String>().get()
        val name = value?.let { frame.newFrame(it).run<String>().get() } ?: return CompletableFuture.completedFuture(null)
        val player = Bukkit.getEntity(frame.player().uniqueId) ?: return CompletableFuture.completedFuture(null)
        when (type) {
            "cast" -> MythicMobs.inst().apiHelper.castSkill(player, name)
        }
        return CompletableFuture.completedFuture(null)
    }
}