package com.example.someapp.utils

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