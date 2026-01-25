@file:OptIn(ExperimentalWasmDsl::class)

import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.maven.publish)
}

// Dokka configuration
dokka {
    moduleName.set("volvo-api-core")
    moduleVersion.set(project.version.toString())

    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory.set(projectDir.resolve("src"))
            remoteUrl("https://github.com/AYastrebov/Volvo-Kotlin-API/tree/master/volvo-api-core/src")
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

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
    signAllPublications()

    coordinates(
        groupId = project.group.toString(),
        artifactId = project.name,
        version = project.version.toString()
    )

    pom {
        name.set("Volvo API Core")
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
