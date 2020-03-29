package com.planner.floorplans.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeIt(owner: Fragment, callback: ((t: T?) -> Unit)) {
    this.observe(owner.viewLifecycleOwner, Observer { value -> callback(value) })
}