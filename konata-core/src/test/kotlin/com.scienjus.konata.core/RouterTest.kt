package com.scienjus.konata.core

import com.scienjus.konata.core.route.RouteBuilder
import com.scienjus.konata.core.route.RouteGroupBuilder
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

                it("should named users") {
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

                it("should named user detail") {
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

        given("a route group for CRUD user") {
            val router = Router()
            val group = RouteGroupBuilder("/users", name = "user")
            val listRoute   = group.get(handler = emptyHandler, name = "list")
            val detailRoute = group.get("/:id", emptyHandler, name = "detail")
            val createRoute = group.post(handler = emptyHandler, name = "create")
            val editRoute   = group.put("/:id", emptyHandler, name = "edit")
            val deleteRoute = group.delete("/:id", emptyHandler, name = "delete")
            router.addRoute(listRoute)
            router.addRoute(detailRoute)
            router.addRoute(createRoute)
            router.addRoute(editRoute)
            router.addRoute(deleteRoute)
            router.register()

            on("mapping get#/users") {
                val routeMatch = router.mapping("GET", "/users")

                it("should match a route") {
                    shouldNotBeNull(routeMatch)
                }

                it("should named user.list") {
                    routeMatch!!.route.name shouldEqual "user.list"
                }
            }

            on("mapping get#/users/123") {
                val routeMatch = router.mapping("GET", "/users/123")

                it("should match a route") {
                    shouldNotBeNull(routeMatch)
                }

                it("should named user.detail") {
                    routeMatch!!.route.name shouldEqual "user.detail"
                }

                it("should have a path variable 'id'") {
                    routeMatch!!.pathVariables.keys shouldContains "id"
                }

                it("should have a path variable 'id' values '123'") {
                    routeMatch!!.pathVariables["id"] shouldEqual "123"
                }
            }

            on("mapping post#/users") {
                val routeMatch = router.mapping("POST", "/users")

                it("should match a route") {
                    shouldNotBeNull(routeMatch)
                }

                it("should named user.create") {
                    routeMatch!!.route.name shouldEqual "user.create"
                }
            }

            on("mapping put#/users/123") {
                val routeMatch = router.mapping("PUT", "/users/123")

                it("should match a route") {
                    shouldNotBeNull(routeMatch)
                }

                it("should named user.edit") {
                    routeMatch!!.route.name shouldEqual "user.edit"
                }

                it("should have a path variable 'id'") {
                    routeMatch!!.pathVariables.keys shouldContains "id"
                }

                it("should have a path variable 'id' values '123'") {
                    routeMatch!!.pathVariables["id"] shouldEqual "123"
                }
            }

            on("mapping delete#/users") {
                val routeMatch = router.mapping("DELETE", "/users/123")

                it("should match a route") {
                    shouldNotBeNull(routeMatch)
                }

                it("should named user.delete") {
                    routeMatch!!.route.name shouldEqual "user.delete"
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
