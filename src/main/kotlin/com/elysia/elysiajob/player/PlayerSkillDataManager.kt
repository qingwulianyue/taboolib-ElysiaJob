package com.elysia.elysiajob.player

import com.elysia.elysiajob.ElysiaJob
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PlayerSkillDataManager {
    // 玩家技能冷却时间数据
    private val skillCooldownDataMap = ConcurrentHashMap<UUID, ConcurrentHashMap<String, LocalDateTime>>()
    // 设置玩家技能释放时间
    fun setPlayerSkillCastTime(uuid: UUID, skillId: String) {
        skillCooldownDataMap.computeIfAbsent(uuid) { ConcurrentHashMap() }[skillId] = LocalDateTime.now()
    }
    // 获取玩家技能剩余冷却时间
    fun getPlayerSkillCoolDown(uuid: UUID, skillId: String): Double {
        // 如果玩家没有技能数据，则返回-1
        if (!skillCooldownDataMap.containsKey(uuid)) return -1.0
        if (!skillCooldownDataMap[uuid]!!.containsKey(skillId)) return -1.0
        // 获取技能数据，若技能不存在返回-1
        val skillData = ElysiaJob.skillDataManager.getSkillData(skillId) ?: return -1.0
        // 若该技能的冷却时间为0，则剩余冷却时间为0
        if (skillData.cooldown == 0.0) return 0.0
        // 计算冷却时间
        val castTime = skillCooldownDataMap[uuid]!![skillId]
        val currentTime = LocalDateTime.now()
        // 计算已过去的时间
        val timeElapsed = Duration.between(castTime, currentTime).seconds
        // 计算剩余冷却时间
        val result = skillData.cooldown - timeElapsed
        return if (result > 0) result else 0.0
    }
}