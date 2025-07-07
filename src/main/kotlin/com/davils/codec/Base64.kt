package com.davils.codec

import java.util.Base64

/**
 * Encodes a String to Base64.
 * 
 * This extension function converts a String to its Base64 encoded representation.
 * The input string is first converted to a byte array using the default charset,
 * then encoded to Base64.
 * 
 * Usage example:
 * ```kotlin
 * val encoded = "Hello, World!".encodeBase64()
 * // Result: "SGVsbG8sIFdvcmxkIQ=="
 * ```
 *
 * @return The Base64 encoded string
 * @since 0.1.0
 */
public fun String.encodeBase64(): String {
    return Base64.getEncoder().encodeToString(this.toByteArray())
}

/**
 * Decodes a Base64 encoded String.
 * 
 * This extension function converts a Base64 encoded String back to its original form.
 * The input string is decoded from Base64, then converted to a String using the default charset.
 * 
 * Usage example:
 * ```kotlin
 * val decoded = "SGVsbG8sIFdvcmxkIQ==".decodeBase64()
 * // Result: "Hello, World!"
 * ```
 *
 * @return The decoded string
 * @since 0.1.0
 */
public fun String.decodeBase64(): String {
    return String(Base64.getDecoder().decode(this))
}

/**
 * Encodes a ByteArray to Base64.
 * 
 * This extension function converts a ByteArray to its Base64 encoded string representation.
 * 
 * Usage example:
 * ```kotlin
 * val bytes = "Hello, World!".toByteArray()
 * val encoded = bytes.encodeBase64()
 * // Result: "SGVsbG8sIFdvcmxkIQ=="
 * ```
 *
 * @return The Base64 encoded string
 * @since 0.1.0
 */
public fun ByteArray.encodeBase64(): String {
    return Base64.getEncoder().encodeToString(this)
}

/**
 * Decodes a Base64 encoded ByteArray.
 * 
 * This extension function converts a Base64 encoded ByteArray back to its original form.
 * 
 * Usage example:
 * ```kotlin
 * val encoded = "SGVsbG8sIFdvcmxkIQ==".toByteArray()
 * val decoded = encoded.decodeBase64()
 * // Result: ByteArray representing "Hello, World!"
 * ```
 *
 * @return The decoded ByteArray
 * @since 0.1.0
 */
public fun ByteArray.decodeBase64(): ByteArray {
    return Base64.getDecoder().decode(this)
}

/**
 * Property that returns the Base64 encoded representation of a String.
 * 
 * This is a convenience property that calls encodeBase64() internally.
 * 
 * Usage example:
 * ```kotlin
 * val encoded = "Hello, World!".base64Encoded
 * // Result: "SGVsbG8sIFdvcmxkIQ=="
 * ```
 *
 * @since 0.1.0
 */
public val String.base64Encoded: String
    get() = encodeBase64()

/**
 * Property that returns the decoded representation of a Base64 encoded String.
 * 
 * This is a convenience property that calls decodeBase64() internally.
 * 
 * Usage example:
 * ```kotlin
 * val decoded = "SGVsbG8sIFdvcmxkIQ==".base64Decoded
 * // Result: "Hello, World!"
 * ```
 *
 * @since 0.1.0
 */
public val String.base64Decoded: String
    get() = decodeBase64()

/**
 * Property that returns the Base64 encoded representation of a ByteArray.
 * 
 * This is a convenience property that calls encodeBase64() internally.
 * 
 * Usage example:
 * ```kotlin
 * val bytes = "Hello, World!".toByteArray()
 * val encoded = bytes.base64Encoded
 * // Result: "SGVsbG8sIFdvcmxkIQ=="
 * ```
 *
 * @since 0.1.0
 */
public val ByteArray.base64Encoded: String
    get() = encodeBase64()

/**
 * Property that returns the decoded representation of a Base64 encoded ByteArray.
 * 
 * This is a convenience property that calls decodeBase64() internally.
 * 
 * Usage example:
 * ```kotlin
 * val encoded = "SGVsbG8sIFdvcmxkIQ==".toByteArray()
 * val decoded = encoded.base64Decoded
 * // Result: ByteArray representing "Hello, World!"
 * ```
 *
 * @since 0.1.0
 */
public val ByteArray.base64Decoded: ByteArray
    get() = decodeBase64()

/**
 * Encodes a String to Base64.
 * 
 * This function is a non-extension alternative to the String.encodeBase64() extension function.
 * 
 * Usage example:
 * ```kotlin
 * val encoded = encodeToBase64("Hello, World!")
 * // Result: "SGVsbG8sIFdvcmxkIQ=="
 * ```
 *
 * @param input The string to encode
 * @return The Base64 encoded string
 * @since 0.1.0
 */
public fun encodeToBase64(input: String): String {
    return input.encodeBase64()
}

/**
 * Decodes a Base64 encoded String.
 * 
 * This function is a non-extension alternative to the String.decodeBase64() extension function.
 * 
 * Usage example:
 * ```kotlin
 * val decoded = decodeFromBase64("SGVsbG8sIFdvcmxkIQ==")
 * // Result: "Hello, World!"
 * ```
 *
 * @param input The Base64 encoded string to decode
 * @return The decoded string
 * @since 0.1.0
 */
public fun decodeFromBase64(input: String): String {
    return input.decodeBase64()
}

/**
 * Encodes a ByteArray to Base64.
 * 
 * This function is a non-extension alternative to the ByteArray.encodeBase64() extension function.
 * 
 * Usage example:
 * ```kotlin
 * val bytes = "Hello, World!".toByteArray()
 * val encoded = encodeToBase64(bytes)
 * // Result: "SGVsbG8sIFdvcmxkIQ=="
 * ```
 *
 * @param input The byte array to encode
 * @return The Base64 encoded string
 * @since 0.1.0
 */
public fun encodeToBase64(input: ByteArray): String {
    return input.encodeBase64()
}

/**
 * Decodes a Base64 encoded ByteArray.
 * 
 * This function is a non-extension alternative to the ByteArray.decodeBase64() extension function.
 * 
 * Usage example:
 * ```kotlin
 * val encoded = "SGVsbG8sIFdvcmxkIQ==".toByteArray()
 * val decoded = decodeFromBase64(encoded)
 * // Result: ByteArray representing "Hello, World!"
 * ```
 *
 * @param input The Base64 encoded byte array to decode
 * @return The decoded byte array
 * @since 0.1.0
 */
public fun decodeFromBase64(input: ByteArray): ByteArray {
    return input.decodeBase64()
}
