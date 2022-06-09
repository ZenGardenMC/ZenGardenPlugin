package top.zengarden.navigator.core.manager

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask
import top.zengarden.navigator.core.CheckpointEffect
import top.zengarden.navigator.core.navigator.Checkpoint
import top.zengarden.navigator.core.navigator.PathNavigator
import top.zengarden.navigator.instance
import top.zengarden.navigator.utils.extensions.*
import xyz.xenondevs.particle.data.color.RegularColor
import xyz.xenondevs.particle.task.ParticleTask
import java.awt.Color
import java.util.UUID

class DataManager(
    val uuid: UUID,
) {

    var nav: PathNavigator? = null
    var index: Int = 0
    var particle: CheckpointEffect? = null
    val checkpoint: Checkpoint?
        get() = nav?.checkpoints?.get(index)
    val distance: Double?
        get() = checkpoint?.loc?.let {
            Bukkit.getPlayer(uuid)?.distance(it)
        }
    val direction: String?
        get() = checkpoint?.loc?.let {
            Bukkit.getPlayer(uuid)?.direction(it)
        }


    companion object {

        private val map = HashMap<UUID, DataManager>()

        operator fun get(uuid: UUID) = map[uuid]

    }

    init {
        StorageManager.engine.load(this)
        map[uuid] = this
    }

    private fun nextCheckPoint() {
        val index = this.index
        if (nav == null) {
            return
        }
        particle?.cancel()
        particle = null
        this.index = index + 1
    }

    fun check() {
        val nav = this.nav ?: return
        val p = Bukkit.getPlayer(uuid) ?: return
        nav.checkpoints[index].loc.let {
            p.distance(it).let { distance ->
                if (distance < 25 && particle == null) {
                    particle = CheckpointEffect(
                        p,
                        if (nav.hasNext(index)) {
                            RegularColor(255, 255, 0)
                        } else RegularColor(0, 255, 0),
                        it
                    )
                    particle?.start()
                }
                if (distance > 2) {
                    return
                }
                if (nav.hasNext(index)) {
                    nextCheckPoint()
                    return
                }
                nav.cmds.forEach { cmd ->
                    p.performCommand(cmd)
                }
                particle?.cancel()
                particle = null
                this.nav = null
                this.index = 0
            }
        }
    }

    fun save(destroy: Boolean) {
        StorageManager.engine.upsertData(this)
        if (destroy) map.remove(uuid)
    }

}