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
package dev.omico.kraft.core.mapper

import dev.omico.kraft.core.model.KraftOpenApiJsonSchema
import dev.omico.kraft.core.model.KraftOpenApiSchema
import dev.omico.kraft.core.model.KraftOpenApiSchemaFormat
import dev.omico.kraft.core.model.KraftOpenApiSchemaType

internal typealias SwaggerSchema = io.swagger.v3.oas.models.media.Schema<*>

internal typealias SwaggerJsonSchema = io.swagger.v3.oas.models.media.JsonSchema

internal fun SwaggerSchema.toKraftOpenApiSchema(name: String): KraftOpenApiSchema =
    when (this) {
        is SwaggerJsonSchema -> toKraftOpenApiJsonSchema(name)
        else -> error("Unsupported schema type: ${this::class.qualifiedName}")
    }

// TODO
internal fun SwaggerSchema.toKraftOpenApiSchema(schemas: SwaggerSchemas, name: String): KraftOpenApiSchema {
    println("name = $name, schema = $this")
    return when (this) {
        is SwaggerJsonSchema -> toKraftOpenApiJsonSchema(schemas, name)
        else -> error("Unsupported schema type: ${this::class.qualifiedName}")
    }
}

private fun SwaggerJsonSchema.toKraftOpenApiJsonSchema(
    schemas: SwaggerSchemas,
    name: String,
): KraftOpenApiJsonSchema {
    val properties = properties?.toKraftOpenApiSchemas().orEmpty()
    val required = required?.toSet().orEmpty()
    if (`$ref` != null) {
        val refSchema = schemas.resolveRef(`$ref`).toKraftOpenApiSchema(schemas, name) as KraftOpenApiJsonSchema
        refSchema.copy(
            format = refSchema.format ?: KraftOpenApiSchemaFormat.find(format),
            properties = refSchema.properties + properties,
            required = refSchema.required + required,
        )
    }
    return toKraftOpenApiJsonSchema(name)
}

private fun SwaggerJsonSchema.toKraftOpenApiJsonSchema(name: String): KraftOpenApiJsonSchema =
    KraftOpenApiJsonSchema(
        name = name,
        type = resolveType(),
        format = KraftOpenApiSchemaFormat.find(format),
        description = description,
        properties = properties?.toKraftOpenApiSchemas().orEmpty(),
        required = required?.toSet().orEmpty(),
    )

private fun SwaggerJsonSchema.resolveType(): KraftOpenApiSchemaType {
    val type = types?.firstOrNull() ?: error("Type should not be null in $this")
    return KraftOpenApiSchemaType.find(type) ?: error("Unsupported type: $type")
}

internal fun SwaggerSchemas.resolveRef(ref: String): SwaggerSchema =
    this[ref.removePrefix(SWAGGER_SCHEMA_REF_PREFIX)] ?: error("Schema not found: $ref")

private const val SWAGGER_SCHEMA_REF_PREFIX: String = "#/components/schemas/"
