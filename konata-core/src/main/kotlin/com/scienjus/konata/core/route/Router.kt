package com.scienjus.konata.core.route

import java.util.regex.PatternSyntaxException

/**
 * @author ScienJus
 * @date 16/2/20.
 */
class Router {

    private val routeBuilders: MutableList<RouteBuilder> = mutableListOf()

    private val httpMethodMatchMap: MutableMap<HttpMethod, MutableList<Route>> = mutableMapOf()
    private val uriMatchMap: MutableMap<String, Route> = mutableMapOf()

    fun register() {
        routeBuilders.forEach { routeBuilder ->
            val route = build(routeBuilder)
            if (httpMethodMatchMap[route.httpMethod] == null) {
                httpMethodMatchMap[route.httpMethod] = mutableListOf()
            }
            httpMethodMatchMap[route.httpMethod]!!.add(route)
            if (route.routePattern.pathParameterNames.isEmpty()) {
                if (uriMatchMap[route.key] != null) {
                    throw RuntimeException("more than one route have the same uri ${route.key}")
                }
                uriMatchMap[route.routePattern.pattern.toString()] = route
            }
        }
    }

    fun build(routeBuilder: RouteBuilder): Route {
        val realUriPattern = realUriPattern(routeBuilder)
        try {
            val routePattern = RoutePattern.compile(realUriPattern)
            return Route(routePattern, routeBuilder.httpMethod, routeBuilder.handler, routeBuilder.name)
        } catch (e: PatternSyntaxException) {
            throw RuntimeException("Route build err, uri: $realUriPattern", e)
        }
    }

    private fun realUriPattern(routeBuilder: RouteBuilder): String {
        var realUriPattern = routeBuilder.uriPattern
        var group = routeBuilder.group
        while (group != null) {
            fun concatUriPattern(): String {
                val notNullGroup: RouteGroupBuilder = group!!
                return if (notNullGroup.uriPattern.endsWith("/")) {
                    if (realUriPattern.startsWith("/")) {
                        notNullGroup.uriPattern + realUriPattern.substring(1)
                    } else {
                        notNullGroup.uriPattern + realUriPattern
                    }
                } else{
                    if (realUriPattern.startsWith("/")) {
                        notNullGroup.uriPattern + realUriPattern
                    } else {
                        notNullGroup.uriPattern + "/" + realUriPattern
                    }

                }
            }
            realUriPattern = concatUriPattern()
            group = group.parent
        }
        return realUriPattern
    }

    fun match(httpMethodStr: String, uri: String): Route? {
        val httpMethod = HttpMethod.valueOf(httpMethodStr)
        var route = matchByUri(httpMethod, uri)
        if (route == null) {
            route = matchByUri(HttpMethod.ALL, uri)
            if (route == null) {
                route = matchByPattern(httpMethod, uri)
                if (route == null) {
                    route = matchByPattern(HttpMethod.ALL, uri)
                }
            }
        }
        return route
    }

    private fun matchByPattern(httpMethod: HttpMethod, uri: String): Route? {
        val routes = httpMethodMatchMap[httpMethod] ?: return null
        routes.forEach { route ->
            if (route.matches(uri)) {
                return route
            }
        }
        return null
    }

    private fun matchByUri(httpMethod: HttpMethod, uri: String): Route? {
        if (uriMatchMap["$uri#$httpMethod"] != null) {
            return uriMatchMap[uri]
        }
        return null
    }

    fun addRoute(route: RouteBuilder) {
        routeBuilders.add(route)
    }
}