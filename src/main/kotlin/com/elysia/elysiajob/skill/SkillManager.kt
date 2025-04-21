package com.elysia.elysiajob.skill

import com.elysia.elysiajob.ElysiaJob
import com.elysia.elysiajob.enums.MessageType
import com.elysia.elysiajob.message
import org.bukkit.entity.Player
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions

object SkillManager {
    // 检查技能是否可以施法
    fun checkSkillCastCondition(player: Player, skillId: String): Boolean {
        val uuid = player.uniqueId
        val skillData = ElysiaJob.skillDataManager.getSkillData(skillId)
        // 检查剩余冷却时间
        if (ElysiaJob.playerSkillDataManager.getPlayerSkillRemainCoolDown(uuid, skillId) > 0) {
            message(player, MessageType.ON_COOLDOWN, arrayOf(skillData!!.name, ElysiaJob.playerSkillDataManager.getPlayerSkillRemainCoolDown(uuid, skillId).toString()))
            return false
        }
        // 检查魔力值
        if (ElysiaJob.playerDataManager.getPlayerMana(uuid) < skillData!!.mana) {
            message(player, MessageType.ON_MANA_NOT_ENOUGH, arrayOf((skillData.mana - ElysiaJob.playerDataManager.getPlayerMana(uuid)).toString()))
            return false
        }
        // 检查体力值
        if (ElysiaJob.playerDataManager.getPlayerStamina(uuid) < skillData.stamina) {
            message(player, MessageType.ON_STAMINA_NOT_ENOUGH, arrayOf((skillData.stamina - ElysiaJob.playerDataManager.getPlayerStamina(uuid)).toString()))
            return false
        }
        // 检查条件
        if (skillData.condition.isEmpty()) return true
        val condition = KetherShell.eval(
            skillData.condition,
            ScriptOptions.new {
                sender(player)
            }
        ).thenApply { it }
        return condition.get() != false
    }

    // 释放技能
    fun castSkill(player: Player, skillId: String) {
        val uuid = player.uniqueId
        val skillData = ElysiaJob.skillDataManager.getSkillData(skillId) ?: return
        // 执行技能动作部分
        KetherShell.eval(
            skillData.action,
            ScriptOptions.new {
                sender(player)
            }
        )
        // 扣除魔力值和体力值，并设置技能施法时间
        ElysiaJob.playerDataManager.takePlayerMana(uuid, skillData.mana)
        ElysiaJob.playerDataManager.takePlayerStamina(uuid, skillData.stamina)
        ElysiaJob.playerSkillDataManager.setPlayerSkillCastTime(uuid, skillId)
        message(player, MessageType.ON_CAST, arrayOf(skillData.name))
    }
}