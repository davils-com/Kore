package com.davils.kore.delay

import kotlin.math.pow
import kotlin.time.Duration

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
public fun exponentialDelay(attempt: Int, initialDelayMillis: Long, maxDelayMillis: Long = 5 * 60 * 1000L): Long {
    val exponentialFactor = 2.0.pow(attempt.toDouble() - 1).toLong()
    val delay = (initialDelayMillis * exponentialFactor).coerceAtMost(maxDelayMillis)
    return delay
}
