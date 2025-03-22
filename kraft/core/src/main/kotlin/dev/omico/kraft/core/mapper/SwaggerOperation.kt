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

import dev.omico.kraft.core.model.KraftOpenApiHttpMethod
import dev.omico.kraft.core.model.KraftOpenApiOperation
import io.swagger.v3.oas.models.Operation

internal typealias SwaggerOperation = Operation

internal fun SwaggerOperation.toKraftOpenApiOperation(
    componentsParameters: SwaggerComponentsParameters,
    httpMethod: KraftOpenApiHttpMethod,
    path: String,
): KraftOpenApiOperation =
    KraftOpenApiOperation(
        id = requireOperationId(httpMethod, path),
        summary = summary,
        description = description,
        parameters = parameters?.toKraftOpenApiParameters(componentsParameters).orEmpty(),
        requestBody = requestBody?.toKraftOpenApiRequestBody(),
        responses = responses?.toKraftOpenApiResponses().orEmpty(),
    )

private fun SwaggerOperation.requireOperationId(httpMethod: KraftOpenApiHttpMethod, path: String): String =
    requireNotNull(operationId) {
        "An Operation ID is required for `$httpMethod $path`, " +
            "see also https://swagger.io/docs/specification/v3_0/paths-and-operations"
    }
