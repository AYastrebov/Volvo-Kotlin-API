@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.maven.publish)
}

// Dokka configuration
dokka {
    moduleName.set("volvo-api-client")
    moduleVersion.set(project.version.toString())

    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory.set(projectDir.resolve("src"))
            remoteUrl("https://github.com/AYastrebov/Volvo-Kotlin-API/tree/master/volvo-api-client/src")
            remoteLineSuffix.set("#L")
        }

        includes.from("MODULE.md")
    }

    dokkaPublications.html {
        outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
    }
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
            implementation(libs.ktor.client.mock)
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

        val androidUnitTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

android {
    namespace = "com.github.ayastrebov.volvo.client"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
    }
}

buildConfig {
    className("ApiConfig")
    packageName("com.github.ayastrebov.volvo.client")

    buildConfigField("VERSION", project.version.toString())
    buildConfigField("API_URL", "https://api.volvocars.com")
    buildConfigField("CONNECTED_VEHICLE_API", "connected-vehicle/v2/vehicles")
    buildConfigField("ENERGY_API", "energy/v2/vehicles")
    buildConfigField("EXTENDED_VEHICLE_API", "extended-vehicle/v1/vehicles")
    buildConfigField("LOCATION_API", "location/v1/vehicles")
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    coordinates(
        groupId = project.group.toString(),
        artifactId = project.name,
        version = project.version.toString()
    )

    pom {
        name.set("Volvo API Client")
        description.set(project.description)
        url.set("https://github.com/AYastrebov/Volvo-Kotlin-API")
        inceptionYear.set("2024")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("ayastrebov")
                name.set("Andrey Yastrebov")
                email.set("ayastrebov@gmail.com")
                url.set("https://github.com/AYastrebov")
            }
        }

        scm {
            url.set("https://github.com/AYastrebov/Volvo-Kotlin-API")
            connection.set("scm:git:git://github.com/AYastrebov/Volvo-Kotlin-API.git")
            developerConnection.set("scm:git:ssh://github.com/AYastrebov/Volvo-Kotlin-API.git")
        }
    }
}

// Configure GitHub Packages repository
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/AYastrebov/Volvo-Kotlin-API")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: findProperty("gpr.token") as String?
            }
        }
    }
}
