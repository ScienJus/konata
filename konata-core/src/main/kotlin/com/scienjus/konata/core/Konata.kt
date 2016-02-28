package com.scienjus.konata.core

import com.scienjus.konata.core.route.RouteBuilder
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

/**
 * @author ScienJus
 * @date 16/2/20.
 */
open class Konata {

    var port = DEFAULT_PORT

    private val router = Router()

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
                .addHttpListener(8080, "localhost")
                .setHandler(rootHandler).build();
        server.start();
    }

    fun dispatch(req: HttpServletRequest, res: HttpServletResponse) {
        val route = router.match(req.method, req.requestURI)
        if (route != null) {
            route.handler.invoke(Request(req, route.getPathParameters(req.requestURI)), Response(res))
        }
    }

    fun addRoute(route: RouteBuilder): RouteBuilder {
        router.addRoute(route)
        return route
    }

    fun GET(uriPattern: String, handler: (Request, Response) -> Unit, name: String? = null): RouteBuilder {
        return addRoute(RouteBuilder.GET(uriPattern, handler, name = name))
    }
}