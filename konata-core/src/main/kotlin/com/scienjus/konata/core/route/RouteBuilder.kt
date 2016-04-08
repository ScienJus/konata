package com.scienjus.konata.core.route

import com.scienjus.konata.core.Request
import com.scienjus.konata.core.Response

/**
 * @author ScienJus
 * @date 16/2/20.
 */
class RouteBuilder(val httpMethod: HttpMethod, val uriPattern: String, val handler: (Request, Response) -> Unit, val group: RouteGroupBuilder? = null, var name: String? = null) {

    init {
        this.group?.addRoute(this)
    }

    val fullUriPattern: String get() {
        var fullUriPattern = uriPattern
        var group = group
        while (group != null) {
            fun concatUriPattern(): String {
                val notNullGroup: RouteGroupBuilder = group!!
                return if (notNullGroup.uriPattern.endsWith("/")) {
                    if (fullUriPattern.startsWith("/")) {
                        notNullGroup.uriPattern + fullUriPattern.substring(1)
                    } else {
                        notNullGroup.uriPattern + fullUriPattern
                    }
                } else{
                    if (fullUriPattern.startsWith("/")) {
                        notNullGroup.uriPattern + fullUriPattern
                    } else {
                        notNullGroup.uriPattern + "/" + fullUriPattern
                    }

                }
            }
            fullUriPattern = concatUriPattern()
            group = group.parent
        }
        return fullUriPattern
    }

    companion object {
        fun get(uriPattern: String, handler: (Request, Response) -> Unit, group: RouteGroupBuilder? = null, name: String? = null): RouteBuilder {
            return RouteBuilder(HttpMethod.GET, uriPattern, handler, group, name)
        }
    }

}