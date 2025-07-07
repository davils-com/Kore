package com.davils.serialization

import kotlinx.serialization.json.Json

/**
 * A pre-configured Json serializer with common settings.
 * 
 * This property provides a ready-to-use instance of kotlinx.serialization.json.Json
 * with sensible default settings. It's configured to produce pretty-printed output
 * and to encode default values, making it suitable for most common serialization needs.
 * 
 * Usage example:
 * ```kotlin
 * // Serialize an object to a JSON string
 * val user = User(name = "John", age = 30)
 * val jsonString = defaultJson.encodeToString(User.serializer(), user)
 * 
 * // Deserialize a JSON string to an object
 * val parsedUser = defaultJson.decodeFromString(User.serializer(), jsonString)
 * ```
 *
 * @since 0.1.0
 */
public val defaultJson: Json = Json {
    prettyPrint = true
    encodeDefaults = true
}
