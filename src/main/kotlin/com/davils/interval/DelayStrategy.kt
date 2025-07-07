package com.davils.interval

import kotlinx.coroutines.delay
import kotlin.time.Duration

/**
 * Interface for delay strategies that can be used for retry mechanisms, rate limiting, etc.
 * 
 * This interface defines the contract for delay strategies, which calculate appropriate
 * waiting times between operations. It can be used for implementing various patterns like
 * retry with backoff, rate limiting, or any scenario where controlled delays are needed.
 * Different implementations can provide fixed, linear, or exponential delay patterns.
 * 
 * Usage example:
 * ```kotlin
 * // Create a custom delay strategy
 * class CustomDelay : DelayStrategy {
 *     override fun calculateDelay(attempt: Int): Duration {
 *         // Custom logic to calculate delay
 *         return (attempt * 100).milliseconds
 *     }
 * }
 * 
 * // Use the strategy
 * val delayStrategy = CustomDelay()
 * val delay = delayStrategy.calculateDelay(3) // Get delay for 3rd attempt
 * ```
 *
 * @since 0.1.0
 */
public interface DelayStrategy {
    /**
     * Calculate the delay for a given attempt.
     * 
     * This method determines how long to wait before a specific attempt. Implementations
     * can use different strategies to calculate this delay, such as fixed intervals,
     * linear growth, or exponential backoff.
     * 
     * Usage example:
     * ```kotlin
     * // Inside a DelayStrategy implementation
     * override fun calculateDelay(attempt: Int): Duration {
     *     // Exponential backoff: 100ms, 200ms, 400ms, 800ms, etc.
     *     return (100 * 2.0.pow(attempt - 1)).toLong().milliseconds
     * }
     * ```
     *
     * @param attempt The current attempt number (1-based)
     * @return The delay duration for this attempt
     * @since 0.1.0
     */
    public fun calculateDelay(attempt: Int): Duration
}

/**
 * Suspends the current coroutine for the duration calculated by this delay strategy.
 * 
 * This extension function provides a convenient way to apply a delay strategy directly
 * in coroutine contexts. It calculates the appropriate delay using the strategy and then
 * suspends the current coroutine for that duration. This is particularly useful in retry
 * loops and other scenarios where controlled waiting is needed.
 * 
 * Usage example:
 * ```kotlin
 * // Create a delay strategy
 * val strategy = ExponentialDelay(100.milliseconds, 2.0)
 * 
 * // Use in a coroutine
 * suspend fun performWithRetry() {
 *     for (attempt in 1..3) {
 *         try {
 *             return performOperation()
 *         } catch (e: Exception) {
 *             if (attempt == 3) throw e
 *             strategy.delay(attempt) // Will suspend for increasing durations
 *         }
 *     }
 * }
 * ```
 *
 * @param attempt The current attempt number (1-based)
 * @since 0.1.0
 */
public suspend fun DelayStrategy.delay(attempt: Int) {
    delay(calculateDelay(attempt).inWholeMilliseconds)
}
