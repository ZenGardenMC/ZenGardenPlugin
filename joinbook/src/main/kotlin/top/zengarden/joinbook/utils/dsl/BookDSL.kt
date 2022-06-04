package top.zengarden.joinbook.utils.dsl

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.network.protocol.game.PacketPlayOutOpenBook
import net.minecraft.world.EnumHand
import net.minecraft.world.item.ItemWrittenBook
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftMetaBook
import org.bukkit.craftbukkit.v1_18_R2.util.CraftChatMessage
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import top.zengarden.joinbook.instance
import top.zengarden.joinbook.utils.extensions.colorTranslate
import top.zengarden.joinbook.utils.extensions.item
import top.zengarden.joinbook.utils.extensions.meta
import top.zengarden.joinbook.utils.extensions.runTask

typealias PageBlock = Page.() -> Unit
typealias TextBlock = TextComponent.() -> Unit

inline fun createBook(
    title: String,
    block: BookDSL.() -> Unit,
) = BookDSL(title).apply(block)

inline fun Player.book(
    title: String,
    block: BookDSL.() -> Unit,
) {
    val ep = (player as CraftPlayer).handle
    val book = createBook(title, block).asItemStack()
    val hand = this.inventory.itemInMainHand
    this.inventory.setItemInMainHand(book)
    try {
        val nmsBook = CraftItemStack.asNMSCopy(book)
        val mainHand = EnumHand.a
        if (ItemWrittenBook.a(nmsBook, ep.cQ(), ep)) {
            ep.bV.c()
        }
        val packetPlayOutBook = PacketPlayOutOpenBook(mainHand)
        ep.b.a(packetPlayOutBook)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        instance.runTask { this@book.inventory.setItemInMainHand(hand) }
    }
}

class BookDSL(
    val title: String,
) {

    private val pages = mutableListOf<Page>()

    fun page(block: PageBlock) {
        pages.add(Page().apply(block))
    }

    fun asItemStack(): ItemStack = item(Material.WRITTEN_BOOK).meta<BookMeta> {
        title = this@BookDSL.title
        author = "Bongle"
        val f = CraftMetaBook::class.java.getDeclaredField("pages")
        f.isAccessible = true
        var list = f.get(this) as? ArrayList<String>
        if (list == null) {
            f.set(this, ArrayList<String>())
            list = f.get(this) as ArrayList<String>
        }
        this@BookDSL.pages.forEach {
            list.add(
                CraftChatMessage.toJSON(
                    CraftChatMessage.fromJSON(it.asString())
                )
            )
        }
    }

}

class Page {

    val texts = mutableListOf<TextComponent>()

    fun text(block: TextBlock) {
        texts.add(TextComponent().apply(block))
    }

    fun asString(): String {
        val baseComponents = mutableListOf<BaseComponent>()
        texts.forEach {
            val toAdd = TextComponent.fromLegacyText(it.text.colorTranslate())
            toAdd.forEach { base ->
                if (it.clickEvent != null) base.clickEvent = it.clickEvent
                if (it.hoverEvent != null) base.hoverEvent = it.hoverEvent
            }
            baseComponents.addAll(toAdd)
        }
        return ComponentSerializer.toString(TextComponent(*baseComponents.toTypedArray()))
    }

}