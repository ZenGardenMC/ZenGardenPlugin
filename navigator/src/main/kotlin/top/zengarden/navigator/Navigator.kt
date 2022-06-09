package top.zengarden.navigator

import org.bukkit.Bukkit
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin
import top.zengarden.navigator.commands.AdminCommands
import top.zengarden.navigator.config.MainConfig
import top.zengarden.navigator.config.NavigatorConfig
import top.zengarden.navigator.core.manager.DataManager
import top.zengarden.navigator.core.manager.StorageManager
import top.zengarden.navigator.core.navigator.Checkpoint
import top.zengarden.navigator.core.navigator.PathNavigator
import top.zengarden.navigator.listeners.DataListener
import top.zengarden.navigator.utils.PlaceholderHook

class Navigator : JavaPlugin() {

    companion object {
        const val PLUGIN_NAME = "Navigator"
        const val AUTHOR = "Bongle"
        const val VERSION = "1.0"
        lateinit var instance: Navigator
    }

    override fun onEnable() {
        instance = this
        log("§a$PLUGIN_NAME enabled v$VERSION By $AUTHOR")
        DataListener()
        Bukkit.getPluginManager().getPlugin("PlaceholderAPI")?.let {
            PlaceholderHook()
        }
        ConfigurationSerialization.registerClass(PathNavigator::class.java, "PathNavigator")
        ConfigurationSerialization.registerClass(Checkpoint::class.java, "Checkpoint")
        MainConfig
        NavigatorConfig
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