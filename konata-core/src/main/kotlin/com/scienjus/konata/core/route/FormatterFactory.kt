package com.scienjus.konata.core.route

import kotlin.reflect.KClass

/**
 * @author ScienJus
 * @date 16/4/9.
 */
object FormatterFactory {

    private val formatters: MutableMap<KClass<*>, (String) -> Any>

    init {
        formatters = mutableMapOf(
                Int::class      to { string -> string.toInt() },
                String::class   to { string -> string }
        )
    }

    fun<T : Any> getFormatter(kClass: KClass<T>): ((String) -> Any)? {
        return formatters[kClass]
    }

    fun <T : Any> addFormatter(kClass: KClass<T>, formatter: (String) -> T) {
        formatters[kClass] = formatter
    }

}