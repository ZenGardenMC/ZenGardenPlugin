package top.zengarden.joinbook.core.manager

import org.bukkit.entity.Player
import java.util.UUID

fun Player.hasJoinedServerBefore() = DataManager[this.uniqueId]?.hasJoinedBefore

class DataManager(
    val uuid: UUID,
    var hasJoinedBefore: Boolean = false
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