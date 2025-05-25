// Convention plugin: Reusable Gradle script for publishing Kotlin (Multiplatform or JVM) libraries
// Supports publishing to Maven Central or GitHub Packages with GPG signing and Dokka documentation

import java.util.*

plugins {
    id("maven-publish")
    id("signing")
}

// Stub secrets to avoid build failures when credentials are missing
extra["signing.keyId"] = null
extra["signing.password"] = null
extra["signing.secretKey"] = null
extra["gpr.user"] = null
extra["gpr.token"] = null

// Load secrets from local.properties or environment variables
val secretPropsFile = rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    Properties().apply {
        secretPropsFile.reader().use { load(it) }
    }.forEach { (name, value) ->
        extra[name.toString()] = value
    }
} else {
    extra["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    extra["signing.password"] = System.getenv("SIGNING_PASSWORD")
    extra["signing.secretKey"] = System.getenv("SIGNING_SECRET_KEY")
    extra["gpr.user"] = System.getenv("GPR_USER")
    extra["gpr.token"] = System.getenv("GPR_TOKEN")
}

fun getExtraString(name: String) = extra[name]?.toString()

fun getDecodedString(name: String): String? {
    val encoded = getExtraString(name)
    return encoded?.let { String(Base64.getDecoder().decode(it)) }
}

inline fun <reified T : Task> TaskContainer.safeNamed(name: String): TaskProvider<T>? =
    try {
        named<T>(name)
    } catch (_: UnknownTaskException) {
        null
    }

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ayastrebov/volvo-api-client")
            credentials {
                username = getExtraString("gpr.user")
                password = getExtraString("gpr.token")
            }
        }
    }

    publications.withType<MavenPublication>().configureEach {
        // Stub javadoc.jar artifact
        artifact(javadocJar.get())

        pom {
            name.set(project.name)
            description.set(project.description ?: "Kotlin library")
            url.set("https://github.com/AYastrebov/Volvo-Kotlin-API")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("ayastrebov")
                    name.set("Andrey Yastrebov")
                    email.set("ayastrebov@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:git://github.com/AYastrebov/Volvo-Kotlin-API.git")
                developerConnection.set("scm:git:ssh://github.com/AYastrebov/Volvo-Kotlin-API.git")
                url.set("https://github.com/AYastrebov/Volvo-Kotlin-API")
            }
        }
    }
}

// Signing setup
signing {
    val keyId = getExtraString("signing.keyId")
    val password = getExtraString("signing.password")
    val secretKey = getDecodedString("signing.secretKey")

    if (keyId != null && secretKey != null) {
        useInMemoryPgpKeys(keyId, secretKey, password)
        sign(publishing.publications)
    }
}

// Workaround: enforce signing before publishing (Gradle issue #26132)
val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    mustRunAfter(signingTasks)
}
