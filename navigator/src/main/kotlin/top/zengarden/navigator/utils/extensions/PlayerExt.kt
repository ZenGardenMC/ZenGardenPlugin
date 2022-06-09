package top.zengarden.navigator.utils.extensions

import net.minecraft.network.protocol.Packet
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer
import org.bukkit.entity.Player
import kotlin.math.abs
import kotlin.math.atan2

fun Player.pos(loc: Location): Double {
    val pLoc = this.location
    val angle = Math.toDegrees(atan2(loc.x - pLoc.x, loc.z - pLoc.z))
    return angleDifference(
        convertToAngle(angle),
        360 - convertToAngle(this.location.yaw.toDouble())
    )
}

fun Player.direction(loc: Location) = when (this.pos(loc)) {
    in (-7.5)..7.5 -> "↑"
    in (-82.5)..(-7.5) -> "↗"
    in (-97.5)..(-82.5) -> "→"
    in (-172.5)..(-97.5) -> "↙"
    in (-180.0)..(-172.5) -> "↓"
    in 172.5..180.0 -> "↓"
    in 97.5..172.5 -> "↘"
    in 82.5..97.5 -> "←"
    in 7.5..82.5 -> "↖"
    else -> "•"
}

fun Player.distance(loc: Location) = location.distance(loc)

private fun angleDifference(a: Double, b: Double): Double {
    val d = abs(a - b) % 360
    val r = if (d > 180) 360 - d else d
    return r * if (a - b in 0.0..180.0 || (a - b <= -180 && a + b >= -360)) 1 else -1
}

private fun convertToAngle(yaw: Double): Double {
    // wrap yaw
    val x = (((yaw + 180) % 360) + 360) % 360 - 180
    return x + 180
}

fun Player.sendPacket(packet: Packet<*>) = (this as CraftPlayer).handle.b.a.a(packet)