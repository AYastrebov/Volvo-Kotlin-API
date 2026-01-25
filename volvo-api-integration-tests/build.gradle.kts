import java.util.Properties

plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":volvo-api-client"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.client.okhttp)

    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.logback.classic)
}

// Load local.properties if it exists
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

fun getCredential(envVar: String, propertyKey: String): String {
    return System.getenv(envVar)
        ?: localProperties.getProperty(propertyKey)
        ?: ""
}

tasks.test {
    useJUnitPlatform()

    // Disable caching - integration tests should always run fresh
    outputs.upToDateWhen { false }

    // Pass system properties for credentials (VIN is fetched from API)
    systemProperty("volvo.apiKey", getCredential("VOLVO_API_KEY", "volvo.apiKey"))
    systemProperty("volvo.token", getCredential("VOLVO_ACCESS_TOKEN", "volvo.token"))

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        showCauses = true
        showExceptions = true
        showStackTraces = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}
