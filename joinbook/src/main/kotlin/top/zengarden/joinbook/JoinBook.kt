package top.zengarden.joinbook

import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.plugin.java.JavaPlugin
import top.zengarden.joinbook.commands.AdminCommands
import top.zengarden.joinbook.commands.BaseCommands
import top.zengarden.joinbook.config.MainConfig
import top.zengarden.joinbook.core.manager.DataManager
import top.zengarden.joinbook.core.manager.StorageManager
import top.zengarden.joinbook.listeners.BookListener
import top.zengarden.joinbook.listeners.DataListener

class JoinBook : JavaPlugin() {

    companion object {
        const val PLUGIN_NAME = "JoinBook"
        const val AUTHOR = "Bongle"
        const val VERSION = "1.0"
        lateinit var instance: JoinBook
    }

    override fun onEnable() {
        instance = this
        log("§a$PLUGIN_NAME enabled v$VERSION By $AUTHOR")
        DataListener()
        BookListener()
        MainConfig
        StorageManager.init()
        Bukkit.getPluginCommand("joinbook")?.setExecutor(AdminCommands())
        Bukkit.getPluginCommand("opengui")?.setExecutor(BaseCommands())
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

val instance: JoinBook
    get() = JoinBook.instance

fun log(msg: String) {
    Bukkit.getConsoleSender().sendMessage("§b[JoinBook-LOG] §f$msg")
}

fun error(msg: String) {
    Bukkit.getConsoleSender().sendMessage("§b[JoinBook-ERROR] §f$msg")
}