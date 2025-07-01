package com.davils.kore.interval

import kotlinx.coroutines.delay
import kotlin.time.Duration

/**
 * Interface for retry policies that determine when and how to retry operations.
 */
public interface RetryPolicy {
    /**
     * Determines whether to retry an operation after a failure.
     *
     * @param attempt The current attempt number (1-based)
     * @param exception The exception that caused the failure
     * @return True if the operation should be retried, false otherwise
     */
    public fun shouldRetry(attempt: Int, exception: Throwable): Boolean

    /**
     * Gets the delay before the next retry attempt.
     *
     * @param attempt The current attempt number (1-based)
     * @return The delay duration before the next retry
     */
    public fun getDelay(attempt: Int): Duration
}

/**
 * A simple retry policy that retries a fixed number of times with a specified delay strategy.
 *
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param delayStrategy The strategy to use for calculating delays between retries
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 */
public class DefaultRetryPolicy(
    private val maxAttempts: Int,
    private val delayStrategy: DelayStrategy,
    private val retryableExceptions: Set<Class<out Throwable>> = emptySet()
) : RetryPolicy {
    init {
        require(maxAttempts > 0) { "Maximum attempts must be greater than 0" }
    }

    override fun shouldRetry(attempt: Int, exception: Throwable): Boolean {
        if (attempt >= maxAttempts) {
            return false
        }

        return retryableExceptions.isEmpty() || retryableExceptions.any { it.isInstance(exception) }
    }

    override fun getDelay(attempt: Int): Duration {
        return delayStrategy.calculateDelay(attempt)
    }
}

/**
 * Executes a function with retries according to the specified retry policy.
 *
 * @param retryPolicy The retry policy to use
 * @param block The function to execute
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
 */
public fun <T> withRetry(retryPolicy: RetryPolicy, block: () -> T): T {
    var attempt = 1

    while (true) {
        try {
            return block()
        } catch (e: Throwable) {
            if (!retryPolicy.shouldRetry(attempt, e)) {
                throw e
            }

            val delayDuration = retryPolicy.getDelay(attempt)
            Thread.sleep(delayDuration.inWholeMilliseconds)
            attempt++
        }
    }
}

/**
 * Executes a suspending function with retries according to the specified retry policy.
 *
 * @param retryPolicy The retry policy to use
 * @param block The suspending function to execute
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
 */
public suspend fun <T> withRetrySuspend(retryPolicy: RetryPolicy, block: suspend () -> T): T {
    var attempt = 1

    while (true) {
        try {
            return block()
        } catch (e: Throwable) {
            if (!retryPolicy.shouldRetry(attempt, e)) {
                throw e
            }

            val delayDuration = retryPolicy.getDelay(attempt)
            delay(delayDuration.inWholeMilliseconds)
            attempt++
        }
    }
}
