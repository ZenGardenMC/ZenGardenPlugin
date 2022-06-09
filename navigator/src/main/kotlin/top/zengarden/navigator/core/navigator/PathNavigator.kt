package top.zengarden.navigator.core.navigator

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs
import top.zengarden.navigator.core.manager.DataManager
import top.zengarden.navigator.instance
import top.zengarden.navigator.utils.extensions.*

@SerializableAs("PathNavigator")
class PathNavigator(
    val id: String,
    val cmds: List<String> = listOf(),
    val checkpoints: MutableList<Checkpoint> = mutableListOf()
) : ConfigurationSerializable {

    companion object {

        init {
            instance.runLoopedTask(async = true, interval = 1.tick) {
                Bukkit.getOnlinePlayers().forEach {
                    DataManager[it.uniqueId]?.check()
                }
            }
        }

        @JvmStatic
        fun deserialize(map: Map<String, Any>): PathNavigator {
            return PathNavigator(
                map["id"] as String,
                map["cmds"] as List<String>,
                map["checkpoints"] as MutableList<Checkpoint>
            )
        }

    }

    fun hasNext(index: Int) = index + 1 < checkpoints.size

    override fun serialize(): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["id"] = id
        map["checkpoints"] = checkpoints
        map["cmds"] = cmds
        return map
    }

}

@SerializableAs("Checkpoint")
data class Checkpoint(
    val name: String,
    val loc: Location
) : ConfigurationSerializable {

    companion object {

        @JvmStatic
        fun deserialize(map: Map<String, Any>): Checkpoint {
            return Checkpoint(
                map["name"] as String,
                map["loc"] as Location
            )
        }

    }

    override fun serialize(): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["name"] = name
        map["loc"] = loc
        return map
    }

}