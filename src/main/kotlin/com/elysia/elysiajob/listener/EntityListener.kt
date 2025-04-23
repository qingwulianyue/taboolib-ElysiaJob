package com.elysia.elysiajob.listener

import com.elysia.elysiajob.ElysiaJob
import com.elysia.elysiajob.enums.SkillListenerType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.platform.util.attacker

// 实体造成伤害监听器
@SubscribeEvent
fun onEntityDamage(event: EntityDamageByEntityEvent) {
    val attacker = event.attacker
    val entity = event.entity
    // 若双方都不是玩家则不处理
    if (attacker !is Player && entity !is Player) return
    // 若攻击方为玩家
    if (attacker is Player) executeEventListener(attacker, SkillListenerType.PLAYER_ATTACK)
    if (entity is Player) executeEventListener(entity, SkillListenerType.PLAYER_DEFENCE)
}
private fun executeEventListener(player: Player, skillListenerType: SkillListenerType){
    val skillListenerData = ElysiaJob.playerSkillDataManager.checkPlayerListener(player.uniqueId, skillListenerType)
    if (skillListenerData == null) return
    // 处理条件
    if (skillListenerData.condition.isNotEmpty()){
        val condition = KetherShell.eval(
            skillListenerData.condition,
            ScriptOptions.new {
                sender(player)
            }
        ).thenApply { it }
        // 如果触发条件不为空且不通过则不处理
        if (condition.get() == false) return
    }
    // 处理动作
    KetherShell.eval(
        skillListenerData.action,
        ScriptOptions.new {
            sender(player)
        }
    )
}
