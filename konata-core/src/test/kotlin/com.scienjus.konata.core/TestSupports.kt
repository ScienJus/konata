package com.scienjus.konata.core

import kotlin.test.*

/**
 * @author ScienJus
 * @date 16/4/14.
 */
infix fun <T> T.shouldEqual(actual: T): Unit {
    assertEquals(this, actual)
}

infix fun <T> T.shouldNotEqual(actual: T): Unit {
    assertNotEquals(this, actual)
}

fun <T> shouldBeNull(actual: T?): Unit {
    assertNull(actual)
}

fun <T> shouldNotBeNull(actual: T?): Unit {
    assertNotNull(actual as Any?)
}

infix fun <T : Collection<X>, X> T.shouldContains(actual: X): Unit {
    assertTrue(this.contains(actual))
}