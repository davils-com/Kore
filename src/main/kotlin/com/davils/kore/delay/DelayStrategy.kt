package com.davils.kore.delay

import kotlinx.coroutines.delay
import kotlin.time.Duration

/**
 * Interface for delay strategies that can be used for retry mechanisms, rate limiting, etc.
 */
public interface DelayStrategy {
    /**
     * Calculate the delay for a given attempt.
     *
     * @param attempt The current attempt number (1-based)
     * @return The delay duration for this attempt
     */
    public fun calculateDelay(attempt: Int): Duration
}

/**
 * Suspends the current coroutine for the duration calculated by this delay strategy.
 *
 * @param attempt The current attempt number (1-based)
 */
public suspend fun DelayStrategy.delay(attempt: Int) {
    delay(calculateDelay(attempt).inWholeMilliseconds)
}