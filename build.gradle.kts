import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "2.0.22"
    kotlin("jvm") version "2.1.10"
}

taboolib {
    env {
        // 安装模块
        install(Basic, Bukkit, BukkitHook, BukkitNMSUtil, Kether    )
    }
    version {
        taboolib = "6.2.2"
        coroutines = "1.10.1"
    }
    description {
        dependencies {
            name("MythicMobs")
        }
    }
    relocate("ink.ptms.um", "com.elysia.elysiajob.um")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:12004:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    compileOnly("io.netty:netty-all:5.0.0.Alpha2")
    taboo("ink.ptms:um:1.0.1")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
