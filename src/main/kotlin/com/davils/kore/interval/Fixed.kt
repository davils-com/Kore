package com.davils.kore.interval

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * A fixed delay strategy that always returns the same delay.
 *
 * @param delay The fixed delay to use
 */
public class FixedDelay(private val delay: Duration) : DelayStrategy {
    override fun calculateDelay(attempt: Int): Duration = delay
}

/**
 * Creates a fixed delay strategy.
 *
 * @param delayMillis The fixed delay in milliseconds
 * @return A fixed delay strategy
 */
public fun fixedDelay(delayMillis: Long): DelayStrategy = FixedDelay(delayMillis.milliseconds)

/**
 * Executes a function with fixed delay retries.
 *
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param delayMillis The fixed delay between retries in milliseconds
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @param block The function to execute
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
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
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param delayMillis The fixed delay between retries in milliseconds
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @param block The suspending function to execute
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
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
