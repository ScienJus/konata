package com.scienjus.konata.core.route

import java.util.regex.Pattern

/**
 * @author ScienJus
 * @date 16/4/7.
 */
class FastRoute(val pattern: Pattern, val routeMap: Map<Int, Route>) {

    fun find(uri: String): RouteMatch? {
        val matcher = pattern.matcher(uri)
        if (matcher.matches()) {
            var position: Int = 1;
            while (matcher.group(position) == null) { position++ }
            val route: Route = routeMap[position]!!;
            val pathVariables: MutableMap<String, String> = mutableMapOf();

            // find path variable
            var pathVariable: String?;
            var j: Int = 0;
            while (++position <= matcher.groupCount()) {
                pathVariable = matcher.group(position)
                if (pathVariable == null) {
                    break;
                }
                pathVariables.put(route.pathVariableNames[j++], pathVariable);
            }
            return RouteMatch(route, pathVariables)
        }
        return null
    }
}