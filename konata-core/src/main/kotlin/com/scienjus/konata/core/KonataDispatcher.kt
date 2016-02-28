package com.scienjus.konata.core

import javax.servlet.*
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author ScienJus
 * @date 16/2/20.
 */
class KonataDispatcher(val konata: Konata) : HttpServlet(), Filter {

    override fun init(config: FilterConfig) {
    }

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        dispatch(req, res)
    }

    override fun service(req: ServletRequest, res: ServletResponse) {
        dispatch(req, res)
    }

    fun dispatch(req: ServletRequest, res: ServletResponse) {
        konata.dispatch(req as HttpServletRequest, res as HttpServletResponse)
    }
}