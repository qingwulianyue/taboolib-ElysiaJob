package com.elysia.elysiajob.job

import com.elysia.elysiajob.ElysiaJob
import org.bukkit.entity.Player
import java.util.*

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
    }
}