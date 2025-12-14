package com.roque.ubicar.core.data.extension

import kotlinx.coroutines.CancellationException
import java.lang.Exception

inline fun <T,R> T.resultOf(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}