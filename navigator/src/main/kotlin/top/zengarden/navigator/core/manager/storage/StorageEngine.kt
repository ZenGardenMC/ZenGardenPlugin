package top.zengarden.navigator.core.manager.storage

import top.zengarden.navigator.core.manager.DataManager

interface StorageEngine {

    fun init()

    fun load(data: DataManager)

    fun upsertData(data: DataManager)

    fun close()

}