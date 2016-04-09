package com.scienjus.konata.core.route

import com.scienjus.konata.core.Request
import com.scienjus.konata.core.Response
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.javaType

/**
 * @author ScienJus
 * @date 16/4/8.
 */
object HandlerFactory {

    private val instances: MutableMap<Type, Any> = mutableMapOf()

    fun createFunctionHandler(function: KFunction<Any>): (Request, Response) -> Unit {
        return { req, res ->
            val parameters: MutableMap<KParameter, Any?> = mutableMapOf()
            function.parameters.forEach { parameter ->
                val type = parameter.type.javaType
                // obj instance
                if (parameter.kind == KParameter.Kind.INSTANCE) {
                    var instance = instances[type]
                    if (instance == null) {
                        instance = (type as Class<*>).newInstance()
                        instances[type] = instance
                    }
                    parameters.put(parameter, instance)
                } else {
                    // request or response
                    if (type == Request::class.java) {
                        parameters.put(parameter, req)
                    } else if (type == Response::class.java) {
                        parameters.put(parameter, res)
                    } else {
                        // request parameter or path variable
                        var value = req.getParameter(parameter.name!!)
                        if (value == null) {
                            value = req.getPathVariable(parameter.name!!)
                        }
                        if (value == null) {
                            parameters.put(parameter, value)
                        } else {
                            // TODO Formatter
                            val formatter = FormatterFactory.getFormatter((parameter.type.javaType as Class<*>).kotlin)
                            if (formatter != null) {
                                parameters.put(parameter, formatter.invoke(value))
                            } else {
                                // TODO Model
                                parameters.put(parameter, null)
                            }
                        }
                    }
                }
            }
            // TODO Converter
            res.send(function.callBy(parameters).toString())
        }
    }
}