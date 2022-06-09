package top.zengarden.navigator.utils

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import top.zengarden.navigator.Navigator
import top.zengarden.navigator.core.manager.DataManager

class PlaceholderHook : PlaceholderExpansion() {

    override fun getIdentifier() = "navigator"

    override fun getAuthor() = Navigator.AUTHOR

    override fun getVersion() = Navigator.VERSION

    override fun onRequest(p: OfflinePlayer, params: String): String? {
        if (!p.isOnline) return null
        val data = DataManager[p.uniqueId] ?: return null
        return when(params) {
            "distance" -> data.distance?.toString()
            "direction" -> data.direction
            else -> null
        }
    }

}