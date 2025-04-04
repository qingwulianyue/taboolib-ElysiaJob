package com.elysia.elysiajob

import com.elysia.elysiajob.filemanager.*
import com.elysia.elysiajob.player.PlayerSkillDataManager
import com.elysia.elysiajob.skill.SkillManager
import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.Schedule
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.info
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import java.io.IOException
import java.io.UncheckedIOException
import java.nio.file.Files

object ElysiaJob : Plugin() {
    @Config("config.yml")
    lateinit var config: ConfigFile
    lateinit var playerDataManager: PlayerDataManager
    lateinit var skillDataManager: SkillDataManager
    lateinit var playerSkillDataManager: PlayerSkillDataManager

    // 项目使用TabooLib Start Jar 创建!
    override fun onEnable() {
        playerDataManager = PlayerDataManager()
        skillDataManager = SkillDataManager()
        playerSkillDataManager = PlayerSkillDataManager()
        playerDataManager.load()
        skillDataManager.loadFile()
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