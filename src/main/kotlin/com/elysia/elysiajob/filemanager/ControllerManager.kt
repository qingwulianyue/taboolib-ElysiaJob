package com.elysia.elysiajob.filemanager

import com.elysia.elysiajob.filemanager.data.ControllerData
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.info
import java.io.File

class ControllerManager {
    private val controllerDataMap = mutableMapOf<String, ControllerData>()
    private val read = ArrayList<File>()
    fun loadFile(){
        val file = newFolder(getDataFolder(), "dragon/controller", create = false)
        file.walk()
            .filter { it.isFile }
            .filter { it.extension == "yaml" || it.extension == "yml" }
            .forEach {
                read.add(it)
            }
        loadConfiguration()
    }
    private fun loadConfiguration(){
        read.forEach{
            val controllerData = ControllerData(it.nameWithoutExtension, YamlConfiguration.loadConfiguration(it).saveToString())
            controllerDataMap[controllerData.name] = controllerData
        }
        info(controllerDataMap)
    }
    fun getControllerData(name: String): ControllerData? {
        return controllerDataMap[name]
    }
}