/*
 * Copyright 2025 Omico
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.omico.gradle.initialization

import org.gradle.api.initialization.Settings
import java.io.File

internal fun Settings.includeAllSubprojectModules(projectName: String): Unit =
    collectSubprojectModules(projectName).forEach { moduleDirectory ->
        val moduleName = moduleDirectory.relativeTo(rootDir).path.replace(File.separator, "-")
        include(":$moduleName")
        project(":$moduleName").projectDir = moduleDirectory
    }

private fun Settings.collectSubprojectModules(projectName: String): Set<File> =
    mutableSetOf<File>().apply {
        val projectDirectory = rootDir.resolve(projectName)
        projectDirectory.mkdirs()
        collectSubprojectModules(projectDirectory)
    }

private fun MutableSet<File>.collectSubprojectModules(parentDirectory: File): Unit =
    parentDirectory.listFiles()
        .asSequence()
        // Only include directories.
        .filter(File::isDirectory)
        // Exclude the `<parentDirectory>/<ignoredName>` directory if `<parentDirectory>/build.gradle.kts` exists.
        .filterNot { it.name in ignoredSubprojectDirectoryNames && it.resolveSibling("build.gradle.kts").exists() }
        .forEach { file ->
            // Find any directory that contains a `build.gradle.kts` file.
            if (file.resolve("build.gradle.kts").exists()) this += file
            collectSubprojectModules(file)
        }

private val ignoredSubprojectDirectoryNames: Set<String> =
    setOf(
        ".kotlin",
        ".gradle",
        ".idea",
        "src",
        "build",
    )
