package com.elysia.elysiajob.hook

import com.elysia.elysiajob.ElysiaJob
import eos.moe.dragoncore.api.gui.event.CustomPacketEvent
import eos.moe.dragoncore.network.PacketSender
import taboolib.common.platform.event.SubscribeEvent

object CustomPacketEventListener {
    @SubscribeEvent
    fun onClientLoaded(event : CustomPacketEvent){
        if (event.identifier == "DragonCore" && event.data.size == 1 && "cache_loaded" == event.data[0]) {
            ElysiaJob.animationManager.animationFiles.forEach{
                PacketSender.sendServerFile(event.player, "PlayerAnimation/${it.key}", it.value)
            }
        }
    }
}