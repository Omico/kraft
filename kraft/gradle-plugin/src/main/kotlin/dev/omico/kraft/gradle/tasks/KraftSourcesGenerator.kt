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
package dev.omico.kraft.gradle.tasks

import dev.omico.kraft.core.codegen.KraftGenerationTarget
import dev.omico.kraft.core.codegen.generateKraftClientSources
import dev.omico.kraft.core.codegen.generateKraftServerSources
import dev.omico.kraft.core.codegen.generateKraftSharedSources
import dev.omico.kraft.core.parser.parseOpenApiDocument
import dev.omico.kraft.gradle.KraftExtension
import dev.omico.kraft.gradle.internal.clearDirectory
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register

@CacheableTask
public abstract class KraftSourcesGenerator : KraftTask() {
    @get:[InputFile PathSensitive(PathSensitivity.NONE)]
    internal abstract val document: RegularFileProperty

    @get:Input
    internal abstract val sharedPackageName: Property<String>

    @get:Input
    internal abstract val clientPackageName: Property<String>

    @get:Input
    internal abstract val serverPackageName: Property<String>

    @get:Input
    internal abstract val generationTargets: SetProperty<KraftGenerationTarget>

    @get:OutputDirectory
    internal abstract val outputDirectory: DirectoryProperty

    @TaskAction
    protected fun generate() {
        val documentFile = document.get().asFile
        val sharedPackageName = sharedPackageName.get()
        val clientPackageName = clientPackageName.get()
        val serverPackageName = serverPackageName.get()
        val generationTargets = generationTargets.get()
        val outputDirectory = outputDirectory.get().asFile.toPath()
        val result = parseOpenApiDocument(documentFile.readText())
        val document = result.document
        outputDirectory.clearDirectory()
        generationTargets.forEach { target ->
            when (target) {
                KraftGenerationTarget.CLIENT -> generateKraftClientSources(document, clientPackageName, outputDirectory)
                KraftGenerationTarget.SERVER -> generateKraftSharedSources(document, serverPackageName, outputDirectory)
                KraftGenerationTarget.SHARED -> generateKraftServerSources(document, sharedPackageName, outputDirectory)
                else -> return@forEach
            }
        }
    }

    public companion object {
        public const val NAME: String = "kraftGenerateSources"
    }
}

internal fun Project.registerKraftSourcesGeneratorTask(
    kraftExtension: KraftExtension,
): TaskProvider<KraftSourcesGenerator> =
    tasks.register<KraftSourcesGenerator>(KraftSourcesGenerator.NAME) {
        document.set(kraftExtension.document)
        clientPackageName.set(kraftExtension.clientPackageName)
        serverPackageName.set(kraftExtension.serverPackageName)
        sharedPackageName.set(kraftExtension.sharedPackageName)
        generationTargets.set(kraftExtension.generationTargets)
        outputDirectory.set(kraftExtension.outputDirectory)
    }
