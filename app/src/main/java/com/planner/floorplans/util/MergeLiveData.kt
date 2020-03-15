package com.planner.floorplans.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class MergeLiveData<F, S, R>(
    private val first: LiveData<F>,
    private val second: LiveData<S>,
    merge: ((first: F?, second: S?) -> R)
) :
    MediatorLiveData<R>() {
    init {
        addSource(first) {
            this.value = merge(it, second.value)
        }

        addSource(second) {
            this.value = merge(first.value, it)
        }
    }
}