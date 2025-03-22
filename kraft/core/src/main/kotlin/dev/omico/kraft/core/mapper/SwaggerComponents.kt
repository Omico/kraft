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

import dev.omico.kraft.core.model.KraftOpenApiComponents
import dev.omico.kraft.core.model.KraftOpenApiParameters

internal typealias SwaggerComponents = io.swagger.v3.oas.models.Components

internal fun SwaggerComponents.toKraftOpenApiComponents(): KraftOpenApiComponents =
    KraftOpenApiComponents(
//        requestBodies = requestBodies.toKraftOpenApiRequestBodies(),
//        parameters = parameters?.toKraftOpenApiParameters().orEmpty(),
//        securitySchemes = securitySchemes?.toKraftOpenApiSecuritySchemes().orEmpty(),
        requestBodies = emptySet(),
        parameters = emptySet(),
        schemas = schemas?.toKraftOpenApiSchemas().orEmpty(),
        securitySchemes = emptySet(),
    )

internal typealias SwaggerComponentsParameters = Map<String, SwaggerParameter>

internal fun SwaggerComponentsParameters.toKraftOpenApiParameters(): KraftOpenApiParameters =
    map { (_, parameter) -> parameter.toKraftOpenApiParameter(this) }.toSet()
