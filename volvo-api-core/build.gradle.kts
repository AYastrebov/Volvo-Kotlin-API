@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinx.serialization)
}

version = "1.0.0"
group = "com.github.ayastrebov.volvo"
description = "Volvo API core library"

kotlin {
    explicitApi()
    jvmToolchain(17)

    jvm()
    wasmJs { nodejs() }
    js { nodejs() }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    linuxX64()
    mingwX64()

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.io.core)
            api(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.serialization.core)
        }

        commonTest.dependencies {
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }

        jvmTest.dependencies {
            implementation(kotlin("test-junit"))
        }

        jsTest.dependencies {
            implementation(kotlin("test-js"))
        }

        wasmJsTest.dependencies {
            implementation(kotlin("test-wasm-js"))
        }
    }
}