@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinx.serialization)
    id("convention.publication")
}

val deployVersion = findProperty("VolvoApiClientDeployVersion") as String?
version = deployVersion?.removePrefix("v") ?: "0.0.1-SNAPSHOT"
group = "com.github.ayastrebov.volvo"
description = "Volvo API client for Kotlin"

kotlin {
    explicitApi()
    jvmToolchain(17)

    androidTarget { publishLibraryVariants("release") }
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
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.io.core)
            api(libs.ktor.client.core)
            api(projects.volvoApiCore)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.io.bytestring)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.serialization.json)
        }

        commonTest.dependencies {
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
            implementation(libs.kotlinx.coroutines.test)
        }

        jvmTest.dependencies {
            implementation(kotlin("test-junit"))
            implementation(libs.ktor.client.okhttp)
            implementation(libs.logback.classic)
        }

        jsTest.dependencies {
            implementation(kotlin("test-js"))
        }

        wasmJsTest.dependencies {
            implementation(kotlin("test-wasm-js"))
        }
    }

    //https://kotlinlang.org/docs/native-objc-interop.html#export-of-kdoc-comments-to-generated-objective-c-headers
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compileTaskProvider.configure {
            compilerOptions {
                freeCompilerArgs.add("-Xexport-kdoc")
            }
        }
    }
}

android {
    namespace = "com.github.ayastrebov.volvo.client"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }
}

buildConfig {
    className("ApiConfig")
    packageName("com.github.ayastrebov.volvo.client")

    buildConfigField("API_URL", "https://api.volvocars.com")
    buildConfigField("CONNECTED_VEHICLE_API", "connected-vehicle/v2/vehicles")
    buildConfigField("ENERGY_API", "energy/v1/vehicles")
    buildConfigField("EXTENDED_VEHICLE_API", "extended-vehicle/v1/vehicles")
    buildConfigField("LOCATION_API", "location/v1/vehicles")
}

publishing.publications.create<MavenPublication>("volvo") {
    groupId = project.group.toString()
    artifactId = project.name
    version = project.version.toString()
    description = project.description
}