package top.zengarden.joinbook.utils.extensions

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.material.MaterialData

inline fun item(
    material: Material,
    amount: Int = 1,
    data: Short = 0,
    meta: ItemMeta.() -> Unit = {},
): ItemStack = ItemStack(material, amount, data).meta(meta)

inline fun <reified T : ItemMeta> metadataItem(
    material: Material,
    amount: Int = 1,
    data: Short = 0,
    meta: T.() -> Unit,
): ItemStack = ItemStack(material, amount, data).meta(meta)

inline fun <reified T : ItemMeta> ItemStack.meta(
    block: T.() -> Unit,
): ItemStack = apply {
    itemMeta = (itemMeta as? T)?.apply(block) ?: itemMeta
}

fun ItemStack.displayName(displayName: String): ItemStack = meta<ItemMeta> {
    this.setDisplayName(displayName)
}

fun ItemStack.amount(amount: Int): ItemStack {
    this.amount = amount
    return this
}

fun ItemStack.lore(lore: List<String>): ItemStack = meta<ItemMeta> {
    this.lore = lore
}

inline fun Material.asItemStack(
    amount: Int = 1,
    data: Short = 0,
    meta: ItemMeta.() -> Unit = {},
): ItemStack = item(this, amount, data, meta)

fun MaterialData.toItemStack(
    amount: Int = 1,
    meta: ItemMeta.() -> Unit = {},
) = toItemStack(amount).meta(meta)