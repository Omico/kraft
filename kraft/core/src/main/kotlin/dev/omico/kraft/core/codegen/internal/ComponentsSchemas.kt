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
package dev.omico.kraft.core.codegen.internal

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import dev.omico.kraft.core.model.KraftOpenApiDocument
import dev.omico.kraft.core.model.KraftOpenApiJsonSchema
import dev.omico.kraft.core.model.KraftOpenApiSchema
import dev.omico.kraft.core.model.KraftOpenApiSchemaFormat
import dev.omico.kraft.core.model.KraftOpenApiSchemaType
import me.omico.elucidator.addClass
import me.omico.elucidator.addModifiers
import me.omico.elucidator.addParameter
import me.omico.elucidator.addProperty
import me.omico.elucidator.initializer
import me.omico.elucidator.ktFile
import me.omico.elucidator.primaryConstructor
import me.omico.elucidator.writeTo
import java.nio.file.Path

internal fun generateComponentsSchemas(
    document: KraftOpenApiDocument,
    packageName: String,
    outputDirectory: Path,
) {
    document.components?.schemas?.forEach { schema ->
        generateModelFromSchema(schema, packageName, outputDirectory)
    }
}

private fun generateModelFromSchema(
    schema: KraftOpenApiSchema,
    packageName: String,
    outputDirectory: Path,
) {
    if (schema !is KraftOpenApiJsonSchema) return
    // TODO maybe remove the below check later
    if (schema.type != KraftOpenApiSchemaType.OBJECT) return
    ktFile(packageName, schema.name) {
        addClass(schema.name) {
            addModifiers(KModifier.DATA)
            primaryConstructor {
                schema.properties.forEach { propertySchema ->
                    addParameter(propertySchema.name, propertySchema.resolveTypeName())
                }
            }
            schema.properties.forEach { propertySchema ->
                addProperty(propertySchema.name, propertySchema.resolveTypeName()) {
                    initializer(propertySchema.name)
                }
            }
        }
        writeTo(outputDirectory)
    }
}

private fun KraftOpenApiSchema.resolveTypeName(): TypeName =
    when (type) {
        KraftOpenApiSchemaType.STRING -> STRING

        KraftOpenApiSchemaType.INTEGER,
        KraftOpenApiSchemaType.NUMBER,
        -> when (format) {
            KraftOpenApiSchemaFormat.INT32 -> INT
            KraftOpenApiSchemaFormat.INT64 -> LONG
            KraftOpenApiSchemaFormat.FLOAT -> FLOAT
            KraftOpenApiSchemaFormat.DOUBLE -> DOUBLE
            else -> INT
        }

        KraftOpenApiSchemaType.BOOLEAN -> BOOLEAN

        KraftOpenApiSchemaType.ARRAY -> LIST

        KraftOpenApiSchemaType.OBJECT -> ANY

        null -> error("Type should not be null in $this")
    }
