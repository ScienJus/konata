package com.scienjus.konata.core

/**
 * @author ScienJus
 * @date 16/2/28.
 */
val DEFAULT_PORT = Math.abs("konata".hashCode() / 940429).reverse()

fun Int.reverse(): Int {
    var input = this
    var output = 0
    while(input > 0) {
        output *= 10
        output += input % 10
        input /= 10
    }
    return output
}