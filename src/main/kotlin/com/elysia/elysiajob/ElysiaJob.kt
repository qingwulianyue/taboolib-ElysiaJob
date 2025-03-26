package com.elysia.elysiajob

import com.elysia.elysiajob.filemanager.*
import com.elysia.elysiajob.listener.ClientListener
import com.elysia.elysiajob.skill.PlayerSkillDataManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.platform.Plugin
import taboolib.common.platform.Schedule
import taboolib.common.platform.function.info
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object ElysiaJob : Plugin() {
    @Config("config.yml")
    lateinit var config: ConfigFile
    lateinit var playerDataManager: PlayerDataManager
    lateinit var skillDataManager: SkillDataManager
    lateinit var jobDataManager: JobDataManager
    lateinit var playerSkillDataManager: PlayerSkillDataManager

    // 项目使用TabooLib Start Jar 创建!
    override fun onEnable() {
        playerDataManager = PlayerDataManager()
        skillDataManager = SkillDataManager()
        jobDataManager = JobDataManager()
        playerSkillDataManager = PlayerSkillDataManager()
        playerDataManager.loadFile()
        skillDataManager.loadFile()
        jobDataManager.loadFile()
        Bukkit.getServer().pluginManager.getPlugin("ElysiaJob")
            ?.let { Bukkit.getMessenger().registerIncomingPluginChannel(it, "ElysiaJob" ,ClientListener) }
        FileListener.load()
        submit()
    }
    override fun onDisable() {
        playerDataManager.save()
        FileListener.clear()
    }
    // 魔力值体力值回复任务
    @Schedule(period = 20, async = true)
    fun manaRegenerationTask(){
        for (player in Bukkit.getOnlinePlayers()){
            val uuid = player.uniqueId
            if (playerDataManager.getPlayerMana(uuid) < playerDataManager.getPlayerMaxMana(uuid))
                playerDataManager.setPlayerMana(uuid,
                    (playerDataManager.getPlayerMana(uuid) + playerDataManager.getPlayerManaRegen(uuid)).coerceAtMost(
                        playerDataManager.getPlayerMaxMana(uuid)
                    )
                )
            if (playerDataManager.getPlayerStamina(uuid) < playerDataManager.getPlayerMaxStamina(uuid))
                playerDataManager.setPlayerStamina(uuid,
                    (playerDataManager.getPlayerStamina(uuid) + playerDataManager.getPlayerStaminaRegen(uuid)).coerceAtMost(
                        playerDataManager.getPlayerMaxStamina(uuid)
                    )
                )
        }
    }
    // 自动保存
    private fun submit(
        now: Boolean = false,
        async: Boolean = true,
        period: Long = config.getLong("save_timer"),
    ) {
        playerDataManager.save()
        if (config.getBoolean("save_tips"))
            info("开始自动保存")
    }
}