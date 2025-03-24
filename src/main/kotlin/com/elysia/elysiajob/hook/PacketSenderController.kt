package com.elysia.elysiajob.hook

import eos.moe.dragoncore.network.PacketSender
import eos.moe.dragoncore.network.PluginMessageSender
import org.bukkit.entity.Player
import java.util.*

object PacketSenderController : PacketSender() {
    fun setPlayerAnimationController(player: Player, uuid: UUID, controllerYaml: String){
        PluginMessageSender.sendPluginMessage(player, 29) {buffer ->
            buffer.writeUniqueId(uuid)
            buffer.writeString(controllerYaml)
        }
    }
}