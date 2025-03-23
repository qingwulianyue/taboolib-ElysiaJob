package com.elysia.elysiajob

import com.elysia.elysiajob.enums.MessageType
import org.bukkit.entity.Player

fun message(player: Player, messageType: MessageType, vars: Array<String>){
    when(messageType){
        MessageType.ON_COOLDOWN -> {
            player.sendMessage(
                ElysiaJob.config["message.onCooldown"].toString()
                    .replace("%name%", vars[0])
                    .replace("%cooldown%", vars[1])
            )
        }
        MessageType.ON_MANA_NOT_ENOUGH -> {
            player.sendMessage(
                ElysiaJob.config["message.onManaNotEnough"].toString()
                    .replace("%mana%", vars[0])
            )
        }
        MessageType.ON_STAMINA_NOT_ENOUGH -> {
            player.sendMessage(
                ElysiaJob.config["message.onStaminaNotEnough"].toString()
                    .replace("%stamina%", vars[0])
            )
        }
        MessageType.ON_CAST -> {
            player.sendMessage(
                ElysiaJob.config["message.onCast"].toString()
                    .replace("%name%", vars[0])
            )
        }
    }
}