import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")
}

dependencies {

    // Kotlin
    api(kotlin("stdlib"))

    // Spigot
    compileOnly("org.spigotmc:spigot:1.18.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")

    // PlaceHolderAPI
    compileOnly("me.clip:placeholderapi:2.11.1")

    // HikariCP
    implementation("com.zaxxer:HikariCP:4.0.3")

    // Particles
    implementation("xyz.xenondevs:particle:1.7.1")

}

tasks {

    val fatJar by named("shadowJar", ShadowJar::class) {
        archiveFileName.set("${project.name}-${project.version}.jar")

        dependencies {
            exclude(dependency("org.slf4j:.*"))
        }

        relocate("xyz.xenondevs", "top.zengarden.libs.xyz.xenondevs")
        relocate("kotlin", "top.zengarden.libs.kotlin")
        relocate("org.jetbrains", "top.zengarden.libs.org.jetbrains")
        relocate("com.zaxxer", "top.zengarden.libs.com.zaxxer")
        minimize()
    }

    artifacts {
        add("archives", fatJar)
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    test {
        useJUnit()
    }

    build {
        dependsOn(shadowJar)
    }

}