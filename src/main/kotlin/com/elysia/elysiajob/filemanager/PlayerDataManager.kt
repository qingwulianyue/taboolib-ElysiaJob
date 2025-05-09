package com.elysia.elysiajob.filemanager

import com.elysia.elysiajob.ElysiaJob
import com.elysia.elysiajob.enums.SkillType
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import taboolib.module.configuration.Configuration
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

// 玩家持久化数据管理器
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
    // 玩家职业数据
    private val playerJob = ConcurrentHashMap<UUID, String>()
    // 玩家按键数据
    private val playerKey = ConcurrentHashMap<UUID, ConcurrentHashMap<SkillType, String>>()
    // 配置文件收集
    private val read = ArrayList<Configuration>()
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
    // 获取玩家职业
    fun getPlayerJob(uuid: UUID): String? {
        return playerJob[uuid]
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
    // 设置玩家职业
    fun setPlayerJob(uuid: UUID, job: String) {
        playerJob[uuid] = job
    }
    // 移除玩家职业
    fun removePlayerJob(uuid: UUID) {
        playerJob.remove(uuid)
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
    // 获取玩家按键数据
    fun getPlayerKey(uuid: UUID, skillType: SkillType): String {
        if (!playerKey.containsKey(uuid)) initPlayerKey(uuid)
        return playerKey[uuid]!![skillType]!!
    }
    // 设置玩家按键数据
    fun setPlayerKey(uuid: UUID, skillType: SkillType, key: String){
        if (!playerKey.contains(uuid)) initPlayerKey(uuid)
        playerKey[uuid]!![skillType] = key
    }
    private fun initPlayerKey(uuid: UUID){
        val keys = ConcurrentHashMap<SkillType, String>()
        keys[SkillType.SKILL1] = "Z"
        keys[SkillType.SKILL2] = "X"
        keys[SkillType.SKILL3] = "R"
        keys[SkillType.SKILL4] = "V"
        keys[SkillType.SKILL5] = "B"
        keys[SkillType.SKILL6] = "LCONTROL"
        playerKey[uuid] = keys
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
            yamlConfiguration.set("job", playerJob[uuid])
            for (skillType in SkillType.entries){
                yamlConfiguration.set(skillType.name, playerKey[uuid]!![skillType])
            }
            try {
                yamlConfiguration.save(playerDataPath.toFile())
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    // 查找玩家数据文件
    fun loadFile(){
        read.clear()
        val file = newFolder(getDataFolder(), "PlayerData", create = false)
        if (!file.exists()) {
            file.mkdirs()
        }
        file.walk()
            .filter { it.isFile }
            .forEach {
                read.add(Configuration.loadFromFile(it))
            }
        loadPlayerData()
    }
    // 加载玩家数据
    private fun loadPlayerData(){
        read.forEach{
            val uuid = UUID.fromString( it.getString("uuid"))
            playerManaRegen[uuid] = it.getDouble("mana_regen")
            playerMaxMana[uuid] = it.getDouble("max_mana")
            playerStaminaRegen[uuid] = it.getDouble("stamina_regen")
            playerMaxStamina[uuid] = it.getDouble("max_stamina")
            playerJob[uuid] = it.getString("job") ?: ""
            val keys = ConcurrentHashMap<SkillType, String>()
            for (skillType in SkillType.entries)
                keys[skillType] = it.getString(skillType.name) ?: ""
            playerKey[uuid] = keys
        }
    }
}