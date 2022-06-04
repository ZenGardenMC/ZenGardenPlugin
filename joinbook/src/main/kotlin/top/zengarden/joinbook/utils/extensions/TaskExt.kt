package top.zengarden.joinbook.utils.extensions

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

typealias RunnableBlock = BukkitRunnable.() -> Unit

inline fun runnable(crossinline block: RunnableBlock) = object : BukkitRunnable() {
    override fun run() {
        this.block()
    }
}

inline fun Plugin.runTask(
    async: Boolean = false,
    crossinline block: RunnableBlock,
): BukkitTask = runnable(block).run {
    if (async) runTaskAsynchronously(this@runTask) else runTask(this@runTask)
}

inline fun Plugin.runDelayedTask(
    async: Boolean = false,
    delay: TaskTime,
    crossinline block: RunnableBlock,
): BukkitTask = runnable(block).run {
    if (async) runTaskLaterAsynchronously(this@runDelayedTask, delay.time) else runTaskLater(
        this@runDelayedTask,
        delay.time
    )
}

inline fun Plugin.runLoopedTask(
    async: Boolean = false,
    delay: TaskTime = TaskTime(0L),
    interval: TaskTime,
    crossinline block: RunnableBlock,
): BukkitTask = runnable(block).run {
    if (async) runTaskTimerAsynchronously(this@runLoopedTask, delay.time, interval.time) else runTaskTimer(
        this@runLoopedTask,
        delay.time,
        interval.time
    )
}

val Double.sec
    get() = TaskTime((this * 20).toLong())

val Int.tick
    get() = TaskTime((this).toLong())

val Int.sec
    get() = TaskTime((this * 20).toLong())

val Int.min
    get() = TaskTime((this * 20 * 60).toLong())

data class TaskTime(val time: Long = 0L)