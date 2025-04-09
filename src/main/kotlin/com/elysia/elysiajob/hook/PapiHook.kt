package com.elysia.elysiajob.hook

import com.elysia.elysiajob.ElysiaJob
import com.elysia.elysiajob.enums.SkillType
import org.bukkit.entity.Player
import taboolib.common.platform.function.info
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
        // 获取玩家职业
        if (args == "job") return ElysiaJob.playerDataManager.getPlayerJob(player.uniqueId) ?: ""
        // 获取技能剩余冷却时间
        if (args.startsWith("skill_remain_cooldown_")) {
            val vars = args.substringAfter("skill_remain_cooldown_")
            return getSkillRemainCooldown(player, vars.split("_"))
        }
        // 获取技能冷却时间
        if (args.startsWith("skill_cooldown_")) {
            val vars = args.substringAfter("skill_cooldown_")
            return getSkillCooldown(player, vars.split("_"))
        }
        // 获取指定按键的技能名
        if (args.startsWith("skill_name_")) {
            val vars = args.substringAfter("skill_name_")
            return getSkillName(player, vars)
        }
        // 获取技能所需技能点
        if (args.startsWith("skill_point_")) {
            val vars = args.substringAfter("skill_point_")
            return getSkillPoint(player, vars.split("_"))
        }
        // 获取技能剩余所需技能点
        if (args.startsWith("skill_remain_point_")) {
            val vars = args.substringAfter("skill_remain_point_")
            return getSkillRemainPoint(player, vars.split("_"))
        }
        return ""
    }
    private fun getSkillRemainCooldown(player: Player, vars: List<String>): String {
        when (vars[0]) {
            // 获取指定id的技能剩余冷却时间
            "id" -> {
                return ElysiaJob.playerSkillDataManager.getPlayerSkillRemainCoolDown(player.uniqueId, vars[1]).toString()
            }
            // 获取指定按键的技能剩余冷却时间
            "key" -> {
                val key = when(vars[1]) {
                    "1" -> SkillType.SKILL1
                    "2" -> SkillType.SKILL2
                    "3" -> SkillType.SKILL3
                    "4" -> SkillType.SKILL4
                    "5" -> SkillType.SKILL5
                    "6" -> SkillType.SKILL6
                    else -> return "-1"
                }
                return ElysiaJob.playerSkillDataManager.getPlayerKeySKillRemainCollDown(player.uniqueId, key).toString()
            }
        }
        return "-1"
    }
    private fun getSkillCooldown(player: Player, vars: List<String>): String {
        when (vars[0]) {
            // 获取指定id的技能冷却时间
            "id" -> {
                return ElysiaJob.skillDataManager.getSkillData(vars[1])?.cooldown.toString()
            }
            // 获取指定按键的技能冷却时间
            "key" -> {
                if (ElysiaJob.playerDataManager.getPlayerJob(player.uniqueId) == null) return "-1"
                val jobData = ElysiaJob.jobDataManager.getJobData(ElysiaJob.playerDataManager.getPlayerJob(player.uniqueId)!!) ?: return "-1"
                val skillId = when(vars[1]) {
                    "1" -> jobData.skills[0]
                    "2" -> jobData.skills[1]
                    "3" -> jobData.skills[2]
                    "4" -> jobData.skills[3]
                    "5" -> jobData.skills[4]
                    "6" -> jobData.skills[5]
                    else -> return "-1"
                }
                return ElysiaJob.skillDataManager.getSkillData(skillId)?.cooldown.toString()
            }
        }
        return "-1"
    }
    private fun getSkillName(player: Player, key: String): String {
        val uuid = player.uniqueId
        // 如果玩家没有职业，则返回-1
        if (ElysiaJob.playerDataManager.getPlayerJob(uuid) == null) return "-1"
        // 获取玩家的职业数据， 若职业不存在返回-1
        val jobData = ElysiaJob.jobDataManager.getJobData(ElysiaJob.playerDataManager.getPlayerJob(uuid)!!) ?: return "-1"
        // 获取对应按键的技能id
        val skillId = when (key) {
            "1" -> jobData.skills[0]
            "2" -> jobData.skills[1]
            "3" -> jobData.skills[2]
            "4" -> jobData.skills[3]
            "5" -> jobData.skills[4]
            "6" -> jobData.skills[5]
            else -> return ""
        }
        return ElysiaJob.skillDataManager.getSkillData(skillId)?.name ?: ""
    }
    private fun getSkillRemainPoint(player: Player, vars: List<String>): String {
        var skillId = ""
        when (vars[0]) {
            // 获取指定id的技能技能点
            "id" ->
                skillId = vars[1]
            // 获取指定按键的技能技能点
            "key" -> {
                if (ElysiaJob.playerDataManager.getPlayerJob(player.uniqueId) == null) return "0"
                val jobData =
                    ElysiaJob.jobDataManager.getJobData(ElysiaJob.playerDataManager.getPlayerJob(player.uniqueId)!!)
                        ?: return "0"
                skillId = when (vars[1]) {
                    "1" -> jobData.skills[0]
                    "2" -> jobData.skills[1]
                    "3" -> jobData.skills[2]
                    "4" -> jobData.skills[3]
                    "5" -> jobData.skills[4]
                    "6" -> jobData.skills[5]
                    else -> return "0"
                }
            }
        }
        val point = ElysiaJob.skillDataManager.getSkillData(skillId)?.point ?: return "0"
        return (point - ElysiaJob.playerSkillDataManager.getPlayerSkillPoint(player.uniqueId, skillId)).toString()
    }
    private fun getSkillPoint(player: Player, vars: List<String>): String {
        when (vars[0]) {
            // 获取指定id的技能技能点
            "id" -> {
                return ElysiaJob.skillDataManager.getSkillData(vars[1])?.point.toString()
            }
            // 获取指定按键的技能技能点
            "key" -> {
                if (ElysiaJob.playerDataManager.getPlayerJob(player.uniqueId) == null) return "0"
                val jobData = ElysiaJob.jobDataManager.getJobData(ElysiaJob.playerDataManager.getPlayerJob(player.uniqueId)!!) ?: return "-1"
                val skillId = when(vars[1]) {
                    "1" -> jobData.skills[0]
                    "2" -> jobData.skills[1]
                    "3" -> jobData.skills[2]
                    "4" -> jobData.skills[3]
                    "5" -> jobData.skills[4]
                    "6" -> jobData.skills[5]
                    else -> return "0"
                }
                return ElysiaJob.skillDataManager.getSkillData(skillId)?.point.toString()
            }
        }
        return "0"
    }
}