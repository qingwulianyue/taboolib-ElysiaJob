package com.elysia.elysiajob.skill

import com.elysia.elysiajob.ElysiaJob
import com.elysia.elysiajob.enums.SkillType
import taboolib.common.platform.function.info
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap

// 玩家技能数据管理器
class PlayerSkillDataManager {
    // 玩家技能冷却时间数据
    private val skillCooldownDataMap = ConcurrentHashMap<UUID, ConcurrentHashMap<String, LocalDateTime>>()
    // 玩家技能点数数据
    private val skillPointDataMap = ConcurrentHashMap<UUID, ConcurrentHashMap<String, Int>>()
    // 玩家技能点回复速率
    private val skillPointRegenRate = ConcurrentHashMap<UUID, ConcurrentHashMap<String, Int>>()
    // 需要恢复技能点数的玩家
    private val needRegenPlayer = ConcurrentHashMap<UUID, MutableList<String>>()
    // 设置玩家技能释放时间
    fun setPlayerSkillCastTime(uuid: UUID, skillId: String) {
        skillCooldownDataMap.computeIfAbsent(uuid) { ConcurrentHashMap() }[skillId] = LocalDateTime.now()
    }
    // 获取玩家技能剩余冷却时间
    fun getPlayerSkillRemainCoolDown(uuid: UUID, skillId: String): Double {
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
    // 获取玩家指定按键的技能剩余冷却时间
    fun getPlayerKeySKillRemainCollDown(uuid: UUID, key: SkillType): Double {
        // 如果玩家没有职业，则返回-1
        if (ElysiaJob.playerDataManager.getPlayerJob(uuid) == null) return -1.0
        // 获取玩家的职业数据， 若职业不存在返回-1
        val jobData = ElysiaJob.jobDataManager.getJobData(ElysiaJob.playerDataManager.getPlayerJob(uuid)!!) ?: return -1.0
        // 获取对应按键的技能id
        val skillId = when (key) {
            SkillType.SKILL1 -> jobData.skills[0]
            SkillType.SKILL2 -> jobData.skills[1]
            SkillType.SKILL3 -> jobData.skills[2]
            SkillType.SKILL4 -> jobData.skills[3]
            SkillType.SKILL5 -> jobData.skills[4]
            SkillType.SKILL6 -> jobData.skills[5]
        }
        // 获取技能剩余冷却时间
        return getPlayerSkillRemainCoolDown(uuid, skillId)
    }
    // 获取玩家技能点数
    fun getPlayerSkillPoint(uuid: UUID, skillId: String): Int {
        // 如果玩家没有技能数据，则创建一个空的技能数据
        skillPointDataMap.computeIfAbsent(uuid) { ConcurrentHashMap() }
        // 如果玩家没有该技能的技能点数数据，则创建一个空的技能点数数据
        if (!skillPointDataMap[uuid]!!.containsKey(skillId)) {
            skillPointDataMap[uuid]!![skillId] = 0
        }
        // 返回技能点数
        return skillPointDataMap[uuid]!![skillId]!!
    }
    // 设定玩家技能点数
    fun setPlayerSkillPoint(uuid: UUID, skillId: String, point: Int) {
        // 如果玩家没有技能数据，则创建一个空的技能数据
        skillPointDataMap.computeIfAbsent(uuid) { ConcurrentHashMap() }
        // 设定玩家技能点数
        skillPointDataMap[uuid]!![skillId] = point
    }
    // 增加玩家技能点数
    fun addPlayerSkillPoint(uuid: UUID, skillId: String, point: Int) {
        // 如果玩家没有技能数据，则创建一个空的技能数据
        skillPointDataMap.computeIfAbsent(uuid) { ConcurrentHashMap() }
        // 如果玩家没有该技能的技能点数数据，则创建一个空的技能点数数据
        if (!skillPointDataMap[uuid]!!.containsKey(skillId))
            skillPointDataMap[uuid]!![skillId] = 0
        // 增加玩家技能点数
        if (skillPointDataMap[uuid]!![skillId]!! + point >= ElysiaJob.skillDataManager.getSkillData(skillId)!!.point){
            // 当技能点数大于等于技能点数上限，则将技能点数设置为技能点数上限
            skillPointDataMap[uuid]!![skillId] = ElysiaJob.skillDataManager.getSkillData(skillId)!!.point
            // 从需要恢复的技能点数列表中移除该技能
            needRegenPlayer.computeIfAbsent(uuid) { mutableListOf() }
            if (needRegenPlayer[uuid]!!.contains(skillId)) {
                needRegenPlayer[uuid]!!.remove(skillId)
                if (needRegenPlayer[uuid]!!.isEmpty())
                    needRegenPlayer.remove(uuid)
            }
        }
        // 当技能点数小于技能点数上限，则将技能点数增加point
        else
            skillPointDataMap[uuid]!![skillId] = skillPointDataMap[uuid]!![skillId]!! + point
    }
    // 减少玩家技能点数
    fun takePlayerSkillPoint(uuid: UUID, skillId: String, point: Int) {
        // 如果玩家没有技能数据，则创建一个空的技能数据
        skillPointDataMap.computeIfAbsent(uuid) { ConcurrentHashMap() }
        // 如果玩家没有该技能的技能点数数据，则创建一个空的技能点数数据
        if (!skillPointDataMap[uuid]!!.containsKey(skillId)) {
            skillPointDataMap[uuid]!![skillId] = 0
        }
        // 减少玩家技能点数
        if (skillPointDataMap[uuid]!![skillId]!! < point)
            skillPointDataMap[uuid]!![skillId] = 0
        else
            skillPointDataMap[uuid]!![skillId] = skillPointDataMap[uuid]!![skillId]!! - point
        if (!skillPointRegenRate.containsKey(uuid)) return
        // 如果技能回复速率不为0，则将技能加入需要回复的技能点数列表
        if (skillPointRegenRate[uuid]!![skillId]!! != 0) {
            needRegenPlayer.computeIfAbsent(uuid) { mutableListOf() }
            if (!needRegenPlayer[uuid]!!.contains(skillId)) {
                needRegenPlayer[uuid]!!.add(skillId)
            }
        }
    }
    // 设定玩家技能点数回复速率
    fun setPlayerSkillPointRegenRate(uuid: UUID, skillId: String, rate: Int) {
        // 如果玩家没有技能数据，则创建一个空的技能数据
        skillPointRegenRate.computeIfAbsent(uuid) { ConcurrentHashMap() }
        // 设定玩家技能点数回复速率
        skillPointRegenRate[uuid]!![skillId] = rate
        if (rate == 0) {
            // 若技能回复速率为0，则从需要回复的技能点数列表中移除该技能
            needRegenPlayer.computeIfAbsent(uuid) { mutableListOf() }
            needRegenPlayer[uuid]!!.remove(skillId)
            if (needRegenPlayer[uuid]!!.isEmpty()) {
                needRegenPlayer.remove(uuid)
            }
        } else {
            // 若技能回复速率不为0，则将技能加入需要回复的技能点数列表
            needRegenPlayer.computeIfAbsent(uuid) { mutableListOf() }
            if (!needRegenPlayer[uuid]!!.contains(skillId)) {
                needRegenPlayer[uuid]!!.add(skillId)
            }
        }
    }
    // 获取所需恢复技能点数的玩家列表
    fun getNeedRegenPlayerList(): ConcurrentHashMap<UUID, MutableList<String>> {
        return needRegenPlayer
    }
    // 玩家技能点数恢复
    fun skillPointRegen(uuid: UUID) {
        val needRegenSkill = skillPointRegenRate[uuid]
        if (!needRegenSkill.isNullOrEmpty()) {
            for ((skillId, rate) in needRegenSkill){
                addPlayerSkillPoint(uuid, skillId, rate)
            }
        }
    }
}