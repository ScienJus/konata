package com.scienjus.konata.core.route

import com.scienjus.konata.core.Request
import com.scienjus.konata.core.Response

/**
 * @author ScienJus
 * @date 16/2/20.
 */
data class Route(val regex: String, val pathVariableNames: List<String>, val httpMethod: HttpMethod, val handler: (Request, Response) -> Unit, val name: String?) {

    val key: String get() = "$regex#$httpMethod"

    val isStaticRoute: Boolean get() = pathVariableNames.isEmpty()
}