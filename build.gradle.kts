import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

                implementation("com.badoo.reaktive:reaktive:1.2.1")
                implementation("com.badoo.reaktive:reaktive-testing:1.2.1")
                implementation("com.badoo.reaktive:utils:1.2.1")
                implementation("com.badoo.reaktive:coroutines-interop:1.2.1")

                implementation("com.arkivanov.mvikotlin:rx:3.0.0")
                implementation("com.arkivanov.mvikotlin:mvikotlin:3.0.0")
                implementation("com.arkivanov.mvikotlin:mvikotlin-main:3.0.0")
                implementation("com.arkivanov.mvikotlin:mvikotlin-logging:3.0.0")
                implementation("com.arkivanov.mvikotlin:mvikotlin-timetravel:3.0.0")
                implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-reaktive:3.0.0")

                implementation("com.arkivanov.decompose:decompose:1.0.0")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:1.0.0")

                implementation("com.squareup.sqldelight:gradle-plugin:1.5.5")
                implementation("com.squareup.sqldelight:android-driver:1.5.5")
                implementation("com.squareup.sqldelight:sqlite-driver:1.5.5")
                implementation("com.squareup.sqldelight:native-driver:1.5.5")
                implementation("com.squareup.sqldelight:sqljs-driver:1.5.5")

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
