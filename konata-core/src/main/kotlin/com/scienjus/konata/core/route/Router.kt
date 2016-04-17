package com.scienjus.konata.core.route

import java.util.regex.PatternSyntaxException

/**
 * @author ScienJus
 * @date 16/2/20.
 */
class Router {

    private val routeBuilders: MutableList<RouteBuilder> = mutableListOf()

    private val staticRoutes: MutableMap<HttpMethod, MutableMap<String, Route>> = mutableMapOf()

    private val regexRoutes: MutableMap<HttpMethod, FastRoute> = mutableMapOf()

    fun register() {
        val fastRouteBuilders: MutableMap<HttpMethod, FastRouteBuilder> = mutableMapOf()
        routeBuilders.forEach { routeBuilder ->
            val route = build(routeBuilder)
            if (route.httpMethod == HttpMethod.ALL) {
                HttpMethod.all().forEach { httpMethod ->
                    if (route.isStaticRoute) {
                        registerStaticRoute(httpMethod, route)
                    } else {
                        registerRegexRoute(httpMethod, route, fastRouteBuilders)
                    }
                }
            } else {
                if (route.isStaticRoute) {
                    registerStaticRoute(route.httpMethod, route)
                } else {
                    registerRegexRoute(route.httpMethod, route, fastRouteBuilders)
                }
            }
        }
        fastRouteBuilders.entries.forEach { entry ->
            regexRoutes.put(entry.key, entry.value.build())
        }
    }

    private fun registerRegexRoute(httpMethod: HttpMethod, route: Route, fastRouteBuilders: MutableMap<HttpMethod, FastRouteBuilder>) {
        if (fastRouteBuilders[httpMethod] == null) {
            fastRouteBuilders[httpMethod] = FastRouteBuilder()
        }
        fastRouteBuilders[httpMethod]!!.addRoute(route)
    }

    private fun registerStaticRoute(httpMethod: HttpMethod, route: Route) {
        if (staticRoutes[httpMethod] == null) {
            staticRoutes[httpMethod] = mutableMapOf()
        }
        if (staticRoutes[httpMethod]!![route.regex] != null) {
            throw RuntimeException("more than one route have the same uri ${route.key}")
        }
        staticRoutes[route.httpMethod]!![route.regex] = route
    }

    private fun build(routeBuilder: RouteBuilder): Route {
        val fullUriPattern = routeBuilder.fullUriPattern
        val fullName = routeBuilder.fullName
        try {
            val pattern = RoutePattern.compile(fullUriPattern)
            return Route(pattern.regex, pattern.pathVariableNames, routeBuilder.httpMethod, routeBuilder.handler, fullName)
        } catch (e: PatternSyntaxException) {
            throw RuntimeException("Route build err, uri: $fullUriPattern", e)
        }
    }

    fun mapping(requestMethod: String, uri: String): RouteMatch? {
        val httpMethod = HttpMethod.valueOf(requestMethod)
        var routeMatch = mappingStaticRoute(httpMethod, uri)
        if (routeMatch == null) {
            routeMatch = mappingRegexRoute(httpMethod, uri)
        }
        // 404 or 405
        // TODO 405/404 page
        return routeMatch
    }

    private fun mappingRegexRoute(httpMethod: HttpMethod, uri: String): RouteMatch? {
        val fastRoute = regexRoutes[httpMethod] ?: return null
        return fastRoute.find(uri)
    }

    private fun mappingStaticRoute(httpMethod: HttpMethod, uri: String): RouteMatch? {
        var route =  staticRoutes[httpMethod]?.get(uri) ?: return null
        return RouteMatch(route)
    }

    fun addRoute(route: RouteBuilder) {
        routeBuilders.add(route)
    }
}