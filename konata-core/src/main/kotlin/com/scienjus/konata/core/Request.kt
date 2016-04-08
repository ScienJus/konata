package com.scienjus.konata.core

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.Part

/**
 * @author ScienJus
 * @date 16/2/21.
 */
class Request(private val request: HttpServletRequest, private val pathVariables: Map<String, String>) {

    val body: String by lazy {
        request.reader.readText()
    }

    val uri: String by lazy {
        request.requestURI.toString()
    }

    val url: String by lazy {
        request.requestURL.toString()
    }

    val query: String by lazy {
        request.queryString
    }

    val contextPath: String by lazy {
        request.contextPath
    }

    val method: String by lazy {
        request.method
    }

    val session: Session by lazy {
        Session(request.getSession(true))
    }

    val files: List<Part> by lazy {
        request.parts.toList()
    }

    val cookies: List<Cookie> by lazy {
        request.cookies.toList()
    }

    val userAgent: String by lazy {
        getHeader("User-Agent")!!
    }

    val remoteIp: String by lazy {
        request.remoteAddr
    }

    val host: String by lazy {
        getHeader("Host")!!
    }

    val contentType: String by lazy {
        getHeader("Content-Type")!!
    }

    fun getCookie(name: String): Cookie? {
        return cookies.filter { it.name == name }.first()
    }

    fun getFile(name: String): Part? {
        return request.getPart(name)
    }

    fun getParameter(name: String): String? {
        return request.getParameter(name)
    }

    fun getPathVariable(name: String): String? {
        return pathVariables[name]
    }

    fun getHeader(name: String): String? {
        return request.getHeader(name)
    }



}