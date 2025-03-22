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
package dev.omico.kraft.gradle

import dev.omico.kraft.gradle.internal.configureEachKotlinTarget
import dev.omico.kraft.gradle.tasks.KraftSourcesGenerator
import dev.omico.kraft.gradle.tasks.registerKraftDocumentValidatorTask
import dev.omico.kraft.gradle.tasks.registerKraftSourcesGeneratorTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation

public class KraftPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val kraftExtension = extensions.create<KraftExtension>(KraftExtension.NAME)
        val kraftDocumentValidatorTaskProvider = registerKraftDocumentValidatorTask(kraftExtension = kraftExtension)
        val kraftSourcesGeneratorTaskProvider = registerKraftSourcesGeneratorTask(kraftExtension = kraftExtension)
        kraftSourcesGeneratorTaskProvider.configure {
            dependsOn(kraftDocumentValidatorTaskProvider)
        }
        val outputDirectory = kraftSourcesGeneratorTaskProvider.flatMap(KraftSourcesGenerator::outputDirectory)
        configureEachKotlinTarget {
            compilations.named(KotlinCompilation.MAIN_COMPILATION_NAME) {
                defaultSourceSet.kotlin.srcDir(outputDirectory)
            }
        }
    }
}
