package com.davils.serialization

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration

/**
 * A pre-configured YAML serializer with common settings.
 * 
 * This property provides a ready-to-use instance of com.charleskorn.kaml.Yaml
 * with sensible default settings. It's configured to operate in strict mode
 * and to encode default values, making it suitable for most common YAML serialization needs.
 * 
 * Usage example:
 * ```kotlin
 * // Serialize an object to a YAML string
 * val config = Configuration(name = "AppConfig", timeout = 30)
 * val yamlString = defaultYaml.encodeToString(Configuration.serializer(), config)
 * 
 * // Deserialize a YAML string to an object
 * val parsedConfig = defaultYaml.decodeFromString(Configuration.serializer(), yamlString)
 * ```
 *
 * @since 0.1.0
 */
public val defaultYaml: Yaml = Yaml(
    configuration = YamlConfiguration(
        strictMode = true,
        encodeDefaults = true
    )
)
