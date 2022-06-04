package top.zengarden.joinbook.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import top.zengarden.joinbook.JoinBook
import top.zengarden.joinbook.config.MainConfig
import top.zengarden.joinbook.instance
import top.zengarden.joinbook.listeners.showJoinBook

class AdminCommands : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(
                "§9${JoinBook.PLUGIN_NAME} §e由 §a${JoinBook.AUTHOR} §e编写，版本: §b${JoinBook.VERSION}"
            )
            return true
        }

        // Check Permission
        if (!sender.hasPermission("joinbook.admin")) {
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
        sendMessage("§c/joinbook reload - 重置配置文件")
        sendMessage("§c/opengui - 打开书本页面")
    }

    override fun onTabComplete(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>,
    ): MutableList<String>? {
        if (cmd.name == "joinbook") {
            if (args.size == 1) {
                return mutableListOf("reload")
            }
        }
        return null
    }

}