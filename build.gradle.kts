plugins {
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.buildConfig).apply(false)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinx.serialization).apply(false)
    alias(libs.plugins.kotlinx.binary.validator) apply false
    alias(libs.plugins.multiplatform).apply(false)

    alias(libs.plugins.caupain)
}

val deployVersion = findProperty("VolvoApiClientDeployVersion") as String?
version = deployVersion?.removePrefix("v") ?: "0.0.1-SNAPSHOT"

// Dokka configuration for aggregated documentation
dokka {
    moduleName.set("Volvo Kotlin API")
    moduleVersion.set(project.version.toString())

    dokkaPublications.html {
        outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
        failOnWarning.set(true)
    }

    dokkaSourceSets.configureEach {
        externalDocumentationLinks.register("ktor") {
            url("https://api.ktor.io/")
        }
        externalDocumentationLinks.register("kotlinx-coroutines") {
            url("https://kotlinlang.org/api/kotlinx.coroutines/")
        }
        externalDocumentationLinks.register("kotlinx-serialization") {
            url("https://kotlinlang.org/api/kotlinx.serialization/")
        }
    }

    pluginsConfiguration.html {
        footerMessage.set("Volvo Kotlin API - MIT License")
        homepageLink.set("https://github.com/AYastrebov/Volvo-Kotlin-API")
    }
}

// Include subprojects in aggregated documentation
dependencies {
    dokka(project(":volvo-api-core"))
    dokka(project(":volvo-api-client"))
}
