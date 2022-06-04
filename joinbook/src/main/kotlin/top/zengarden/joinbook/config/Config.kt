package top.zengarden.joinbook.config

import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import top.zengarden.joinbook.instance
import top.zengarden.joinbook.log
import java.io.File
import kotlin.reflect.KProperty

/**
 * @name Config
 * @author Bongle
 * @version v3.0
 */
abstract class Config(name: String) {

    private val file: File
    var yml: YamlConfiguration

    init {
        val folder = instance.dataFolder
        if (!folder.exists()) folder.mkdir()
        this.file = File("$folder/$name.yml")
        if (this.file.createNewFile()) log("§a正在创建 ${file.path}")
        this.yml = YamlConfiguration.loadConfiguration(file)
    }

    open fun reload() {
        yml = YamlConfiguration.loadConfiguration(file)
    }

    fun <T : Any> value(path: String, default: T) = Property(path, default)

    fun <T : Any?> valueOrNull(path: String, default: T?) = Property(path, default)

    fun location(path: String, default: Location?) = PropertyNullable(path, default)

    fun save() {
        yml.save(file)
    }

}

class PropertyNullable<T>(
    private val path: String,
    private val default: T?,
) {

    operator fun getValue(cfg: Config, prop: KProperty<*>): T? {
        var value = cfg.yml.get(path)
        if (value == null) {
            cfg.yml.set(path, default)
            cfg.save()
            value = default
        }
        return value as T?
    }

    operator fun setValue(cfg: Config, property: KProperty<*>, any: T?) {
        cfg.yml.set(path, any)
        cfg.save()
    }

}

class Property<T>(
    private val path: String,
    private val default: T,
) {

    operator fun getValue(cfg: Config, prop: KProperty<*>): T {
        var value = cfg.yml.get(path)
        if (value == null) {
            cfg.yml.set(path, default)
            cfg.save()
            value = default
        }
        return value as T
    }


    operator fun setValue(cfg: Config, property: KProperty<*>, any: T) {
        cfg.yml.set(path, any)
        cfg.save()
    }

}