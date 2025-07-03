package com.davils.kore.interval

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * A fixed delay strategy that always returns the same delay.
 * 
 * This implementation of DelayStrategy provides a constant delay regardless of
 * the attempt number. This is useful for implementing simple retry mechanisms
 * where a consistent delay between attempts is desired.
 * 
 * Usage example:
 * ```kotlin
 * val delay = FixedDelay(500.milliseconds)
 * 
 * // First attempt: 500ms
 * // Second attempt: 500ms
 * // Third attempt: 500ms
 * // All attempts use the same delay
 * ```
 *
 * @param delay The fixed delay to use for all attempts
 * @since 0.1.0
 */
public class FixedDelay(private val delay: Duration) : DelayStrategy {
    override fun calculateDelay(attempt: Int): Duration = delay
}

/**
 * Creates a fixed delay strategy.
 * 
 * This factory function creates a FixedDelay instance with the specified delay in milliseconds.
 * It's a convenience function that converts the milliseconds value to a Duration object.
 * 
 * Usage example:
 * ```kotlin
 * // Create a delay strategy with a 1-second delay
 * val delayStrategy = fixedDelay(1000)
 * 
 * // Use with retry logic
 * val result = withRetry(DefaultRetryPolicy(3, delayStrategy)) {
 *     // Your operation here
 * }
 * ```
 *
 * @param delayMillis The fixed delay in milliseconds
 * @return A fixed delay strategy
 * @since 0.1.0
 */
public fun fixedDelay(delayMillis: Long): DelayStrategy = FixedDelay(delayMillis.milliseconds)

/**
 * Executes a function with fixed delay retries.
 * 
 * This utility function provides a simple way to execute code with automatic retry logic
 * using a fixed delay between attempts. It will retry the operation when exceptions occur,
 * with the same delay between each attempt.
 * 
 * Usage example:
 * ```kotlin
 * val result = fixedRetry(
 *     maxAttempts = 3,
 *     delayMillis = 1000,
 *     retryableExceptions = setOf(IOException::class.java)
 * ) {
 *     // Your operation that might fail
 *     api.fetchData()
 * }
 * ```
 *
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param delayMillis The fixed delay between retries in milliseconds
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @param block The function to execute
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
 * @since 0.1.0
 */
public fun <T> fixedRetry(
    maxAttempts: Int,
    delayMillis: Long,
    retryableExceptions: Set<Class<out Throwable>> = emptySet(),
    block: () -> T
): T {
    val delayStrategy = fixedDelay(delayMillis)
    val retryPolicy = DefaultRetryPolicy(maxAttempts, delayStrategy, retryableExceptions)
    return withRetry(retryPolicy, block)
}

/**
 * Executes a suspending function with fixed delay retries.
 * 
 * This utility function provides a simple way to execute suspending code with automatic retry logic
 * using a fixed delay between attempts. It will retry the operation when exceptions occur,
 * with the same delay between each attempt. This is the coroutine-friendly version of
 * fixedRetry.
 * 
 * Usage example:
 * ```kotlin
 * val result = fixedRetrySuspend(
 *     maxAttempts = 3,
 *     delayMillis = 1000,
 *     retryableExceptions = setOf(IOException::class.java)
 * ) {
 *     // Your suspending operation that might fail
 *     api.fetchDataSuspend()
 * }
 * ```
 *
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param delayMillis The fixed delay between retries in milliseconds
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @param block The suspending function to execute
 *
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
 * @since 0.1.0
 */
public suspend fun <T> fixedRetrySuspend(
    maxAttempts: Int,
    delayMillis: Long,
    retryableExceptions: Set<Class<out Throwable>> = emptySet(),
    block: suspend () -> T
): T {
    val delayStrategy = fixedDelay(delayMillis)
    val retryPolicy = DefaultRetryPolicy(maxAttempts, delayStrategy, retryableExceptions)
    return withRetrySuspend(retryPolicy, block)
}
