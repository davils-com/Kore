package com.davils.kore.delay

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