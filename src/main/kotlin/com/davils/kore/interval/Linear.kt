package com.davils.kore.interval

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * A linear delay strategy that increases the delay linearly with each attempt.
 * 
 * This implementation of DelayStrategy calculates delays that grow linearly
 * based on the attempt number. This provides a middle ground between fixed delays
 * and exponential delays, offering a predictable but increasing delay pattern.
 * 
 * Usage example:
 * ```kotlin
 * val delay = LinearDelay(
 *     initialDelay = 100.milliseconds,
 *     increment = 200.milliseconds,
 *     maxDelay = 1.seconds
 * )
 * 
 * // First attempt: 100ms
 * // Second attempt: 300ms (100ms + 200ms)
 * // Third attempt: 500ms (100ms + 2*200ms)
 * // Fourth attempt: 700ms (100ms + 3*200ms)
 * // Fifth attempt: 900ms (100ms + 4*200ms)
 * // Sixth attempt: 1000ms (capped by maxDelay)
 * ```
 *
 * @param initialDelay The delay for the first attempt
 * @param increment The amount to increase the delay by for each subsequent attempt
 * @param maxDelay The maximum delay to return (optional)
 * @since 0.1.0
 */
public class LinearDelay(
    private val initialDelay: Duration,
    private val increment: Duration,
    private val maxDelay: Duration? = null
) : DelayStrategy {
    override fun calculateDelay(attempt: Int): Duration {
        val calculatedDelay = initialDelay + (increment * (attempt - 1))
        return maxDelay?.let { calculatedDelay.coerceAtMost(it) } ?: calculatedDelay
    }
}

/**
 * Creates a linear delay strategy.
 * 
 * This factory function creates a LinearDelay instance with the specified parameters in milliseconds.
 * It's a convenience function that converts the milliseconds values to Duration objects.
 * 
 * Usage example:
 * ```kotlin
 * // Create a delay strategy starting at 100ms, increasing by 200ms each attempt, with a max of 1 second
 * val delayStrategy = linearDelay(100, 200, 1000)
 * 
 * // Use with retry logic
 * val result = withRetry(DefaultRetryPolicy(3, delayStrategy)) {
 *     // Your operation here
 * }
 * ```
 *
 * @param initialDelayMillis The delay for the first attempt in milliseconds
 * @param incrementMillis The amount to increase the delay by for each subsequent attempt in milliseconds
 * @param maxDelayMillis The maximum delay in milliseconds (optional)
 *
 * @return A linear delay strategy
 * @since 0.1.0
 */
public fun linearDelay(
    initialDelayMillis: Long,
    incrementMillis: Long,
    maxDelayMillis: Long? = null
): DelayStrategy = LinearDelay(
    initialDelayMillis.milliseconds,
    incrementMillis.milliseconds,
    maxDelayMillis?.milliseconds
)

/**
 * Executes a function with linear backoff retries.
 * 
 * This utility function provides a simple way to execute code with automatic retry logic
 * using linear backoff. It will retry the operation when exceptions occur, with
 * progressively longer delays between attempts that increase by a fixed increment.
 * 
 * Usage example:
 * ```kotlin
 * val result = linearRetry(
 *     maxAttempts = 5,
 *     initialDelayMillis = 100,
 *     incrementMillis = 200,
 *     maxDelayMillis = 1000,
 *     retryableExceptions = setOf(IOException::class.java)
 * ) {
 *     // Your operation that might fail
 *     api.fetchData()
 * }
 * ```
 *
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param initialDelayMillis The delay for the first retry in milliseconds
 * @param incrementMillis The amount to increase the delay by for each subsequent retry in milliseconds
 * @param maxDelayMillis The maximum delay in milliseconds (optional)
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @param block The function to execute
 *
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
 * @since 0.1.0
 */
public fun <T> linearRetry(
    maxAttempts: Int,
    initialDelayMillis: Long,
    incrementMillis: Long,
    maxDelayMillis: Long? = null,
    retryableExceptions: Set<Class<out Throwable>> = emptySet(),
    block: () -> T
): T {
    val delayStrategy = linearDelay(initialDelayMillis, incrementMillis, maxDelayMillis)
    val retryPolicy = DefaultRetryPolicy(maxAttempts, delayStrategy, retryableExceptions)
    return withRetry(retryPolicy, block)
}

/**
 * Executes a suspending function with linear backoff retries.
 * 
 * This utility function provides a simple way to execute suspending code with automatic retry logic
 * using linear backoff. It will retry the operation when exceptions occur, with
 * progressively longer delays between attempts that increase by a fixed increment.
 * This is the coroutine-friendly version of linearRetry.
 * 
 * Usage example:
 * ```kotlin
 * val result = linearRetrySuspend(
 *     maxAttempts = 5,
 *     initialDelayMillis = 100,
 *     incrementMillis = 200,
 *     maxDelayMillis = 1000,
 *     retryableExceptions = setOf(IOException::class.java)
 * ) {
 *     // Your suspending operation that might fail
 *     api.fetchDataSuspend()
 * }
 * ```
 *
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param initialDelayMillis The delay for the first retry in milliseconds
 * @param incrementMillis The amount to increase the delay by for each subsequent retry in milliseconds
 * @param maxDelayMillis The maximum delay in milliseconds (optional)
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @param block The suspending function to execute
 *
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
 * @since 0.1.0
 */
public suspend fun <T> linearRetrySuspend(
    maxAttempts: Int,
    initialDelayMillis: Long,
    incrementMillis: Long,
    maxDelayMillis: Long? = null,
    retryableExceptions: Set<Class<out Throwable>> = emptySet(),
    block: suspend () -> T
): T {
    val delayStrategy = linearDelay(initialDelayMillis, incrementMillis, maxDelayMillis)
    val retryPolicy = DefaultRetryPolicy(maxAttempts, delayStrategy, retryableExceptions)
    return withRetrySuspend(retryPolicy, block)
}
