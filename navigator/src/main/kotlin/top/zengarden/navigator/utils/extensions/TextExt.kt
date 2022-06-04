package top.zengarden.navigator.utils.extensions

import org.bukkit.ChatColor

fun String.colorTranslate(colorChar: Char = '&'): String = ChatColor.translateAlternateColorCodes(colorChar, this)

fun List<String>.colorTranslate(colorChar: Char = '&'): List<String> {
    val list = mutableListOf<String>()
    this.forEach {
        list.add(it.colorTranslate())
    }
    return list
}
