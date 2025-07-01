package com.davils.kore.serialization

import kotlinx.serialization.json.Json

public val defaultJson: Json = Json {
    prettyPrint = true
    encodeDefaults = true
}