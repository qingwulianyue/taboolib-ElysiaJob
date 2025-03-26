package com.elysia.elysiajob.hook

import com.elysia.elysiajob.ElysiaJob
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

// papi变量
object PapiHook : PlaceholderExpansion{
    override val identifier: String = "ElysiaJob"
    override fun onPlaceholderRequest(player: Player?, args: String): String {
        if (player == null || args.isEmpty()) return ""
        // 获取当前魔力值
        if (args == "mana") return ElysiaJob.playerDataManager.getPlayerMana(player.uniqueId).toString()
        // 获取最大魔力值
        if (args == "max_mana") return ElysiaJob.playerDataManager.getPlayerMaxMana(player.uniqueId).toString()
        // 获取魔力恢复数值
        if (args == "mana_regen") return ElysiaJob.playerDataManager.getPlayerManaRegen(player.uniqueId).toString()
        // 获取当前耐力值
        if (args == "stamina") return ElysiaJob.playerDataManager.getPlayerStamina(player.uniqueId).toString()
        // 获取最大耐力值
        if (args == "max_stamina") return ElysiaJob.playerDataManager.getPlayerMaxStamina(player.uniqueId).toString()
        // 获取耐力恢复数值
        if (args == "stamina_regen") return ElysiaJob.playerDataManager.getPlayerStaminaRegen(player.uniqueId).toString()
        // 获取技能冷却时间
        if (args.startsWith("skill_cooldown_")) {
            val vars = args.substringAfter("skill_cooldown_")
            return getSkillCooldown(player, vars.split("_"))
        }
        return ""
    }
    private fun getSkillCooldown(player: Player, vars: List<String>): String {
        when (vars[0]) {
            // 获取指定id的技能冷却时间
            "id" -> {
                return ElysiaJob.playerSkillDataManager.getPlayerSkillCoolDown(player.uniqueId, vars[1]).toString()
            }
        }
        return "-1"
    }
}