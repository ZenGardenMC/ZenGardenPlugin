package top.zengarden.navigator.utils.extensions

import org.bukkit.ChatColor
import java.text.DecimalFormat
import java.util.regex.Pattern

private val DECIMAL_FORMAT = DecimalFormat("#,###.##")

/**
 * Make the numbers in to trisection format
 * e.g. 100000000.0 -> 100,000,000.00
 */
fun Double.toCommas(): String {
    return DECIMAL_FORMAT.format(this).removeSuffix(".00").removeSuffix(".0")
}

/**
 * Make the numbers in to trisection format
 * e.g. 100000000 -> 100,000,000
 */
fun Int.toCommas(): String {
    return this.toDouble().toCommas()
}

fun String.colorTranslate(colorChar: Char = '&'): String = ChatColor.translateAlternateColorCodes(colorChar, this)

fun List<String>.colorTranslate(colorChar: Char = '&'): List<String> {
    val list = mutableListOf<String>()
    this.forEach {
        list.add(it.colorTranslate())
    }
    return list
}
