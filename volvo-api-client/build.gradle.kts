@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.dokka)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    id("convention.publication")
}

val projectId = "com.github.ayastrebov.volvo.client"

group = projectId
val deployVersion = findProperty("VolvoApiClientDeployVersion") as String?
version = deployVersion?.removePrefix("v") ?: "0.0.1-SNAPSHOT"
description = "Volvo API client for Kotlin"

kotlin {
    jvmToolchain(17)

    androidTarget { publishLibraryVariants("release") }
    jvm()
    wasmJs { browser() }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    linuxX64()
    mingwX64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
        }

        commonTest.dependencies {
            implementation(libs.junit)
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
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

//val sourcesJar = tasks.register<Jar>("sourcesJar") {
//    archiveClassifier.set("sources")
//    from(project.sourceSets.main.map { it.allSource })
//}
//val dokkaJavadocJar = tasks.register<Jar>("dokkaJavadocJar") {
//    archiveClassifier.set("javadoc")
//    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
//}
//
//publishing {
//    publications {
//        create<MavenPublication>("telegram") {
//            groupId = projectId
//            artifactId = project.name
//            version = project.version.toString()
//            description = project.description
//
//            from(components["java"])
//            artifact(sourcesJar)
//            artifact(dokkaJavadocJar)
//        }
//    }
//}
