package com.scienjus.konata.core

import com.scienjus.konata.core.route.RouteBuilder
import com.scienjus.konata.core.route.RouteGroupBuilder
import com.scienjus.konata.core.route.Router
import io.undertow.Handlers
import io.undertow.Undertow
import io.undertow.server.handlers.GracefulShutdownHandler
import io.undertow.servlet.Servlets
import io.undertow.servlet.api.FilterInfo
import io.undertow.servlet.api.ServletInfo
import io.undertow.servlet.handlers.DefaultServlet
import io.undertow.servlet.util.ImmediateInstanceFactory
import javax.servlet.DispatcherType
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.reflect

/**
 * @author ScienJus
 * @date 16/2/20.
 */
class Konata {

    var port = DEFAULT_PORT

    private val router = Router()

    fun listen(port: Int): Konata {
        this.port = port
        return this
    }

    fun start() {
        router.register()
        val konataDispatcher = KonataDispatcher(this)
        val info = Servlets.deployment()
        info.deploymentName = "Konata"
        info.classLoader = this.javaClass.classLoader
        info.contextPath = "/"
        info.isIgnoreFlush = true
        info.addFilter(FilterInfo("KonataDispatcher", KonataDispatcher::class.java, ImmediateInstanceFactory(konataDispatcher)))
        info.addFilterUrlMapping("KonataDispatcher", "/*", DispatcherType.REQUEST)

        val defaultServlet = ServletInfo("DefaultServlet", DefaultServlet::class.java)
        defaultServlet.addMapping("/")
        info.addServlet(defaultServlet)

        val deploymentManager = Servlets.defaultContainer().addDeployment(info);
        deploymentManager.deploy()
        val handler = deploymentManager.start()
        val contextHandler = Handlers.path(Handlers.redirect("/"));
        contextHandler.addPrefixPath("/", handler)

        val rootHandler = GracefulShutdownHandler(contextHandler);

        val server = Undertow.builder()
                .addHttpListener(port, "localhost")
                .setHandler(rootHandler).build();
        println("server started on localhost:" + port)
        server.start();
    }

    fun dispatch(req: HttpServletRequest, res: HttpServletResponse) {
        val routeMatch = router.mapping(req.method, req.requestURI)
        if (routeMatch != null) {
            routeMatch.route.handler.invoke(Request(req, routeMatch.pathVariables), Response(res))
        }
    }

    fun addRoute(route: RouteBuilder): RouteBuilder {
        router.addRoute(route)
        return route
    }

    // http Method
    fun get(uriPattern: String, handler: (Request, Response) -> Unit, name: String? = null): RouteBuilder {
        return addRoute(RouteBuilder.get(uriPattern, handler, name = name))
    }


    /**
     * create a group
     */
    fun group(uriPattern: String, name: String? = null, init: RouteGroupBuilder.() -> Unit) {
        val routeGroupBuilder = RouteGroupBuilder(uriPattern, name = name)
        routeGroupBuilder.init()
        routeGroupBuilder.routes.forEach { this.addRoute(it) }
    }

    /* kotlin.reflect.KotlinReflectionInternalError: Introspecting local functions, lambdas and anonymous functions is not yet fully supported in Kotlin reflection
    fun get(uriPattern: String, handler: Function<Any>, name: String? = null): RouteBuilder {
        val function = handler.reflect()!!
        return get(uriPattern, name = name, handler = { req, res ->
            val parameters: MutableMap<KParameter, Any?> = mutableMapOf()
            function.parameters.forEach { parameter ->
                var value = req.getParameter(parameter.name!!)
                if (value == null) {
                    value = req.getPathParamter(parameter.name!!)
                }
                parameters.put(parameter, value)
            }
            res.send(function.callBy(parameters).toString())
        })
    }
     */
}

fun konata(init: Konata.() -> Unit): Konata {
    val konata = Konata()
    konata.init()
    return konata;
}
