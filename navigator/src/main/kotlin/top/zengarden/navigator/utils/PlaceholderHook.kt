package top.zengarden.navigator.utils

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import top.zengarden.navigator.Navigator
import top.zengarden.navigator.core.manager.DataManager
import top.zengarden.navigator.utils.extensions.toCommas

class PlaceholderHook : PlaceholderExpansion() {

    override fun getIdentifier() = "nav"

    override fun getAuthor() = Navigator.AUTHOR

    override fun getVersion() = Navigator.VERSION

    override fun persist() = true

    override fun onRequest(p: OfflinePlayer, params: String): String? {
        val data = DataManager[p.uniqueId] ?: return null
        return when(params) {
            "name" -> data.checkpoint?.name
            "distance" -> data.distance?.toCommas() + "m"
            "direction" -> data.direction
            else -> null
        }
    }

}