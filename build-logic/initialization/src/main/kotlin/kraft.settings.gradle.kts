import me.omico.gradle.initialization.includeAllSubprojectModules
import me.omico.gradm.addDeclaredRepositories

addDeclaredRepositories()

plugins {
    id("kraft.develocity")
    id("kraft.gradm")
}

includeBuild("build-logic/project")

includeAllSubprojectModules("kraft")
