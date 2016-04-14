package com.scienjus.konata.core

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * @author ScienJus
 * @date 16/4/14.
 */

fun <T> shouldEqual(expected: T, actual: T): Unit {
    assertEquals(expected, actual)
}

fun <T> shouldNotEqual(expected: T, actual: T): Unit {
    assertFalse(null) { expected == actual }
}

fun <T> shouldBeNull(actual: T?): Unit {
    assertNull(actual)
}

fun <T> shouldNotBeNull(actual: T?): Unit {
    assertNotNull(actual as Any, "")
}