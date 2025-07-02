package com.davils.kore.codec

import java.util.Base64

public fun String.encodeBase64(): String {
    return Base64.getEncoder().encodeToString(this.toByteArray())
}

public fun String.decodeBase64(): String {
    return String(Base64.getDecoder().decode(this))
}

public fun ByteArray.encodeBase64(): String {
    return Base64.getEncoder().encodeToString(this)
}

public fun ByteArray.decodeBase64(): ByteArray {
    return Base64.getDecoder().decode(this)
}

public val String.base64Encoded: String
    get() = encodeBase64()

public val String.base64Decoded: String
    get() = decodeBase64()

public val ByteArray.base64Encoded: String
    get() = encodeBase64()

public val ByteArray.base64Decoded: ByteArray
    get() = decodeBase64()

public fun encodeBase64(input: String): String {
    return input.encodeBase64()
}

public fun decodeBase64(input: String): String {
    return input.decodeBase64()
}

public fun encodeBase64(input: ByteArray): String {
    return input.encodeBase64()
}

public fun decodeBase64(input: ByteArray): ByteArray {
    return input.decodeBase64()
}
