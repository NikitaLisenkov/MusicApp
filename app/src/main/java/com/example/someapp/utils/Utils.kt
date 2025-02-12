package com.example.someapp.utils

import android.annotation.SuppressLint
import kotlin.coroutines.cancellation.CancellationException


suspend fun <T> runSuspendCatching(
    block: suspend () -> T,
    onSuccess: suspend (T) -> Unit = {},
    onError: suspend (Throwable) -> Unit = {}
): T? {
    return try {
        val result = block()
        onSuccess(result)
        result
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        onError(e)
        null
    }
}

@SuppressLint("DefaultLocale")
fun formatTime(timeInMillis: Long): String {
    val safeTime = timeInMillis.coerceAtLeast(0)
    val minutes = (safeTime / 1000) / 60
    val seconds = (safeTime / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}
