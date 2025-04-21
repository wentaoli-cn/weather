package com.iloatnew.weather.ui.util

import android.os.SystemClock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong

object ClickUtil {
    private const val DEFAULT_INTERVAL = 500L

    private val lastClickTime = AtomicLong(0L)

    fun throttle(
        interval: Long = DEFAULT_INTERVAL,
        action: () -> Unit,
    ): () -> Unit {
        return {
            val currentTime = SystemClock.elapsedRealtime()
            if (currentTime - lastClickTime.get() > interval) {
                lastClickTime.set(currentTime)
                action()
            }
        }
    }

    fun throttle(
        interval: Long = DEFAULT_INTERVAL,
        scope: CoroutineScope,
        action: suspend () -> Unit,
    ): () -> Unit {
        return {
            val currentTime = SystemClock.elapsedRealtime()
            if (currentTime - lastClickTime.get() > interval) {
                lastClickTime.set(currentTime)
                scope.launch {
                    action()
                }
            }
        }
    }
}
