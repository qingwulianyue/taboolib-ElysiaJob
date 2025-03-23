package com.elysia.elysiajob.filemanager

import com.elysia.elysiajob.ElysiaJob
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.platform.function.getDataFolder
import java.io.File
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class PlayerDataManager {
    // 玩家魔力值数据
    private val playerMana = ConcurrentHashMap<UUID, Double>()
    // 玩家魔力值回复速度数据
    private val playerManaRegen = ConcurrentHashMap<UUID, Double>()
    // 玩家最大魔力值数据
    private val playerMaxMana = ConcurrentHashMap<UUID, Double>()
    // 玩家体力值数据
    private val playerStamina = ConcurrentHashMap<UUID, Double>()
    // 玩家体力值回复速度数据
    private val playerStaminaRegen = ConcurrentHashMap<UUID, Double>()
    // 玩家最大体力值数据
    private val playerMaxStamina = ConcurrentHashMap<UUID, Double>()
    // 获取玩家魔力值
    fun getPlayerMana(uuid: UUID): Double {
        return playerMana.computeIfAbsent(uuid) { ElysiaJob.config["default_mana"] as Double }
    }
    // 获取玩家魔力值回复速度
    fun getPlayerManaRegen(uuid: UUID): Double {
        return playerManaRegen.computeIfAbsent(uuid) { ElysiaJob.config["default_mana_regen"] as Double }
    }
    // 获取玩家最大魔力值
    fun getPlayerMaxMana(uuid: UUID): Double {
        return playerMaxMana.computeIfAbsent(uuid) { ElysiaJob.config["default_mana"] as Double }
    }
    // 获取玩家最大魔力值
    fun getPlayerStamina(uuid: UUID): Double {
        return playerStamina.computeIfAbsent(uuid) { ElysiaJob.config["default_stamina"] as Double }
    }
    // 获取玩家体力值回复速度
    fun getPlayerStaminaRegen(uuid: UUID): Double {
        return playerStaminaRegen.computeIfAbsent(uuid) { ElysiaJob.config["default_stamina_regen"] as Double }
    }
    // 获取玩家最大体力值
    fun getPlayerMaxStamina(uuid: UUID): Double {
        return playerMaxStamina.computeIfAbsent(uuid) { ElysiaJob.config["default_stamina"] as Double }
    }
    // 设置玩家魔力值
    fun setPlayerMana(uuid: UUID, mana: Double) {
        playerMana[uuid] = mana
    }
    // 设置玩家魔力值回复速度
    fun setPlayerManaRegen(uuid: UUID, manaRegen: Double) {
        playerManaRegen[uuid] = manaRegen
    }
    // 设置玩家最大魔力值
    fun setPlayerMaxMana(uuid: UUID, maxMana: Double) {
        playerMaxMana[uuid] = maxMana
    }
    // 设置玩家体力值
    fun setPlayerStamina(uuid: UUID, stamina: Double) {
        playerStamina[uuid] = stamina
    }
    // 设置玩家体力值回复速度
    fun setPlayerStaminaRegen(uuid: UUID, staminaRegen: Double) {
        playerStaminaRegen[uuid] = staminaRegen
    }
    // 设置玩家最大体力值
    fun setPlayerMaxStamina(uuid: UUID, maxStamina: Double) {
        playerMaxStamina[uuid] = maxStamina
    }
    // 扣除玩家魔力值
    fun takePlayerMana(uuid: UUID, mana: Double){
        // 数据检查
        if (!playerMana.contains(uuid)) playerMana[uuid] = ElysiaJob.config["default_mana"] as Double
        // 如果玩家当前魔力值大于等于扣除值，则扣除，否则设置为0
        if (playerMana[uuid]!! >= mana)
            playerMana[uuid] = playerMana[uuid]!! - mana
        else
            playerMana[uuid] = 0.0
    }
    // 扣除玩家体力值
    fun takePlayerStamina(uuid: UUID, stamina: Double){
        // 数据检查
        if (!playerStamina.contains(uuid)) playerStamina[uuid] = ElysiaJob.config["default_stamina"] as Double
        // 如果玩家当前魔力值大于等于扣除值，则扣除，否则设置为0
        if (playerStamina[uuid]!! >= stamina)
            playerStamina[uuid] = playerStamina[uuid]!! - stamina
        else
            playerStamina[uuid] = 0.0
    }
    // 增加玩家魔力值
    fun addPlayerMana(uuid: UUID, mana: Double){
        // 数据检查
        if (!playerMaxMana.contains(uuid)) playerMaxMana[uuid] = ElysiaJob.config["default_mana"] as Double
        if (!playerMana.contains(uuid)) playerMana[uuid] = ElysiaJob.config["default_mana"] as Double
        // 如果玩家当前魔力值加上增加值小于等于最大魔力值，则增加，否则设置为最大魔力值
        if (playerMana[uuid]!! + mana <= playerMaxMana[uuid]!!)
            playerMana[uuid] = playerMana[uuid]!! + mana
        else
            playerMana[uuid] = playerMaxMana[uuid]!!
    }
    // 增加玩家体力值
    fun addPlayerStamina(uuid: UUID, stamina: Double){
        // 数据检查
        if (!playerMaxStamina.contains(uuid)) playerMaxStamina[uuid] = ElysiaJob.config["default_stamina"] as Double
        if (!playerStamina.contains(uuid)) playerStamina[uuid] = ElysiaJob.config["default_stamina"] as Double
        // 如果玩家当前魔力值加上增加值小于等于最大魔力值，则增加，否则设置为最大魔力值
        if (playerStamina[uuid]!! + stamina <= playerMaxStamina[uuid]!!)
            playerStamina[uuid] = playerStamina[uuid]!! + stamina
        else
            playerStamina[uuid] = playerMaxStamina[uuid]!!
    }
    // 保存玩家数据
    fun save(){
        for (uuid in playerMana.keys){
            val playerDataPath = getDataFolder().toPath().resolve("PlayerData").resolve(uuid.toString())
            val yamlConfiguration = YamlConfiguration()
            yamlConfiguration.set("uuid", uuid.toString())
            yamlConfiguration.set("mana_regen", playerManaRegen[uuid])
            yamlConfiguration.set("max_mana", playerMaxMana[uuid])
            yamlConfiguration.set("stamina_regen", playerStaminaRegen[uuid])
            yamlConfiguration.set("max_stamina", playerMaxStamina[uuid])
            try {
                yamlConfiguration.save(playerDataPath.toFile())
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    // 加载玩家数据
    fun load(){
        findAllFiles(getDataFolder().toPath().resolve("PlayerData").toFile())
    }
    // 寻找所有文件
    private fun findAllFiles(folder : File){
        val files = folder.listFiles()
        if (files != null){
            for (file in files){
                if (file.isDirectory){
                    findAllFiles(file)
                } else {
                    val playerDataFolder = File( "${folder}/${file.name}")
                    val config = YamlConfiguration.loadConfiguration(playerDataFolder)
                    loadPlayerData(config)
                }
            }
        }
    }
    // 加载玩家数据
    private fun loadPlayerData(config : YamlConfiguration){
        val uuid = UUID.fromString(config.getString("uuid"))
        playerManaRegen[uuid] = config.getDouble("mana_regen")
        playerMaxMana[uuid] = config.getDouble("max_mana")
        playerStaminaRegen[uuid] = config.getDouble("stamina_regen")
        playerMaxStamina[uuid] = config.getDouble("max_stamina")
    }
}