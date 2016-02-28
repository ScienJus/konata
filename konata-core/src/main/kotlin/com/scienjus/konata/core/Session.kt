package com.scienjus.konata.core

import javax.servlet.http.HttpSession

/**
 * @author ScienJus
 * @date 16/2/21.
 */
class Session(private val session: HttpSession) {

    val id by lazy {
        session.id
    }

    fun get(name: String): Any {
        return session.getAttribute(name)
    }

    fun remove(name: String): Any {
        val obj = get(name)
        session.removeAttribute(name)
        return obj
    }

    fun invalidate() {
        session.invalidate()
    }
}