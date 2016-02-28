package com.scienjus.konata.core.route

import com.scienjus.konata.core.Request
import com.scienjus.konata.core.Response

/**
 * @author ScienJus
 * @date 16/2/20.
 */
class RouteGroupBuilder(val uriPattern: String, val parent: RouteGroupBuilder? = null, var name: String? = null) {

    // '_list' is private read-write, 'list' is public read-only
    private val _children: MutableList<RouteGroupBuilder>
    val children: List<RouteGroupBuilder> get() = _children.toList()

    private val _routes: MutableList<RouteBuilder>
    val routes: List<RouteBuilder> get() = _routes.toList()
    
    init {
        this.parent?._children?.add(this)
        this._routes = mutableListOf()
        this._children = mutableListOf()
    }
    
    fun addRoute(route: RouteBuilder) {
        this._routes.add(route)
    }

    fun GET(uriPattern: String, handler: (Request, Response) -> Unit, name: String? = null): RouteBuilder {
        return RouteBuilder.GET(uriPattern, handler, this, name);
    }


}