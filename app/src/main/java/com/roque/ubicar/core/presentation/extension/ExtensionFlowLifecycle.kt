package com.roque.ubicar.core.presentation.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
inline fun <reified T> Flow<T>.collectWithLifeCycle(
    key: Any = Unit,
    lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline action: suspend (value: T) -> Unit
) {
    val lifeCycleAwareFlow = remember(this, lifeCycleOwner) {
        flowWithLifecycle(
            lifecycle = lifeCycleOwner.lifecycle,
            minActiveState = minActiveState
        )
    }

    LaunchedEffect(key) {
        lifeCycleAwareFlow.collect { action(it) }
    }
}