package com.scienjus.konata.core.route

import com.scienjus.konata.core.Request
import com.scienjus.konata.core.Response
import java.util.regex.PatternSyntaxException

/**
 * @author ScienJus
 * @date 16/2/20.
 */
class RouteBuilder(val httpMethod: HttpMethod, val uriPattern: String, val handler: (Request, Response) -> Unit, val group: RouteGroupBuilder? = null, var name: String? = null) {

    init {
        this.group?.addRoute(this)
    }



    companion object {
        fun GET(uriPattern: String, handler: (Request, Response) -> Unit, group: RouteGroupBuilder? = null, name: String? = null): RouteBuilder {
            return RouteBuilder(HttpMethod.GET, uriPattern, handler, group, name)
        }
    }

}