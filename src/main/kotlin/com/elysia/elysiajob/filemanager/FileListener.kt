package com.elysia.elysiajob.filemanager

import com.elysia.elysiajob.ElysiaJob
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.info
import taboolib.common5.FileWatcher
import java.io.File

object FileListener {

    private val listening = mutableSetOf<File>()

    private val watcher: FileWatcher = FileWatcher.INSTANCE

    private fun listener(file: File, runnable: File.() -> Unit) {
        watcher.addSimpleListener(file, runnable)
        listening.add(file)
    }

    fun clear() {
        listening.removeIf {
            val remove = !it.exists()
            if (remove) {
                watcher.removeListener(it)
            }
            remove
        }
    }

    fun load() {
        val file = File(getDataFolder(), "config.yml")
        listener(file) {
            info("监听文件重载了")
            ElysiaJob.config.reload()
        }
    }

}