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
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.matching("GraalVM")
    }
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.commandapi.bukkit.core)

    implementation(libs.graalvm.polyglot)
    implementation(libs.graalvm.python.community)
    implementation(libs.graalvm.truffle.api)

    shadow(libs.graalvm.polyglot)
    shadow(libs.graalvm.python.community)

    annotationProcessor(libs.graalvm.truffle.dsl.processor)

    testImplementation(libs.paper.api)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
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
        jvmArgs = listOf("-XX:+UnlockDiagnosticVMOptions", "-XX:+DebugNonSafepoints", "-javaagent:${sparkFile}=start,open")
    }

    shadowJar {
        mergeServiceFiles()
    }

    test {
        useJUnitPlatform()
        jvmArgs("-XX:+UnlockExperimentalVMOptions", "-XX:+EnableJVMCI", "-XX:+UseJVMCICompiler")
    }
}