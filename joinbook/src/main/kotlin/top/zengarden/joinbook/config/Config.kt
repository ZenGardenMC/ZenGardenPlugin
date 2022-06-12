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
 * @version v3.3
 */
abstract class Config(name: String) {

    private val file: File
    var yml: YamlConfiguration
    private val properties = mutableListOf<Property<*>>()
    private val propertiesNullable = mutableListOf<PropertyNullable<*>>()

    init {
        val folder = instance.dataFolder
        if (!folder.exists()) folder.mkdir()
        this.file = File("$folder/$name.yml")
        if (this.file.createNewFile()) log("§a正在创建 ${file.path}")
        this.yml = YamlConfiguration.loadConfiguration(file)
    }

    open fun reload() {
        yml = YamlConfiguration.loadConfiguration(file)
        properties.forEach {
            it.value = null
        }
        propertiesNullable.forEach {
            it.init = false
            it.value = null
        }
    }

    fun <T : Any> value(path: String, default: T) = Property(path, default).also {
        properties.add(it)
    }

    fun <T : Any?> valueOrNull(path: String, default: T?) = PropertyNullable(path, default).also {
        propertiesNullable.add(it)
    }

    fun location(path: String, default: Location?) = PropertyNullable(path, default).also {
        propertiesNullable.add(it)
    }

    fun save() {
        yml.save(file)
    }

}

class PropertyNullable<T>(
    private val path: String,
    private val default: T?,
) {

    var value: T? = null
    var init = false

    operator fun getValue(cfg: Config, prop: KProperty<*>): T? {
        if (init && value == null) return null
        if (default == null) {
            return value ?: (cfg.yml.get(path) as? T).also {
                init = true
            }
        }
        return value ?: cfg.yml.get(path) as? T ?: default.also {
            cfg.yml.set(path, it)
            cfg.save()
            value = it
            init = true
        }
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

    var value: T? = null

    operator fun getValue(cfg: Config, prop: KProperty<*>): T {
        return value ?: cfg.yml.get(path) as? T ?: default.also {
            cfg.yml.set(path, it)
            cfg.save()
            value = it
        }
    }


    operator fun setValue(cfg: Config, property: KProperty<*>, any: T) {
        cfg.yml.set(path, any)
        cfg.save()
    }

}