package com.elysia.elysiajob.command

import com.elysia.elysiajob.ElysiaJob
import com.elysia.elysiajob.skill.SkillManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*

@CommandHeader("ElysiaJob",["ej"], permission = "server.admin")
object CommandManager {
    // 重载命令
    @CommandBody
    val reload = subCommand {
        execute<CommandSender> { sender, context, argument ->
            ElysiaJob.config.reload()
            ElysiaJob.skillDataManager.loadFile()
            ElysiaJob.jobDataManager.loadFile()
            sender.sendMessage("重载成功")
        }
    }
    // 魔力值相关命令
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
    // 体力值相关命令
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
    // 技能相关命令
    @CommandBody
    val skill = subCommand {
        dynamic("type") {
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
                        val type = context["type"]
                        val id = context["id"]
                        val name = context["name"]
                        val player = Bukkit.getPlayer(name)
                        if (player == null) {
                            sender.sendMessage("玩家不存在")
                            return@execute
                        }
                        when (type){
                            "cast" -> skillCast(player, id)
                        }
                    }
                }
            }
        }
    }
    // 技能点数相关命令
    @CommandBody
    val point = subCommand {
        dynamic("type") {
            suggestion<CommandSender>(uncheck = true) { sender, context ->
                val result = mutableListOf("add", "take", "max", "regen", "set")
                return@suggestion result
            }
            dynamic("player") {
                suggestion<CommandSender>(uncheck = true) { sender, context ->
                    return@suggestion Bukkit.getOnlinePlayers().map { it.name }
                }
                dynamic("id") {
                    suggestion<CommandSender>(uncheck = false) { sender, context ->
                        return@suggestion ElysiaJob.skillDataManager.getSkillIdList()
                    }
                    int("value") {
                        execute<CommandSender> { sender, context, argument ->
                            val type = context["type"]
                            val player = Bukkit.getPlayer(context["player"])
                            val id = context["id"]
                            val value = context.double("value")
                            if (player == null){
                                sender.sendMessage("玩家不存在")
                                return@execute
                            }
                            when (type){
                                "add" -> ElysiaJob.playerSkillDataManager.addPlayerSkillPoint(player.uniqueId, id, value.toInt())
                                "take" -> ElysiaJob.playerSkillDataManager.takePlayerSkillPoint(player.uniqueId, id, value.toInt())
                                "regen" -> ElysiaJob.playerSkillDataManager.setPlayerSkillPointRegenRate(player.uniqueId, id, value.toInt())
                                "set" -> ElysiaJob.playerSkillDataManager.setPlayerSkillPoint(player.uniqueId, id, value.toInt())
                            }
                        }
                    }
                }
            }
        }
    }
    // 职业相关命令
    @CommandBody
    val job = subCommand {
        dynamic("type") {
            suggestion<CommandSender>(uncheck = false) { sender, context ->
                return@suggestion listOf("set")
            }
            dynamic("name"){
                suggestion<CommandSender>(uncheck = false) { sender, context ->
                    return@suggestion Bukkit.getOnlinePlayers().map { it.name }
                }
                dynamic("id"){
                    suggestion<CommandSender>(uncheck = false) { sender, context ->
                        return@suggestion ElysiaJob.jobDataManager.getJobIdList()
                    }
                    execute<CommandSender> { sender, context, argument ->
                        val id = context["id"]
                        val name = context["name"]
                        val player = Bukkit.getPlayer(name)
                        if (player == null) {
                            sender.sendMessage("玩家不存在")
                            return@execute
                        }
                        val jobData = ElysiaJob.jobDataManager.getJobData(id)
                        if (jobData == null) {
                            sender.sendMessage("职业不存在")
                            return@execute
                        }
                        ElysiaJob.playerDataManager.setPlayerJob(player.uniqueId, id)
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
    private fun skillCast(player: Player, skillId: String){
        val skillData = ElysiaJob.skillDataManager.getSkillData(skillId)
        if (skillData == null)
            player.sendMessage("技能不存在")
        if (SkillManager.checkSkillCastCondition(player, skillId))
            SkillManager.castSkill(player, skillId)
    }
}