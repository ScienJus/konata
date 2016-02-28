package com.scienjus.konata.core

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

/**
 * @author ScienJus
 * @date 16/2/21.
 */
class Response(private val response: HttpServletResponse) {

    var encoding: String
    get() = response.characterEncoding
    set(value) {
        response.characterEncoding = value
    }

    private val _cookies: MutableMap<String, Cookie> = mutableMapOf()
    val cookies: List<Cookie> get() = _cookies.values.toList()

    private val _headers: MutableMap<String, String> = mutableMapOf()
    val headers: Map<String, String> get() = _headers

    var status: Int = 200

    var contentType: String = "*/*"

    fun addCookie(name: String, value: String): Response {
        _cookies[name] = Cookie(name, value)
        return this
    }

    fun removeCookie(name: String) {
        _cookies.remove(name)
    }

    fun getHeader(name: String): String? {
        return headers[name]
    }

    fun addHeader(name: String, value: String): Response {
        _headers[name] = value
        return this
    }

    fun status(status: Int): Response {
        this.status = status
        return this
    }

    fun contentType(contentType: String): Response {
        this.contentType = contentType
        return this
    }

    fun ok(): Response {
        return status(200)
    }

    fun html(html: String) {
        contentType("text/html").ok().send(html)
    }

    fun json(json: String) {
        contentType("application/json").ok().send(json)
    }

    fun send(content: String) {
        cookies.forEach { response.addCookie(it) }
        headers.forEach { response.addHeader(it.key, it.value)  }
        response.status = status
        response.contentType = contentType
        response.writer.write(content)
        response.flushBuffer()
    }

}