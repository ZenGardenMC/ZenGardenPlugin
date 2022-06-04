package top.zengarden.joinbook.core.manager.storage

import top.zengarden.joinbook.core.manager.DataManager

interface StorageEngine {

    fun init()

    fun load(data: DataManager)

    fun upsertData(data: DataManager)

    fun close()

}