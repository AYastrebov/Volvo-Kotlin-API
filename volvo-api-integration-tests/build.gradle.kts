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
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.logback.classic)
}

tasks.test {
    useJUnitPlatform()

    // Pass system properties for credentials
    systemProperty("volvo.apiKey", System.getenv("VOLVO_API_KEY") ?: findProperty("volvo.apiKey") ?: "")
    systemProperty("volvo.token", System.getenv("VOLVO_ACCESS_TOKEN") ?: findProperty("volvo.token") ?: "")
    systemProperty("volvo.vin", System.getenv("VOLVO_VIN") ?: findProperty("volvo.vin") ?: "")

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}
