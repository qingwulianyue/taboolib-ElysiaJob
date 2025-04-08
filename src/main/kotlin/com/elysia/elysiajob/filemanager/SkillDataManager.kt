package com.elysia.elysiajob.filemanager

import com.elysia.elysiajob.filemanager.data.SkillData
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
            it.getKeys(false).forEach(
                fun(key: String) {
                    val name = it.getString("$key.name") ?: ""
                    val mana = it.getDouble("$key.mana")
                    val stamina = it.getDouble("$key.stamina")
                    val cooldown = it.getDouble("$key.cooldown")
                    val condition = it.getString("$key.condition") ?: ""
                    val action = it.getString("$key.action") ?: ""
                    skillDataMap[key] = SkillData(name, mana, stamina, cooldown, condition , action)
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