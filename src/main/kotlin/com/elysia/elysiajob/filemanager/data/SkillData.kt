package com.elysia.elysiajob.filemanager.data

// 技能数据
data class SkillData(val name : String, val mana : Double, val stamina : Double, val cooldown: Double, val point : Int , val condition: String , val action : String, val listener : List<SkillListenerData>)
