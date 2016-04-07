package com.scienjus.konata.core.route

import com.scienjus.konata.core.Request
import com.scienjus.konata.core.Response

/**
 * @author ScienJus
 * @date 16/2/20.
 */
class RouteGroupBuilder(val uriPattern: String, val parent: RouteGroupBuilder? = null, var name: String? = null) {

    // '_list' is private read-write, 'list' is public read-only
    private val _routes: MutableList<RouteBuilder>
    val routes: List<RouteBuilder> get() = _routes.toList()
    
    init {
        this._routes = mutableListOf()
    }
    
    fun addRoute(route: RouteBuilder) {
        this._routes.add(route)
    }

    fun get(uriPattern: String, handler: (Request, Response) -> Unit, name: String? = null): RouteBuilder {
        return RouteBuilder.get(uriPattern, handler, this, name);
    }

    fun group(uriPattern: String, name: String? = null, init: RouteGroupBuilder.() -> Unit) {
        val routeGroupBuilder = RouteGroupBuilder(uriPattern, this, name)
        routeGroupBuilder.init()
        routeGroupBuilder.routes.forEach { this.addRoute(it) }
    }

}