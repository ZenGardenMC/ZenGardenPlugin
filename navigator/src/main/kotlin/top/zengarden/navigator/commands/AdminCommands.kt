package top.zengarden.navigator.commands

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import top.zengarden.navigator.Navigator
import top.zengarden.navigator.config.MainConfig
import top.zengarden.navigator.config.NavigatorConfig
import top.zengarden.navigator.core.manager.DataManager
import top.zengarden.navigator.core.navigator.Checkpoint
import top.zengarden.navigator.core.navigator.PathNavigator
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
        when(args[0].lowercase()) {
            "reload" -> {
                MainConfig.reload()
                NavigatorConfig.reload()
                sender.sendMessage("§a重载配置文件成功！")
            }
            "edit" -> {
                if (args.size != 2) {
                    sender.sendMessage("§c用法: /navigator edit <名字> - 设置导航点")
                    return true
                }
                val p = sender as? Player ?: kotlin.run {
                    sender.sendMessage("§c此指令仅玩家身份可用！")
                    return true
                }
                val id = args[1]
                var nav = NavigatorConfig.navigators.find { it.id == id }
                if (nav == null) {
                    nav = PathNavigator(id)
                    NavigatorConfig.navigators.add(nav)
                }
                nav.checkpoints.add(Checkpoint("checkpoint", p.location))
                NavigatorConfig.save()
                NavigatorConfig.reload()
                sender.sendMessage("§a添加成功！")
            }
            "remove" -> {
                if (args.size != 3) {
                    sender.sendMessage("§c用法: /navigator remove <名字> <索引> - 删除导航点")
                    return true
                }
                val id = args[1]
                val nav = NavigatorConfig.navigators.find { it.id == id }
                if (nav == null) {
                    sender.sendMessage("§c导航点不存在！")
                    return true
                }
                val index = args[2].lowercase().toIntOrNull() ?: kotlin.run {
                    sender.sendMessage("§c请输入数字！")
                    return true
                }
                if (index <= 0 || index > NavigatorConfig.navigators.size) {
                    sender.sendMessage("§c该导航不存在！")
                    return true
                }
                nav.checkpoints.removeAt(index - 1)
                NavigatorConfig.save()
            }
            "set" -> {
                if (args.size != 3) {
                    sender.sendMessage("§c用法: /navigator set <玩家名> <导航点> - 设置玩家的导航点")
                    return true
                }
                val p = Bukkit.getPlayer(args[1]) ?: kotlin.run {
                    sender.sendMessage("§c玩家不存在！")
                    return true
                }
                val id = args[2]
                val nav = NavigatorConfig.navigators.find { it.id == id }
                if (nav == null) {
                    sender.sendMessage("§c导航点不存在！")
                    return true
                }
                val data = DataManager[p.uniqueId] ?: kotlin.run {
                    sender.sendMessage("§c玩家数据不存在！")
                    return true
                }
                data.nav = nav
                sender.sendMessage("§a已为玩家 ${p.displayName} §a设置导航点！")
            }
            else -> {
                sender.sendHelp()
            }

        }
        return true
    }

    private fun CommandSender.sendHelp() {
        sendMessage("§c/navigator reload - 重置配置文件")
        sendMessage("§c/navigator edit <名字> - 设置导航点")
        sendMessage("§c/navigator set <玩家名> <导航点> - 设置玩家的导航点")
        sendMessage("§c/navigator remove <名字> <索引> - 移除导航点的检查点")
    }

    override fun onTabComplete(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>,
    ): MutableList<String>? {
        if (cmd.name == "navigator") {
            if (args.size == 1) {
                return mutableListOf("reload", "edit", "set", "remove")
            }
        }
        return null
    }

}