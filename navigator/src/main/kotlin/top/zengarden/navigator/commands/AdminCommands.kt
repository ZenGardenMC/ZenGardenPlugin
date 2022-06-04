package top.zengarden.navigator.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import top.zengarden.navigator.Navigator
import top.zengarden.navigator.config.MainConfig
import top.zengarden.navigator.instance

class AdminCommands : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(
                "§9${Navigator.PLUGIN_NAME} §e由 §a${Navigator.AUTHOR} §e编写，版本: §b${Navigator.VERSION}"
            )
            return true
        }

        // Check Permission
        if (!sender.hasPermission("navigator.admin")) {
            sender.sendMessage("§c你没有权限使用该命令！")
            return true
        }
        when(args[0]) {
            "reload" -> {
                MainConfig.reload()
                sender.sendMessage("§a重载配置文件成功！")
            }
            else -> {
                sender.sendHelp()
            }
        }
        return true
    }

    private fun CommandSender.sendHelp() {
        sendMessage("§c/navigator reload - 重置配置文件")
    }

    override fun onTabComplete(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>,
    ): MutableList<String>? {
        if (cmd.name == "navigator") {
            if (args.size == 1) {
                return mutableListOf("reload")
            }
        }
        return null
    }

}