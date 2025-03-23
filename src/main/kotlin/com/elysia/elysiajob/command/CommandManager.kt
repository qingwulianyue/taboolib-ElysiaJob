package com.elysia.elysiajob.command

import com.elysia.elysiajob.ElysiaJob
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*

@CommandHeader("ElysiaJob",["ej"], permission = "server.admin")
object CommandManager {
    @CommandBody
    val reload = subCommand {
        execute<CommandSender> { sender, context, argument ->
            ElysiaJob.config.reload()
            ElysiaJob.skillDataManager.loadFile()
            sender.sendMessage("重载成功")
        }
    }
    @CommandBody
    val mana = subCommand {
        dynamic("type") {
            suggestion<CommandSender>(uncheck = true) { sender, context ->
                val result = mutableListOf("add", "take", "max", "regen", "set")
                return@suggestion result
            }
            dynamic("player") {
                suggestion<CommandSender>(uncheck = true) { sender, context ->
                    return@suggestion Bukkit.getOnlinePlayers().map { it.name }
                }
                int("value") {
                    execute<CommandSender> { sender, context, argument ->
                        val type = context["type"]
                        val player = Bukkit.getPlayer(context["player"])
                        val value = context.double("value")
                        if (player == null){
                            sender.sendMessage("玩家不存在")
                            return@execute
                        }
                        when (type){
                            "add" -> manaAdd(player, value)
                            "take" -> manaTake(player, value)
                            "max" -> manaMax(player, value)
                            "regen" -> manaRegen(player, value)
                            "set" -> manaSet(player, value)
                        }
                    }
                }
            }
        }
    }
    @CommandBody
    val stamina = subCommand {
        dynamic("type") {
            suggestion<CommandSender>(uncheck = true) { sender, context ->
                val result = mutableListOf("add", "take", "max", "regen", "set")
                return@suggestion result
            }
            dynamic("player") {
                suggestion<CommandSender>(uncheck = true) { sender, context ->
                    return@suggestion Bukkit.getOnlinePlayers().map { it.name }
                }
                int("value") {
                    execute<CommandSender> { sender, context, argument ->
                        val type = context["type"]
                        val player = Bukkit.getPlayer(context["player"])
                        val value = context.double("value")
                        if (player == null){
                            sender.sendMessage("玩家不存在")
                            return@execute
                        }
                        when (type){
                            "add" -> staminaAdd(player, value)
                            "take" -> staminaTake(player, value)
                            "max" -> staminaMax(player, value)
                            "regen" -> staminaRegen(player, value)
                            "set" -> staminaSet(player, value)
                        }
                    }
                }
            }
        }
    }
    @CommandBody
    val skill = subCommand {
        dynamic("cast") {
            suggestion<CommandSender>(uncheck = false) { sender, context ->
                return@suggestion listOf("cast")
            }
            dynamic("name"){
                suggestion<CommandSender>(uncheck = false) { sender, context ->
                    return@suggestion Bukkit.getOnlinePlayers().map { it.name }
                }
                dynamic("id"){
                    suggestion<CommandSender>(uncheck = false) { sender, context ->
                        return@suggestion ElysiaJob.skillDataManager.getSkillIdList()
                    }
                    execute<CommandSender> { sender, context, argument ->
                        val id = context["id"]
                        val name = context["name"]
                        val player = Bukkit.getPlayer(name)
                        if (player == null) {
                            sender.sendMessage("玩家不存在")
                            return@execute
                        }
                        val skillData = ElysiaJob.skillDataManager.getSkillData(id)
                        if (skillData == null) {
                            sender.sendMessage("技能不存在")
                            return@execute
                        }
                        if (ElysiaJob.skillManager.checkSkillCastCondition(player, id))
                            ElysiaJob.skillManager.castSkill(player, id)
                    }
                }
            }
        }
    }
    private fun manaAdd(player: Player, value: Double){
        if (value == -1.0)
            ElysiaJob.playerDataManager.setPlayerMana(player.uniqueId, ElysiaJob.playerDataManager.getPlayerMaxMana(player.uniqueId))
        else
            ElysiaJob.playerDataManager.addPlayerMana(player.uniqueId, value)
    }
    private fun manaTake(player: Player, value: Double){
        if (value == -1.0)
            ElysiaJob.playerDataManager.setPlayerMana(player.uniqueId, 0.0)
        else
            ElysiaJob.playerDataManager.takePlayerMana(player.uniqueId, value)
    }
    private fun manaMax(player: Player, value: Double){
        ElysiaJob.playerDataManager.setPlayerMaxMana(player.uniqueId, value)
    }
    private fun manaRegen(player: Player, value: Double){
        ElysiaJob.playerDataManager.setPlayerManaRegen(player.uniqueId, value)
    }
    private fun manaSet(player: Player, value: Double){
        if (value == -1.0)
            ElysiaJob.playerDataManager.setPlayerMana(player.uniqueId, ElysiaJob.playerDataManager.getPlayerMaxMana(player.uniqueId))
        else
            ElysiaJob.playerDataManager.setPlayerMana(player.uniqueId,
                value.coerceAtMost(ElysiaJob.playerDataManager.getPlayerMaxMana(player.uniqueId))
            )
    }
    private fun staminaAdd(player: Player, value: Double){
        if (value == -1.0)
            ElysiaJob.playerDataManager.setPlayerStamina(player.uniqueId, ElysiaJob.playerDataManager.getPlayerMaxStamina(player.uniqueId))
        else
            ElysiaJob.playerDataManager.addPlayerStamina(player.uniqueId, value)
    }
    private fun staminaTake(player: Player, value: Double){
        if (value == -1.0)
            ElysiaJob.playerDataManager.setPlayerStamina(player.uniqueId, 0.0)
        else
            ElysiaJob.playerDataManager.takePlayerStamina(player.uniqueId, value)
    }
    private fun staminaMax(player: Player, value: Double){
        ElysiaJob.playerDataManager.setPlayerMaxStamina(player.uniqueId, value)
    }
    private fun staminaRegen(player: Player, value: Double){
        ElysiaJob.playerDataManager.setPlayerStaminaRegen(player.uniqueId, value)
    }
    private fun staminaSet(player: Player, value: Double){
        if (value == -1.0)
            ElysiaJob.playerDataManager.setPlayerStamina(player.uniqueId, ElysiaJob.playerDataManager.getPlayerMaxStamina(player.uniqueId))
        else
            ElysiaJob.playerDataManager.setPlayerStamina(player.uniqueId,
                value.coerceAtMost(ElysiaJob.playerDataManager.getPlayerMaxStamina(player.uniqueId))
            )
    }
}