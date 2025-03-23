package com.elysia.elysiajob.hook

import com.elysia.elysiajob.ElysiaJob
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object PapiHook : PlaceholderExpansion{
    override val identifier: String = "ElysiaJob"
    override fun onPlaceholderRequest(player: Player?, args: String): String {
        if (player == null || args.isEmpty()) return ""
        if (args == "mana") return ElysiaJob.playerDataManager.getPlayerMana(player.uniqueId).toString()
        if (args == "max_mana") return ElysiaJob.playerDataManager.getPlayerMaxMana(player.uniqueId).toString()
        if (args == "mana_regen") return ElysiaJob.playerDataManager.getPlayerManaRegen(player.uniqueId).toString()
        if (args == "stamina") return ElysiaJob.playerDataManager.getPlayerStamina(player.uniqueId).toString()
        if (args == "max_stamina") return ElysiaJob.playerDataManager.getPlayerMaxStamina(player.uniqueId).toString()
        if (args == "stamina_regen") return ElysiaJob.playerDataManager.getPlayerStaminaRegen(player.uniqueId).toString()
        if (args.startsWith("skill_cooldown_")) {
            val vars = args.substringAfter("skill_cooldown_")
            return getSkillCooldown(player, vars.split("_"))
        }
        return ""
    }
    private fun getSkillCooldown(player: Player, vars: List<String>): String {
        when (vars[0]) {
            "id" -> {
                return ElysiaJob.playerSkillDataManager.getPlayerSkillCoolDown(player.uniqueId, vars[1]).toString()
            }
        }
        return "-1"
    }
}