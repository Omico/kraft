import org.gradle.kotlin.dsl.support.expectedKotlinDslPluginsVersion

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(com.gradleup.shadow)
    implementation(consensusGradlePlugins)
    implementation(embeddedKotlin("gradle-plugin"))
    implementation(gradleKotlinDslPlugins(expectedKotlinDslPluginsVersion))
    implementation(gradmGeneratedJar)
}
