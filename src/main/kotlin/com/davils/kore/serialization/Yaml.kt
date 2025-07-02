package com.davils.kore.serialization

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration

public val defaultYaml: Yaml = Yaml(
    configuration = YamlConfiguration(
        strictMode = true,
        encodeDefaults = true
    )
)