package com.project.feed.util

import androidx.fragment.app.Fragment

fun isSafeFragment(fragment: Fragment): Boolean {
    return !(fragment.isRemoving || fragment.activity == null || fragment.isDetached || !fragment.isAdded || fragment.view == null)
}