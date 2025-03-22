plugins {
    `kotlin-dsl`
    id("me.omico.gradm") version "4.0.0-beta03"
}

repositories {
    mavenCentral()
}

gradm {
    pluginId = "kraft.gradm"
    debug = true
}
