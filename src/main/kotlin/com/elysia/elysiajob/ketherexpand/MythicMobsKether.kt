package com.elysia.elysiajob.ketherexpand

import ink.ptms.um.Mythic
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import taboolib.common.platform.function.info
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.ScriptAction
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.player
import java.util.concurrent.CompletableFuture

class MythicMobsKether(val key: ParsedAction<*>, val value: ParsedAction<*>? = null): ScriptAction<Any?>() {
    override fun run(frame: ScriptFrame): CompletableFuture<Any?> {
        val type = frame.newFrame(key).run<String>().get()
        val name = value?.let { frame.newFrame(it).run<String>().get() } ?: return CompletableFuture.completedFuture(null)
        val player = Bukkit.getEntity(frame.player().uniqueId) ?: return CompletableFuture.completedFuture(null)
        when (type) {
            "cast" -> Mythic.API.castSkill(player, name)
        }
        return CompletableFuture.completedFuture(null)
    }
}