package com.example.musicapp.utils

import android.annotation.SuppressLint
import kotlin.coroutines.cancellation.CancellationException


suspend fun <T> runSuspendCatching(
    block: suspend () -> T,
): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

@SuppressLint("DefaultLocale")
fun formatTime(timeInMillis: Long): String {
    val safeTime = timeInMillis.coerceAtLeast(0)
    val minutes = (safeTime / 1000) / 60
    val seconds = (safeTime / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}
