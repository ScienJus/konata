package com.scienjus.konata.core

import com.scienjus.konata.core.route.RouteBuilder
import com.scienjus.konata.core.route.Router
import org.jetbrains.spek.api.Spek

/**
 * @author ScienJus
 * @date 16/4/14.
 */
class RouterTest : Spek({

    given("an empty handler") {
        val emptyHandler = {req: Request, res: Response -> }

        given("a route mapped get#/users and named users") {
            val router = Router()
            router.addRoute(RouteBuilder.get("/users", emptyHandler, name = "users"))
            router.register()

            on("mapping get#/users") {
                val routeMatch = router.mapping("GET", "/users")

                it("should match a route") {
                    shouldNotBeNull(routeMatch)
                }

                it("should match the route named users") {
                    routeMatch!!.route.name shouldEqual "users"
                }
            }
            on("mapping get#/") {
                it("should not match anything") {
                    shouldBeNull(router.mapping("GET", "/"))
                }
            }

            on("mapping get#/users/123") {
                it("should not match anything") {
                    shouldBeNull(router.mapping("GET", "/users/123"))
                }
            }
        }

        given("a route mapped get#/users/:id and named user detail") {
            val router = Router()
            router.addRoute(RouteBuilder.get("/users/:id", emptyHandler, name = "user detail"))
            router.register()

            on("mapping get#/users/123") {
                val routeMatch = router.mapping("GET", "/users/123")

                it("should match a route") {
                    shouldNotBeNull(routeMatch)
                }

                it("should match the route named user detail") {
                    routeMatch!!.route.name shouldEqual "user detail"
                }

                it("should have a path variable 'id'") {
                    routeMatch!!.pathVariables.keys shouldContains "id"
                }

                it("should have a path variable 'id' values '123'") {
                    routeMatch!!.pathVariables["id"] shouldEqual "123"
                }
            }
        }
    }
})
