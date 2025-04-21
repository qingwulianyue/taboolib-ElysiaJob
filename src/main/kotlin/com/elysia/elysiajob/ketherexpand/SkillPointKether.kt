package com.elysia.elysiajob.ketherexpand

import com.elysia.elysiajob.ElysiaJob
import com.elysia.elysiajob.enums.SkillPointKetherType
import org.bukkit.Bukkit
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.ScriptAction
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.player
import java.util.concurrent.CompletableFuture

class SkillPointKether(val var1: ParsedAction<*>, val var2: ParsedAction<*>? = null, val type: SkillPointKetherType): ScriptAction<Any?>() {
    constructor(var1: ParsedAction<*>, type: SkillPointKetherType) : this(var1, null ,type) 
    override fun run(frame: ScriptFrame): CompletableFuture<Any?> {
        when (type) {
            SkillPointKetherType.ADD -> {
                val id = var1.let { frame.newFrame(it).run<String>().get() } ?: return CompletableFuture.completedFuture(null)
                val value = var2?.let { frame.newFrame(it).run<String>().get() } ?: return CompletableFuture.completedFuture(null)
                val player = Bukkit.getEntity(frame.player().uniqueId) ?: return CompletableFuture.completedFuture(null)
                ElysiaJob.playerSkillDataManager.addPlayerSkillPoint(player.uniqueId, id, value.toInt())
            }
            SkillPointKetherType.SET -> {
                val id = var1.let { frame.newFrame(it).run<String>().get() } ?: return CompletableFuture.completedFuture(null)
                val value = var2?.let { frame.newFrame(it).run<String>().get() } ?: return CompletableFuture.completedFuture(null)
                val player = Bukkit.getEntity(frame.player().uniqueId) ?: return CompletableFuture.completedFuture(null)
                ElysiaJob.playerSkillDataManager.setPlayerSkillPoint(player.uniqueId, id, value.toInt())
            }
            SkillPointKetherType.TAKE -> {
                val id = var1.let { frame.newFrame(it).run<String>().get() } ?: return CompletableFuture.completedFuture(null)
                val value = var2?.let { frame.newFrame(it).run<String>().get() } ?: return CompletableFuture.completedFuture(null)
                val player = Bukkit.getEntity(frame.player().uniqueId) ?: return CompletableFuture.completedFuture(null)
                ElysiaJob.playerSkillDataManager.takePlayerSkillPoint(player.uniqueId, id, value.toInt())
            }
            SkillPointKetherType.GET -> {
                val id = var1.let { frame.newFrame(it).run<String>().get() } ?: return CompletableFuture.completedFuture(null)
                val player = Bukkit.getEntity(frame.player().uniqueId) ?: return CompletableFuture.completedFuture(null)
                return CompletableFuture.completedFuture(ElysiaJob.playerSkillDataManager.getPlayerSkillPoint(player.uniqueId, id))
            }
        }
        return CompletableFuture.completedFuture(null)
    }
}