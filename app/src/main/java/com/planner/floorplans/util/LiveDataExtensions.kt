package com.planner.floorplans.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeIt(owner: Fragment, callback: ((t: T?) -> Unit)) {
    this.observe(owner.viewLifecycleOwner, Observer { value -> callback(value) })
}

fun <T> LiveData<T>.observeIt(owner: AppCompatActivity, callback: ((t: T?) -> Unit)) {
    this.observe(owner, Observer { value -> callback(value) })
}