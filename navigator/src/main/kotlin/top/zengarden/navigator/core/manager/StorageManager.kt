package top.zengarden.navigator.core.manager

import top.zengarden.navigator.core.manager.storage.impl.MySQL

object StorageManager {

    var engine = MySQL()

    fun init() {
        engine.init()
    }

}