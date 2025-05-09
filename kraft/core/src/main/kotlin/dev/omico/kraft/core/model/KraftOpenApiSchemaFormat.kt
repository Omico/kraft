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
package dev.omico.kraft.core.model

public enum class KraftOpenApiSchemaFormat(internal val value: String) {
    INT32("int32"),
    INT64("int64"),
    FLOAT("float"),
    DOUBLE("double"),
    TIMESTAMP("timestamp"),
    ;

    public companion object {
        public fun find(value: String?): KraftOpenApiSchemaFormat? =
            when (value) {
                null -> null
                else -> KraftOpenApiSchemaFormat.entries.firstOrNull { it.value == value }
            }
    }
}
