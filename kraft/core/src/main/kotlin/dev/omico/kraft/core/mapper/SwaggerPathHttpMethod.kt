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

internal typealias SwaggerPathHttpMethod = io.swagger.v3.oas.models.PathItem.HttpMethod

internal fun SwaggerPathHttpMethod.toKraftOpenApiHttpMethod(): KraftOpenApiHttpMethod =
    when (this) {
        SwaggerPathHttpMethod.GET -> KraftOpenApiHttpMethod.GET
        SwaggerPathHttpMethod.PUT -> KraftOpenApiHttpMethod.PUT
        SwaggerPathHttpMethod.POST -> KraftOpenApiHttpMethod.POST
        SwaggerPathHttpMethod.DELETE -> KraftOpenApiHttpMethod.DELETE
        SwaggerPathHttpMethod.OPTIONS -> KraftOpenApiHttpMethod.OPTIONS
        SwaggerPathHttpMethod.HEAD -> KraftOpenApiHttpMethod.HEAD
        SwaggerPathHttpMethod.PATCH -> KraftOpenApiHttpMethod.PATCH
        SwaggerPathHttpMethod.TRACE -> KraftOpenApiHttpMethod.TRACE
    }
