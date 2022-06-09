package top.zengarden.navigator.core

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import top.zengarden.navigator.instance
import xyz.xenondevs.particle.ParticleBuilder
import xyz.xenondevs.particle.ParticleEffect
import xyz.xenondevs.particle.data.color.RegularColor
import kotlin.math.cos
import kotlin.math.sin

class CheckpointEffect(
    val p: Player,
    val color: RegularColor,
    val loc: Location,
    val radius: Double = 1.0,
) : BukkitRunnable() {

    var degree = 0.0
    var y = 0.0
    var reverse: Boolean = false

    override fun run() {
        if (!p.isOnline){
            this.cancel()
            return
        }
        val radians = Math.toRadians(degree)
        repeat(15) {
            val effectLoc = if (it % 2 == 0) {
                loc.clone().add(radius * cos(radians), y, radius * sin(radians))
            } else {
                loc.clone().add(radius * sin(radians), y, radius * cos(radians))
            }
            ParticleBuilder(ParticleEffect.REDSTONE, effectLoc.clone()
                .add(0.0, it.toDouble() / 3f, 0.0))
                .setParticleData(color)
                .setAmount(2)
                .display(p)
        }
        if (reverse) y -= 0.1
        else y += 0.1

        if (y >= 2) reverse = true
        if (y <= 0) reverse = false

        if (degree >= 360) {
            degree = 0.0
        } else {
            degree += 20.0
        }
    }

    fun start() {
        runTaskTimerAsynchronously(instance, 0L, 2L)
    }

}