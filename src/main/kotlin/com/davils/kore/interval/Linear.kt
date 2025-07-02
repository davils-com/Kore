package com.davils.kore.interval

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * A linear delay strategy that increases the delay linearly with each attempt.
 *
 * @param initialDelay The delay for the first attempt
 * @param increment The amount to increase the delay by for each subsequent attempt
 * @param maxDelay The maximum delay to return (optional)
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
 * @param initialDelayMillis The delay for the first attempt in milliseconds
 * @param incrementMillis The amount to increase the delay by for each subsequent attempt in milliseconds
 * @param maxDelayMillis The maximum delay in milliseconds (optional)
 * @return A linear delay strategy
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
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param initialDelayMillis The delay for the first retry in milliseconds
 * @param incrementMillis The amount to increase the delay by for each subsequent retry in milliseconds
 * @param maxDelayMillis The maximum delay in milliseconds (optional)
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @param block The function to execute
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
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
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param initialDelayMillis The delay for the first retry in milliseconds
 * @param incrementMillis The amount to increase the delay by for each subsequent retry in milliseconds
 * @param maxDelayMillis The maximum delay in milliseconds (optional)
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @param block The suspending function to execute
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
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
