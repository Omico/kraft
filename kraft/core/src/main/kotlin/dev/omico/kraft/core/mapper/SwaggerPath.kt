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

import dev.omico.kraft.core.model.KraftOpenApiOperations
import dev.omico.kraft.core.model.KraftOpenApiPath

internal typealias SwaggerPath = io.swagger.v3.oas.models.PathItem

internal fun SwaggerPath.toKraftOpenApiPath(
    componentsParameters: SwaggerComponentsParameters,
    path: String,
): KraftOpenApiPath =
    KraftOpenApiPath(
        summary = summary,
        description = description,
        parameters = parameters?.toKraftOpenApiParameters(componentsParameters).orEmpty(),
        operations = collectKraftOpenApiOperations(componentsParameters, path),
    )

private fun SwaggerPath.collectKraftOpenApiOperations(
    componentsParameters: SwaggerComponentsParameters,
    path: String,
): KraftOpenApiOperations =
    buildMap {
        readOperationsMap().forEach { (httpMethod, operation) ->
            val httpMethod = httpMethod.toKraftOpenApiHttpMethod()
            val operation = operation.toKraftOpenApiOperation(
                componentsParameters = componentsParameters,
                httpMethod = httpMethod,
                path = path,
            )
            put(httpMethod, operation)
        }
    }
