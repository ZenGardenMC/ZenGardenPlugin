package top.zengarden.joinbook.listeners

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.meta.BookMeta
import top.zengarden.joinbook.JoinBook
import top.zengarden.joinbook.config.MainConfig
import top.zengarden.joinbook.core.manager.DataManager
import top.zengarden.joinbook.core.manager.hasJoinedServerBefore
import top.zengarden.joinbook.instance
import top.zengarden.joinbook.utils.dsl.book
import top.zengarden.joinbook.utils.extensions.colorTranslate
import top.zengarden.joinbook.utils.extensions.item
import top.zengarden.joinbook.utils.extensions.meta
import top.zengarden.joinbook.utils.extensions.runTask

fun Player.showJoinBook() {
    this.book(MainConfig.title) {
        MainConfig.content.forEach {
            page {
                text {
                    text = it
                }
            }
        }
    }
    this.inventory.addItem(item(Material.WRITTEN_BOOK).meta<BookMeta> {
        title = MainConfig.title.colorTranslate()
        author = JoinBook.AUTHOR
        val pages = mutableListOf<String>()
        MainConfig.content.forEach {
            pages.add(it.colorTranslate())
        }
        this.pages = pages
    })
}

class BookListener : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, instance)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onJoin(e: PlayerJoinEvent) {
        val p = e.player
        if (p.hasJoinedServerBefore() == true) return
        p.showJoinBook()
        DataManager[p.uniqueId]?.hasJoinedBefore = true
        instance.runTask(async = true) {
            DataManager[p.uniqueId]?.save(false)
        }
    }

}