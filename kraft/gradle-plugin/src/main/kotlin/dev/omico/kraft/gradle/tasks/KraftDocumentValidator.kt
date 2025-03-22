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

import dev.omico.kraft.core.parser.parseOpenApiDocument
import dev.omico.kraft.gradle.KraftExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register

@CacheableTask
public abstract class KraftDocumentValidator : KraftTask() {
    @get:[InputFile PathSensitive(PathSensitivity.NONE)]
    internal abstract val document: RegularFileProperty

    @TaskAction
    protected fun validate() {
        val document = document.get().asFile
        val result = parseOpenApiDocument(document.readText())
        result.messages.forEach { message -> logger.error(message) }
        if (result.messages.isNotEmpty()) throw GradleException("Document validation failed.")
    }

    public companion object {
        public const val NAME: String = "kraftValidateDocument"
    }
}

internal fun Project.registerKraftDocumentValidatorTask(
    kraftExtension: KraftExtension,
): TaskProvider<KraftDocumentValidator> =
    tasks.register<KraftDocumentValidator>(KraftDocumentValidator.NAME) {
        document.set(kraftExtension.document)
    }
