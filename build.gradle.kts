import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription
import java.net.URI
import java.nio.file.Files
import kotlin.io.path.exists

plugins {
    java
    id("com.gradleup.shadow") version "9.0.0"
    id("de.eldoria.plugin-yml.paper") version "0.7.1"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "com.floweytf"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public/")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

val codeGeneratorSourceSet = sourceSets.create("codegen")

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("dev.jorel:commandapi-bukkit-core:9.4.1")
    compileOnly("org.graalvm.python:python-language:23.1.8")
    "codegenImplementation"("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    "codegenImplementation"("org.ow2.asm:asm:9.8")
    "codegenImplementation"("org.ow2.asm:asm-tree:9.8")
    "codegenImplementation"("org.jetbrains:annotations:26.0.2")
    shadow(implementation("org.graalvm.polyglot:polyglot:23.1.8")!!)
    shadow(implementation("org.graalvm.polyglot:python-community:23.1.8")!!)
}

paper {
    main = "com.floweytf.mechthon.MechthonPlugin"
    hasOpenClassloader = false
    generateLibrariesJson = false
    foliaSupported = true
    apiVersion = "1.20"
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    authors = listOf("Flowey")

    serverDependencies {
        create("CommandAPI") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = true
            joinClasspath = true
        }
    }
}

// download spark standalone
val sparkFile = projectDir.resolve(".gradle").resolve("spark.jar").toPath()
val sparkUrl =
    "https://ci.lucko.me/job/spark/492/artifact/spark-standalone-agent/build/libs/spark-1.10.142-standalone-agent.jar"

if (!sparkFile.exists()) {
    URI.create(sparkUrl).toURL().openStream().use {
        Files.copy(it, sparkFile)
    }
}

tasks {
    runServer {
        minecraftVersion("1.20.4")
        jvmArgs = listOf(
            "-XX:+UnlockExperimentalVMOptions",
            "-XX:+EnableJVMCI",
            "-javaagent:${sparkFile}=start,open"
        )
    }

    shadowJar {
        mergeServiceFiles()
        // evil hack to patch out the GIL
        filesMatching("com/oracle/graal/python/nodes/call/CallNode.class") {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    }
}