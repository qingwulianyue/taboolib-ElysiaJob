package com.elysia.elysiajob.filemanager

import com.elysia.elysiajob.filemanager.data.SkillData
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Configuration

class SkillDataManager {
    private val skillDataMap = mutableMapOf<String, SkillData>()
    private val read = ArrayList<Configuration>()
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
    private fun loadConfiguration(){
        read.forEach {
            it.getKeys(false).forEach(
                fun(key: String) {
                    val name = it.getString("$key.name") ?: ""
                    val mana = it.getDouble("$key.mana")
                    val stamina = it.getDouble("$key.stamina")
                    val cooldown = it.getDouble("$key.cooldown")
                    val action = it.getString("$key.action") ?: ""
                    skillDataMap[key] = SkillData(name, mana, stamina, cooldown, action)
                }
            )
        }
    }
    fun getSkillData(name: String): SkillData? {
        return skillDataMap[name]
    }
    fun getSkillIdList(): List<String> {
        return skillDataMap.keys.toList()
    }
}