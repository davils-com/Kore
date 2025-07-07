package com.davils.interval

import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * An exponential delay strategy that increases the delay exponentially with each attempt.
 * 
 * This implementation of DelayStrategy calculates delays that grow exponentially
 * based on the attempt number. This is useful for implementing backoff mechanisms
 * in retry logic, rate limiting, or other scenarios where progressive delays are needed.
 * 
 * Usage example:
 * ```kotlin
 * val delay = ExponentialDelay(
 *     initialDelay = 100.milliseconds,
 *     factor = 2.0,
 *     maxDelay = 5.seconds
 * )
 * 
 * // First attempt: 100ms
 * // Second attempt: 200ms
 * // Third attempt: 400ms
 * // Fourth attempt: 800ms
 * // Fifth attempt: 1600ms
 * // Sixth attempt: 3200ms
 * // Seventh attempt: 5000ms (capped by maxDelay)
 * ```
 *
 * @param initialDelay The delay for the first attempt
 * @param factor The base factor for the exponential calculation (default is 2.0)
 * @param maxDelay The maximum delay to return (optional)
 * @since 0.1.0
 */
public class ExponentialDelay(
    private val initialDelay: Duration,
    private val factor: Double = 2.0,
    private val maxDelay: Duration? = null
) : DelayStrategy {
    override fun calculateDelay(attempt: Int): Duration {
        val multiplier = factor.pow(attempt - 1)
        val calculatedDelay = initialDelay * multiplier
        return maxDelay?.let { calculatedDelay.coerceAtMost(it) } ?: calculatedDelay
    }
}

/**
 * Creates an exponential delay strategy with common defaults.
 * 
 * This factory function creates an ExponentialDelay instance with a factor of 2.0,
 * allowing you to specify the initial and maximum delays in milliseconds rather than
 * as Duration objects. This is a convenience function for common use cases.
 * 
 * Usage example:
 * ```kotlin
 * // Create a delay strategy starting at 100ms with a max of 10 seconds
 * val delayStrategy = exponentialDelay(100, 10_000)
 * 
 * // Use with retry logic
 * val result = withRetry(DefaultRetryPolicy(3, delayStrategy)) {
 *     // Your operation here
 * }
 * ```
 *
 * @param initialDelayMillis The delay for the first attempt in milliseconds
 * @param factor The base factor for the exponential calculation (default is 2.0)
 * @param maxDelayMillis The maximum delay in milliseconds (default is 5 minutes)
 * @return An ExponentialDelay instance configured with the specified parameters
 * @since 0.1.0
 */
public fun exponentialDelay(initialDelayMillis: Long, factor: Double = 2.0, maxDelayMillis: Long = 5 * 60 * 1000L): ExponentialDelay {
    val delayStrategy = ExponentialDelay(
        initialDelayMillis.milliseconds,
        factor,
        maxDelayMillis.milliseconds
    )
    return delayStrategy
}

/**
 * Executes a function with exponential backoff retries.
 * 
 * This utility function provides a simple way to execute code with automatic retry logic
 * using exponential backoff. It will retry the operation when exceptions occur, with
 * progressively longer delays between attempts.
 * 
 * Usage example:
 * ```kotlin
 * val result = exponentialRetry(
 *     maxAttempts = 5,
 *     initialDelayMillis = 100,
 *     maxDelayMillis = 10_000,
 *     retryableExceptions = setOf(IOException::class.java)
 * ) {
 *     // Your operation that might fail
 *     api.fetchData()
 * }
 * ```
 *
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param initialDelayMillis The delay for the first retry in milliseconds
 * @param maxDelayMillis The maximum delay in milliseconds
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @param block The function to execute
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
 * @since 0.1.0
 */
public fun <T> exponentialRetry(
    maxAttempts: Int,
    initialDelayMillis: Long,
    factor: Double = 2.0,
    maxDelayMillis: Long = 5 * 60 * 1000L,
    retryableExceptions: Set<Class<out Throwable>> = emptySet(),
    block: () -> T
): T {
    val delayStrategy = exponentialDelay(initialDelayMillis, factor, maxDelayMillis)
    val retryPolicy = DefaultRetryPolicy(maxAttempts, delayStrategy, retryableExceptions)
    return withRetry(retryPolicy, block)
}

/**
 * Executes a suspending function with exponential backoff retries.
 * 
 * This utility function provides a simple way to execute suspending code with automatic retry logic
 * using exponential backoff. It will retry the operation when exceptions occur, with
 * progressively longer delays between attempts. This is the coroutine-friendly version of
 * exponentialRetry.
 * 
 * Usage example:
 * ```kotlin
 * val result = exponentialRetrySuspend(
 *     maxAttempts = 5,
 *     initialDelayMillis = 100,
 *     maxDelayMillis = 10_000,
 *     retryableExceptions = setOf(IOException::class.java)
 * ) {
 *     // Your suspending operation that might fail
 *     api.fetchDataSuspend()
 * }
 * ```
 *
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param initialDelayMillis The delay for the first retry in milliseconds
 * @param maxDelayMillis The maximum delay in milliseconds
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @param block The suspending function to execute
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
 * @since 0.1.0
 */
public suspend fun <T> exponentialRetrySuspend(
    maxAttempts: Int,
    initialDelayMillis: Long,
    factor: Double = 2.0,
    maxDelayMillis: Long = 5 * 60 * 1000L,
    retryableExceptions: Set<Class<out Throwable>> = emptySet(),
    block: suspend () -> T
): T {
    val delayStrategy = exponentialDelay(initialDelayMillis, factor, maxDelayMillis)
    val retryPolicy = DefaultRetryPolicy(maxAttempts, delayStrategy, retryableExceptions)
    return withRetrySuspend(retryPolicy, block)
}
