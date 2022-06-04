package top.zengarden.joinbook.core.manager

import top.zengarden.joinbook.core.manager.storage.impl.MySQL

object StorageManager {

    var engine = MySQL()

    fun init() {
        engine.init()
    }

}