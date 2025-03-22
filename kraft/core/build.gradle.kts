plugins {
    id("kraft.kotlin.jvm.base")
    id("com.gradleup.shadow")
}

dependencies {
    implementation(swagger.v3.codegen)
}

val shadowImplementation: Configuration by configurations.creating {
    isTransitive = false
}

dependencies {
    fun embedded(dependency: Any) {
        compileOnly(dependency)
        shadowImplementation(dependency)
    }
    embedded(elucidator)
    embedded(kotlinpoet)
}

tasks {
    shadowJar {
        archiveClassifier = ""
        configurations = listOf(shadowImplementation)
        relocate("com.squareup.kotlinpoet", "dev.omico.kraft.core.internal.shadow.kotlinpoet")
        relocate("me.omico.elucidator", "dev.omico.kraft.core.internal.shadow.elucidator")
    }
}
