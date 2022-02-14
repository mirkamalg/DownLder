package com.mirkamalg.domain.utils

/**
 * Created by Mirkamal Gasimov on 14.02.2022.
 */

inline fun <T> applyToAll(vararg objects: T, block: T.(Int) -> Unit) {
    objects.forEachIndexed { index, obj ->
        obj.block(index)
    }
}