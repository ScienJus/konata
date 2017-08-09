# Konata

micro web framework like [decebals/pippo][2] and written in Kotlin

### Route DSL

Konata uses [nikic/FastRoute][1] and has a simple DSL to configure routes. like:


```
fun main(args: Array<String>) {
    konata {
    
        // higher-order functions, (Request, Response) -> Unit
        get("/", { req, res ->
            res.send("home")
        })

        // route groups
        group("/users") {
            get(":id", { req, res ->
                res.send("user(id=${req.getPathVariable("id")})")
            })

            group("posts") {
                get(":id", { req, res ->
                    res.send("post(id=${req.getPathVariable("id")})")
                })
            }
        }
        
        // anonymous functions is not supported yet :(
        get("/tags/:name", fun (name: String): String {
            return "tag(name=$name)"
        })
        
        // but you can use local functions
        get("/tags/:name", ::showTag)
        
        // or member functions
        get("/tags/:name", TagController::show)
        
        class TagController {

            fun show(name: String): String {
                return "tag(name=$name)"
            }
        }
    }.start()
}

fun showTag(name: String): String {
    return "tag(name=$name)"
}
```


[1]: https://github.com/nikic/FastRoute
[2]: https://github.com/decebals/pippo


