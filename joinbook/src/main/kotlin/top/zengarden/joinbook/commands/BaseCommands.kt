package top.zengarden.joinbook.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import top.zengarden.joinbook.listeners.showJoinBook

class BaseCommands : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        val p = sender as? Player ?: kotlin.run {
            sender.sendMessage("§c该指令仅玩家使用！")
            return true
        }
        p. showJoinBook()
        return true
    }

}