package com.scienjus.konata.core.route

import com.scienjus.konata.core.Request
import com.scienjus.konata.core.Response

/**
 * @author ScienJus
 * @date 16/2/20.
 */
data class Route(val routePattern: RoutePattern, val httpMethod: HttpMethod, val handler: (Request, Response) -> Unit, val name: String?) {

    val key: String get() = "${routePattern.pattern}#$httpMethod"

    fun matches(uri: String): Boolean {
        val matcher = routePattern.pattern.matcher(uri)
        return matcher.matches()
    }

    fun getPathParameters(uri: String): Map<String, String> {
        val matcher = routePattern.pattern.matcher(uri)
        if (matcher.matches()) {
            return routePattern.pathParameterNames.associate { name ->
                name to matcher.group(name)
            }
        }
        return emptyMap()
    }
}