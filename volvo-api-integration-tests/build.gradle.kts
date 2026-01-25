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

    // Pass system properties for credentials
    systemProperty("volvo.apiKey", getCredential("VOLVO_API_KEY", "volvo.apiKey"))
    systemProperty("volvo.vins", getCredential("VOLVO_VINS", "volvo.vins"))
    systemProperty("volvo.token", getCredential("VOLVO_ACCESS_TOKEN", "volvo.token"))
    systemProperty("volvo.token.connectedVehicle", getCredential("VOLVO_TOKEN_CONNECTED_VEHICLE", "volvo.token.connectedVehicle"))
    systemProperty("volvo.token.energy", getCredential("VOLVO_TOKEN_ENERGY", "volvo.token.energy"))
    systemProperty("volvo.token.location", getCredential("VOLVO_TOKEN_LOCATION", "volvo.token.location"))

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        showCauses = true
        showExceptions = true
        showStackTraces = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

// Task to fetch VINs from the Volvo API
tasks.register<JavaExec>("fetchVins") {
    group = "volvo"
    description = "Fetches VINs from the Volvo API and prints them for local.properties"

    dependsOn("testClasses")

    mainClass.set("com.github.ayastrebov.volvo.api.integration.util.FetchVins")
    classpath = sourceSets["test"].runtimeClasspath

    // Pass credentials as system properties
    systemProperty("volvo.apiKey", getCredential("VOLVO_API_KEY", "volvo.apiKey"))
    systemProperty("volvo.token", getCredential("VOLVO_ACCESS_TOKEN", "volvo.token"))
    systemProperty("volvo.token.connectedVehicle", getCredential("VOLVO_TOKEN_CONNECTED_VEHICLE", "volvo.token.connectedVehicle"))
}
