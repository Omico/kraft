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

import dev.omico.kraft.Specification.SubmoduleCreator
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.BeforeTest

abstract class Specification {
    @TempDir lateinit var testProjectDirectory: File
    lateinit var gradleKotlinSettingsScript: File
    lateinit var gradleKotlinBuildScript: File

    @BeforeTest
    protected fun setup() {
        gradleKotlinSettingsScript = testProjectDirectory.resolve(GRADLE_KOTLIN_SETTINGS_SCRIPT_NAME)
        gradleKotlinBuildScript = testProjectDirectory.resolve(GRADLE_KOTLIN_BUILD_SCRIPT_NAME)
    }

    protected fun runTest(
        projectName: String,
        gradleKotlinSettingsScriptContent: () -> String = { "" },
        gradleKotlinBuildScriptContent: () -> String = { "" },
        noConfigurationCache: Boolean = true,
        vararg arguments: String = emptyArray(),
        submodules: SubmoduleCreator.() -> Unit = {},
        result: BuildResult.() -> Unit,
    ) {
        gradleKotlinSettingsScript.writeText(gradleKotlinSettingsScriptContent())
        gradleKotlinBuildScript.writeText(gradleKotlinBuildScriptContent())
        SubmoduleCreatorImpl(testProjectDirectory).submodules()
        val argumentSet = buildSet {
            addAll(arguments)
            add("--stacktrace")
            if (noConfigurationCache) add("--no-configuration-cache")
        }
        val buildResult = GradleRunner.create()
            .withProjectDir(testProjectDirectory)
            .withArguments(argumentSet.toList())
            .withPluginClasspath()
            .forwardOutput()
            .build()
        val debugProjectDirectory = File("build/test-projects/$projectName")
        if (debugProjectDirectory.exists()) debugProjectDirectory.deleteRecursively()
        testProjectDirectory.copyRecursively(debugProjectDirectory)
        result(buildResult)
    }

    interface SubmoduleCreator {
        fun submodule(
            name: String,
            buildKotlinScriptContent: () -> String = { "" },
        )
    }
}

private class SubmoduleCreatorImpl(
    private val testProjectDirectory: File,
) : SubmoduleCreator {
    override fun submodule(
        name: String,
        buildKotlinScriptContent: () -> String,
    ) {
        val submoduleDirectory = testProjectDirectory.resolve(name)
        submoduleDirectory.mkdirs()
        val buildKotlinScript = submoduleDirectory.resolve(GRADLE_KOTLIN_BUILD_SCRIPT_NAME)
        buildKotlinScript.writeText(buildKotlinScriptContent())
    }
}

private const val GRADLE_KOTLIN_BUILD_SCRIPT_NAME: String = "build.gradle.kts"
private const val GRADLE_KOTLIN_SETTINGS_SCRIPT_NAME: String = "settings.gradle.kts"
