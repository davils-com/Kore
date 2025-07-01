package com.davils.kore.delay

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
