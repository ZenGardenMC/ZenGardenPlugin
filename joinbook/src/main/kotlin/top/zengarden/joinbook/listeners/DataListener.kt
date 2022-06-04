package top.zengarden.joinbook.listeners

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import top.zengarden.joinbook.core.manager.DataManager
import top.zengarden.joinbook.instance

class DataListener : Listener{

    init {
        Bukkit.getPluginManager().registerEvents(this, instance)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(e: PlayerJoinEvent) {
        val p = e.player
        DataManager(p.uniqueId)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onQuit(e: PlayerQuitEvent) {
        val p = e.player
        DataManager[p.uniqueId]?.save(true)
    }

}