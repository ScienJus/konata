package com.scienjus.konata.core.route

/**
 * @author ScienJus
 * @date 16/2/20.
 */
enum class HttpMethod {
    GET, POST, PUT, DELETE, PATCH, ALL;

    companion object {
        fun all(): List<HttpMethod> {
            return HttpMethod.values().filter { it != ALL }
        }
    }
}