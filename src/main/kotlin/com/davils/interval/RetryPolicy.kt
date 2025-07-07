package com.davils.interval

import kotlinx.coroutines.delay
import kotlin.time.Duration

/**
 * Interface for retry policies that determine when and how to retry operations.
 * 
 * This interface defines the contract for retry policies, which are responsible for
 * determining whether a failed operation should be retried and how long to wait
 * before the next attempt. Implementations can provide different strategies for
 * retry behavior based on various factors such as the number of attempts,
 * the type of exception, or other contextual information.
 * 
 * Usage example:
 * ```kotlin
 * class CustomRetryPolicy : RetryPolicy {
 *     override fun shouldRetry(attempt: Int, exception: Throwable): Boolean {
 *         // Only retry network exceptions, up to 3 times
 *         return exception is NetworkException && attempt < 3
 *     }
 *     
 *     override fun getDelay(attempt: Int): Duration {
 *         // Increase delay with each attempt
 *         return (attempt * 1000).milliseconds
 *     }
 * }
 * ```
 *
 * @since 0.1.0
 */
public interface RetryPolicy {
    /**
     * Determines whether to retry an operation after a failure.
     * 
     * This method is called after an operation fails with an exception to determine
     * if another attempt should be made. Implementations can consider factors such as
     * the current attempt number, the type of exception, and any other relevant context.
     * 
     * Usage example:
     * ```kotlin
     * // Inside a retry policy implementation
     * override fun shouldRetry(attempt: Int, exception: Throwable): Boolean {
     *     // Only retry for network exceptions and up to 3 attempts
     *     return exception is IOException && attempt < 3
     * }
     * ```
     *
     * @param attempt The current attempt number (1-based)
     * @param exception The exception that caused the failure
     *
     * @return True if the operation should be retried, false otherwise
     * @since 0.1.0
     */
    public fun shouldRetry(attempt: Int, exception: Throwable): Boolean

    /**
     * Gets the delay before the next retry attempt.
     * 
     * This method calculates how long to wait before making the next retry attempt.
     * Implementations can use different strategies to determine the delay, such as
     * fixed delays, exponential backoff, or other patterns.
     * 
     * Usage example:
     * ```kotlin
     * // Inside a retry policy implementation
     * override fun getDelay(attempt: Int): Duration {
     *     // Exponential backoff: 100ms, 200ms, 400ms, 800ms, etc.
     *     return (100 * 2.0.pow(attempt - 1)).toLong().milliseconds
     * }
     * ```
     *
     * @param attempt The current attempt number (1-based)
     * @return The delay duration before the next retry
     * @since 0.1.0
     */
    public fun getDelay(attempt: Int): Duration
}

/**
 * A simple retry policy that retries a fixed number of times with a specified delay strategy.
 * 
 * This implementation of RetryPolicy provides a configurable retry mechanism that limits
 * the number of attempts and uses a DelayStrategy to determine the delay between retries.
 * It can also be configured to only retry specific types of exceptions.
 * 
 * Usage example:
 * ```kotlin
 * // Create a policy that retries up to 3 times with exponential backoff
 * val retryPolicy = DefaultRetryPolicy(
 *     maxAttempts = 3,
 *     delayStrategy = ExponentialDelay(100.milliseconds, 2.0, 5.seconds),
 *     retryableExceptions = setOf(IOException::class.java, TimeoutException::class.java)
 * )
 * 
 * // Use the policy with the withRetry function
 * val result = withRetry(retryPolicy) {
 *     // Your operation here
 * }
 * ```
 *
 * @param maxAttempts The maximum number of attempts to make (including the initial attempt)
 * @param delayStrategy The strategy to use for calculating delays between retries
 * @param retryableExceptions The set of exception types that should trigger a retry (empty means all exceptions are retryable)
 * @since 0.1.0
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
 * This utility function executes the provided function and automatically retries it
 * if it fails, according to the rules defined in the provided retry policy.
 * It handles the retry logic, including waiting between attempts.
 * 
 * Usage example:
 * ```kotlin
 * val result = withRetry(
 *     retryPolicy = DefaultRetryPolicy(
 *         maxAttempts = 3,
 *         delayStrategy = ExponentialDelay(100.milliseconds)
 *     )
 * ) {
 *     // Your operation that might fail
 *     api.fetchData()
 * }
 * ```
 *
 * @param retryPolicy The retry policy to use
 * @param block The function to execute
 *
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
 * @since 0.1.0
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
 * This utility function executes the provided suspending function and automatically retries it
 * if it fails, according to the rules defined in the provided retry policy.
 * It handles the retry logic, including waiting between attempts. This is the coroutine-friendly
 * version of withRetry.
 * 
 * Usage example:
 * ```kotlin
 * val result = withRetrySuspend(
 *     retryPolicy = DefaultRetryPolicy(
 *         maxAttempts = 3,
 *         delayStrategy = ExponentialDelay(100.milliseconds)
 *     )
 * ) {
 *     // Your suspending operation that might fail
 *     api.fetchDataSuspend()
 * }
 * ```
 *
 * @param retryPolicy The retry policy to use
 * @param block The suspending function to execute
 *
 * @return The result of the function
 * @throws Exception The last exception that occurred if all retries failed
 * @since 0.1.0
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
