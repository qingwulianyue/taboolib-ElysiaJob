package com.elysia.elysiajob.job

import com.elysia.elysiajob.ElysiaJob
import com.elysia.elysiajob.enums.MessageType
import com.elysia.elysiajob.message
import org.bukkit.entity.Player

object JobManager {
    // 设置玩家职业
    fun setPlayerJob(player: Player, jobId: String) {
        val jobData = ElysiaJob.jobDataManager.getJobData(jobId) ?: return
        // 处理职业基础数据
        ElysiaJob.playerDataManager.setPlayerJob(player.uniqueId, jobId)
        ElysiaJob.playerDataManager.setPlayerMaxMana(player.uniqueId, jobData.mana)
        ElysiaJob.playerDataManager.setPlayerManaRegen(player.uniqueId, jobData.manaRegen)
        ElysiaJob.playerDataManager.setPlayerMaxStamina(player.uniqueId, jobData.stamina)
        ElysiaJob.playerDataManager.setPlayerStaminaRegen(player.uniqueId, jobData.staminaRegen)
        message(player, MessageType.ON_SET_JOB, arrayOf(jobData.name))
    }
}