package com.elysia.elysiajob.filemanager

import com.elysia.elysiajob.filemanager.data.JobData
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Configuration

// 职业数据管理器
class JobDataManager {
    // 职业数据表
    private val jobDataMap = mutableMapOf<String, JobData>()
    // 配置文件收集器
    private val read = ArrayList<Configuration>()
    // 加载文件
    fun loadFile(){
        read.clear()
        val file = newFolder(getDataFolder(), "job", create = false)
        if (!file.exists()) {
            file.mkdirs()
            releaseResourceFile("job/示例职业.yml")
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
                    val manaRegen = it.getDouble("$key.manaRegen")
                    val stamina = it.getDouble("$key.stamina")
                    val staminaRegen = it.getDouble("$key.staminaRegen")
                    val skills = it.getStringList("$key.skills")
                    jobDataMap[name] = JobData(name, mana, manaRegen, stamina, staminaRegen, skills)
                }
            )
        }
    }
    // 获取职业数据
    fun getJobData(name: String): JobData? {
        return jobDataMap[name]
    }
    // 获取职业id列表
    fun getJobIdList(): List<String> {
        return jobDataMap.keys.toList()
    }
}