package com.scienjus.konata.core

import com.scienjus.konata.core.route.RouteBuilder
import com.scienjus.konata.core.route.Router
import org.jetbrains.spek.api.Spek

/**
 * @author ScienJus
 * @date 16/4/14.
 */
class RouterTest : Spek({

    given("a router") {
        val router = Router()
        val emptyHandler = {req: Request, res: Response -> }

        on("add a route get#/users named users") {
            router.addRoute(RouteBuilder.get("/users", emptyHandler, name = "users"))
            router.register()

            it("get#/users should match users") {
                val routeMatch = router.mapping("GET", "/users")

                shouldNotBeNull(routeMatch)

                shouldEqual("users", routeMatch!!.route.name)
            }

            it("get#/ should not match anything") {
                shouldBeNull(router.mapping("GET", "/"))
            }

            it("get#/users/123 should not match anything") {
                shouldBeNull(router.mapping("GET", "/users/123"))
            }
        }
    }
})
