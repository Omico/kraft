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
package dev.omico.kraft.core.parser

import dev.omico.kraft.core.mapper.toKraftOpenApiDocument
import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.parser.core.models.SwaggerParseResult

public fun parseOpenApiDocument(content: String): KraftParserResult =
    OpenAPIV3Parser().readContents(content, null, null).toKraftParserResult()

// TODO Complete the conversions
private fun SwaggerParseResult.toKraftParserResult(): KraftParserResult =
    KraftParserResult(
        document = openAPI
            //  .also(::println) // TODO Remove println later
            .toKraftOpenApiDocument(),
        messages = messages,
    )
