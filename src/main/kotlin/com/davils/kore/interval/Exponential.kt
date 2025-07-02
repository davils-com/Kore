package com.davils.kore.interval

import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * An exponential delay strategy that increases the delay exponentially with each attempt.
 *
 * @param initialDelay The delay for the first attempt
 * @param factor The base factor for the exponential calculation (default is 2.0)
 * @param maxDelay The maximum delay to return (optional)
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
 * Creates an exponential delay strategy.
 *
 * @param initialDelayMillis The delay for the first attempt in milliseconds
 * @param maxDelayMillis The maximum delay in milliseconds (default is 5 minutes)
 * @return The delay in milliseconds for the given attempt
 */
public fun exponentialDelay(initialDelayMillis: Long, maxDelayMillis: Long = 5 * 60 * 1000L): ExponentialDelay {
    val delayStrategy = ExponentialDelay(
        initialDelayMillis.milliseconds,
        2.0,
        maxDelayMillis.milliseconds
    )
    return delayStrategy
}

/**
 * Executes a function with exponential backoff retries.
 *
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param initialDelayMillis The delay for the first retry in milliseconds
 * @param maxDelayMillis The maximum delay in milliseconds
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @param block The function to execute
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
 */
public fun <T> exponentialRetry(
    maxAttempts: Int,
    initialDelayMillis: Long,
    maxDelayMillis: Long = 5 * 60 * 1000L,
    retryableExceptions: Set<Class<out Throwable>> = emptySet(),
    block: () -> T
): T {
    val delayStrategy = exponentialDelay(initialDelayMillis, maxDelayMillis)
    val retryPolicy = DefaultRetryPolicy(maxAttempts, delayStrategy, retryableExceptions)
    return withRetry(retryPolicy, block)
}

/**
 * Executes a suspending function with exponential backoff retries.
 *
 * @param initialDelayMillis The delay for the first retry in milliseconds
 * @param maxDelayMillis The maximum delay in milliseconds
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @param block The suspending function to execute
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
 */
public suspend fun <T> exponentialRetrySuspend(
    maxAttempts: Int,
    initialDelayMillis: Long,
    maxDelayMillis: Long = 5 * 60 * 1000L,
    retryableExceptions: Set<Class<out Throwable>> = emptySet(),
    block: suspend () -> T
): T {
    val delayStrategy = exponentialDelay(initialDelayMillis, maxDelayMillis)
    val retryPolicy = DefaultRetryPolicy(maxAttempts, delayStrategy, retryableExceptions)
    return withRetrySuspend(retryPolicy, block)
}
