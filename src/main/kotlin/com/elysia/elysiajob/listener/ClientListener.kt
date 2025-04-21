package com.elysia.elysiajob.listener

import com.elysia.elysiajob.ElysiaJob
import com.elysia.elysiajob.enums.SkillType
import com.elysia.elysiajob.skill.SkillManager
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

// 客户端监听器
object ClientListener: PluginMessageListener {
    override fun onPluginMessageReceived(p0: String, player: Player, p2: ByteArray) {
        val uuid = player.uniqueId
        val messages = String(p2).split("_")
        if (messages[0].contains("change")) changeKey(player, messages[1], messages[2])
        else if (messages[0].contains("cast") && (ElysiaJob.playerDataManager.getPlayerJob(uuid) != "")) getCastSkill(player, messages[1])
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
    private fun getCastSkill(player: Player, type : String){
        // 待定的技能类型
        lateinit var skillType: SkillType
        // 根据客户端返回值判断技能类型
        when(type){
            "skill1" -> skillType = SkillType.SKILL1
            "skill2" -> skillType = SkillType.SKILL2
            "skill3" -> skillType = SkillType.SKILL3
            "skill4" -> skillType = SkillType.SKILL4
            "skill5" -> skillType = SkillType.SKILL5
            "special_skill" -> skillType = SkillType.SKILL6
        }
        castSkill(player, skillType)
    }
    private fun changeKey(player: Player, type: String, key: String){
        val uuid = player.uniqueId
        val skillType = when(type){
            "skill1" -> SkillType.SKILL1
            "skill2" -> SkillType.SKILL2
            "skill3" -> SkillType.SKILL3
            "skill4" -> SkillType.SKILL4
            "skill5" -> SkillType.SKILL5
            "special" -> SkillType.SKILL6
            else -> return
        }
        ElysiaJob.playerDataManager.setPlayerKey(uuid, skillType, key)
    }
}