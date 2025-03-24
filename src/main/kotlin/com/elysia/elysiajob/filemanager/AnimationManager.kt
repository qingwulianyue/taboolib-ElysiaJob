package com.elysia.elysiajob.filemanager

import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.info
import java.io.File

class AnimationManager {
    val animationFiles = mutableMapOf<String, ByteArray>()
    private val read = ArrayList<File>()
    fun loadFile(){
        val file = newFolder(getDataFolder(), "dragon/animation", create = false)
        file.walk()
            .filter { it.isFile }
            .filter { it.extension == "json" }
            .forEach {
                read.add(it)
            }
        loadConfiguration()
    }
    private fun loadConfiguration(){
        read.forEach{
            animationFiles[it.nameWithoutExtension] = it.readBytes()
        }
        info("AnimationManager loaded ${animationFiles.size} files")
    }
}