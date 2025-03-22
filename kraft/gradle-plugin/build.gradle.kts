@file:Suppress("UnstableApiUsage")

plugins {
    id("kraft.kotlin.gradle-plugin")
}

kotlin {
    explicitApi()
}

gradlePlugin {
    plugins {
        register("kraft") {
            id = "dev.omico.kraft"
            implementationClass = "dev.omico.kraft.gradle.KraftPlugin"
        }
    }
}

dependencies {
    compileOnly(kotlin("gradle-plugin"))
}

dependencies {
    implementation(project(":kraft-core"))
}

dependencies {
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test"))
    testImplementation(project(":kraft-test"))
}

val testKitRuntimeOnly: Configuration by configurations.creating
dependencies {
    testKitRuntimeOnly(elucidator)
    testKitRuntimeOnly(kotlin("gradle-plugin"))
    testKitRuntimeOnly(kotlinpoet)
}

tasks {
    pluginUnderTestMetadata {
        pluginClasspath.from(testKitRuntimeOnly)
    }
    validatePlugins {
        enableStricterValidation = true
    }
    test {
        useJUnitPlatform()
    }
}
