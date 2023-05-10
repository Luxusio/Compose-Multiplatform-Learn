import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.8.20"
    // id("app.cash.sqldelight") version "2.0.0-alpha05"
    id("com.squareup.sqldelight") version "1.5.5"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

sqldelight {
    database("AppDatabase") {
        packageName = "db"
    }
    // databases {
    //     create("AppDatabase") {
    //         sourceFolders.set(listOf("sqldelight"))
    //     }
    // }
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {

                implementation("com.darkrockstudios:mpfilepicker:1.1.0")
                implementation("com.arkivanov.decompose:decompose:1.0.0")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("com.squareup.sqldelight:gradle-plugin:1.5.5")
                implementation("com.squareup.sqldelight:sqlite-driver:1.5.5")

                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Compose-Multiplatform-Learn"
            packageVersion = "1.0.0"
        }
    }
}
