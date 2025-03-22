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
package dev.omico.kraft

import dev.omico.kraft.test.kraftTestResource
import kotlin.test.Test

class KraftPluginTests : Specification() {
    @Test
    fun test(): Unit = runTest(
        projectName = "kraft-plugin-test",
        gradleKotlinSettingsScriptContent = {
            // language=kotlin
            """
            |pluginManagement {
            |    repositories {
            |        mavenCentral()
            |        gradlePluginPortal()
            |    }
            |}
            |
            |dependencyResolutionManagement {
            |    repositories {
            |        mavenCentral()
            |    }
            |}
            """.trimMargin()
        },
        gradleKotlinBuildScriptContent = {
            val document = kraftTestResource("api.yaml").invariantSeparatorsPath
            // language=kotlin
            """
            |plugins {
            |    kotlin("multiplatform")
            |    id("dev.omico.kraft")
            |}
            |
            |kotlin {
            |    jvm()
            |}
            |
            |kraft {
            |    document = file("$document")
            |    packageName = "dev.omico.kraft.test"
            |}
            """.trimMargin()
        },
        arguments = arrayOf(":kraftGenerateSources"),
        result = {},
    )
}
