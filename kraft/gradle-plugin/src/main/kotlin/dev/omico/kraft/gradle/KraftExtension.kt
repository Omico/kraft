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

import dev.omico.kraft.core.codegen.KraftGenerationTarget
import dev.omico.kraft.gradle.internal.defaultOutputSourceDirectory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.setProperty
import javax.inject.Inject

public abstract class KraftExtension @Inject internal constructor(
    objects: ObjectFactory,
    layout: ProjectLayout,
) {
    public abstract val document: RegularFileProperty
    public abstract val packageName: Property<String>
    public val clientPackageName: Property<String> =
        objects.property<String>().convention(packageName.map { packageName -> "$packageName.client" })
    public val serverPackageName: Property<String> =
        objects.property<String>().convention(packageName.map { packageName -> "$packageName.server" })
    public val sharedPackageName: Property<String> =
        objects.property<String>().convention(packageName.map { packageName -> "$packageName.shared" })
    public val generationTargets: SetProperty<KraftGenerationTarget> =
        objects.setProperty<KraftGenerationTarget>().convention(enumValues<KraftGenerationTarget>().toSet())
    public val outputDirectory: DirectoryProperty =
        objects.directoryProperty().convention(layout.defaultOutputSourceDirectory)

    public companion object {
        public const val NAME: String = "kraft"
    }
}
