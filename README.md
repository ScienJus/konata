# Konata

micro web framework like [decebals/pippo][2] and write in Kotlin

### Route DSL

Konata uses [nikic/FastRoute][1] and has a simple DSL to configure routes. like:


```
fun main(args: Array<String>) {
    konata {
        get("/", { req, res ->
            res.send("home")
        })

        group("/users") {
            get(":id", { req, res ->
                res.send("user(id=" + req.getPathParamter("id") + ")")
            })

            group("posts") {
                get(":id", { req, res ->
                    res.send("post(id=" + req.getPathParamter("id") + ")")
                })
            }
        }

    }.start()
}
```


[1]: https://github.com/nikic/FastRoute
[2]: https://github.com/decebals/pippo


