package top.zengarden.navigator.core.manager

import java.util.UUID

class DataManager(
    val uuid: UUID,
) {

    companion object {

        private val map = HashMap<UUID, DataManager>()

        operator fun get(uuid: UUID) = map[uuid]

    }

    init {
        StorageManager.engine.load(this)
        map[uuid] = this
    }

    fun save(destroy: Boolean) {
        StorageManager.engine.upsertData(this)
        if (destroy) map.remove(uuid)
    }

}