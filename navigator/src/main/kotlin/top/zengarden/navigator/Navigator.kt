package top.zengarden.navigator

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import top.zengarden.navigator.commands.AdminCommands
import top.zengarden.navigator.config.MainConfig
import top.zengarden.navigator.core.manager.DataManager
import top.zengarden.navigator.core.manager.StorageManager
import top.zengarden.navigator.listeners.DataListener

class Navigator : JavaPlugin() {

    companion object {
        const val PLUGIN_NAME = "JoinBook"
        const val AUTHOR = "Bongle"
        const val VERSION = "1.0"
        lateinit var instance: Navigator
    }

    override fun onEnable() {
        instance = this
        log("§a$PLUGIN_NAME enabled v$VERSION By $AUTHOR")
        DataListener()
        MainConfig
        StorageManager.init()
        Bukkit.getPluginCommand("navigator")?.setExecutor(AdminCommands())
    }

    override fun onDisable() {
        Bukkit.getOnlinePlayers().forEach {
            DataManager[it.uniqueId]?.save(true)
        }
        Bukkit.getScheduler().cancelTasks(this)
        StorageManager.engine.close()
        log("§a$PLUGIN_NAME disabled v$VERSION By $AUTHOR")
    }

}

val instance: Navigator
    get() = Navigator.instance

fun log(msg: String) {
    Bukkit.getConsoleSender().sendMessage("§b[Navigator-LOG] §f$msg")
}

fun error(msg: String) {
    Bukkit.getConsoleSender().sendMessage("§b[Navigator-ERROR] §f$msg")
}