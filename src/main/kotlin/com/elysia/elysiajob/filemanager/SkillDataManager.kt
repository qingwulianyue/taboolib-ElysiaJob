package com.elysia.elysiajob.filemanager

import com.elysia.elysiajob.filemanager.data.SkillData
import com.elysia.elysiajob.filemanager.data.SkillListenerData
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Configuration

// 技能数据管理器
class SkillDataManager {
    // 技能数据表
    private val skillDataMap = mutableMapOf<String, SkillData>()
    // 配置文件收集器
    private val read = ArrayList<Configuration>()
    // 加载文件
    fun loadFile(){
        read.clear()
        val file = newFolder(getDataFolder(), "skill", create = false)
        if (!file.exists()) {
            file.mkdirs()
            releaseResourceFile("skill/示例技能.yml")
        }
        file.walk()
            .filter { it.isFile }
            .filter { it.extension == "yaml" || it.extension == "yml" }
            .forEach {
                read.add(Configuration.loadFromFile(it))
            }
        loadConfiguration()
    }
    // 加载数据
    private fun loadConfiguration(){
        read.forEach {
            // 遍历一级键
            it.getKeys(false).forEach(
                fun(key: String) {
                    // 加载常规数据
                    val name = it.getString("$key.name") ?: ""
                    val mana = it.getDouble("$key.mana")
                    val stamina = it.getDouble("$key.stamina")
                    val cooldown = it.getDouble("$key.cooldown")
                    val point = it.getInt("$key.point")
                    val condition = it.getString("$key.condition") ?: ""
                    val action = it.getString("$key.action") ?: ""
                    // 获取监听器
                    val listeners = mutableListOf<SkillListenerData>()
                    // 获取监听器键
                    val listenerSection = it.getConfigurationSection("$key.listener")
                    // 遍历监听器配置
                    listenerSection?.getKeys(false)?.forEach { listenerKey ->
                        val listenerTime = listenerSection.getDouble("$listenerKey.time")
                        val listenerCondition = listenerSection.getString("$listenerKey.condition") ?: ""
                        val listenerAction = listenerSection.getString("$listenerKey.action") ?: ""
                        val skillListenerData = SkillListenerData(listenerKey, listenerTime, listenerCondition, listenerAction)
                        listeners.add(skillListenerData)
                    }
                    skillDataMap[key] = SkillData(name, mana, stamina, cooldown, point , condition , action, listeners)
                }
            )
        }
    }
    // 获取技能数据
    fun getSkillData(name: String): SkillData? {
        return skillDataMap[name]
    }
    // 获取技能id列表
    fun getSkillIdList(): List<String> {
        return skillDataMap.keys.toList()
    }
}