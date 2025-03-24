package com.elysia.elysiajob.listener

import com.elysia.elysiajob.ElysiaJob
import com.elysia.elysiajob.hook.PacketSenderController
import eos.moe.dragoncore.api.event.EntityJoinWorldEvent
import org.bukkit.Bukkit
import taboolib.common.platform.event.SubscribeEvent

object BukkitPlayerListener {
    @SubscribeEvent
    fun onEntityJoinWorld(event: EntityJoinWorldEvent) {
        val uuid = event.entityUUID
        val player = Bukkit.getPlayer(uuid) ?: return
        val controllerData = ElysiaJob.controllerManager.getControllerData("太刀") ?: return
        PacketSenderController.setPlayerAnimationController(event.player, uuid, controllerData.fileString)
    }
}