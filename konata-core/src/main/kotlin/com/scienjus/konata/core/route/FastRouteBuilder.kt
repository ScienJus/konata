package com.scienjus.konata.core.route

import java.util.regex.Pattern

/**
 * @author ScienJus
 * @date 16/4/7.
 */
class FastRouteBuilder {

    private val regex: StringBuilder = StringBuilder("^")

    private var groupPosition = 1

    private val positionMap: MutableMap<Int, Route> = mutableMapOf()

    fun addRoute(route: Route) {
        regex.append("(").append(route.regex).append(")|")
        positionMap.put(groupPosition, route)
        groupPosition += route.pathVariableNames.size + 1
    }

    fun build(): FastRoute {
        if (regex.length > 1) {
            regex.setCharAt(regex.length - 1, '$')
        }
        return FastRoute(Pattern.compile(regex.toString()), positionMap)
    }
}