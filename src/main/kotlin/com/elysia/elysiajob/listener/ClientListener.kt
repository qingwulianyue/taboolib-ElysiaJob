package com.elysia.elysiajob.listener

import com.elysia.elysiajob.ElysiaJob
import com.elysia.elysiajob.enums.SkillType
import com.elysia.elysiajob.skill.SkillManager
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import taboolib.common.platform.function.info

// 客户端监听器
object ClientListener: PluginMessageListener {
    override fun onPluginMessageReceived(p0: String, player: Player, p2: ByteArray) {
        val uuid = player.uniqueId
        val jobId = ElysiaJob.playerDataManager.getPlayerJob(uuid) ?: return
        val message = String(p2)
        // 待定的技能类型
        lateinit var skillType: SkillType
        // 根据客户端返回值判断技能类型
        when(message){
            "skill1" -> skillType = SkillType.SKILL1
            "skill2" -> skillType = SkillType.SKILL2
            "skill3" -> skillType = SkillType.SKILL3
            "skill4" -> skillType = SkillType.SKILL4
            "skill5" -> skillType = SkillType.SKILL5
            "special_skill" -> skillType = SkillType.SKILL6
        }
        castSkill(player, skillType)
    }
    // 判断释放技能
    private fun castSkill(player: Player, skillType: SkillType) {
        val jobId = ElysiaJob.playerDataManager.getPlayerJob(player.uniqueId) ?: return
        val jobData = ElysiaJob.jobDataManager.getJobData(jobId) ?: return
        // 待定的技能id
        val skillId: String = when(skillType){
            SkillType.SKILL1 -> jobData.skills[0]
            SkillType.SKILL2 -> jobData.skills[1]
            SkillType.SKILL3 -> jobData.skills[2]
            SkillType.SKILL4 -> jobData.skills[3]
            SkillType.SKILL5 -> jobData.skills[4]
            SkillType.SKILL6 -> jobData.skills[5]
        }
        // 判断技能是否满足释放条件
        if (SkillManager.checkSkillCastCondition(player, skillId))
            SkillManager.castSkill(player, skillId)
    }
}