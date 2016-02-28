package com.scienjus.konata.core.route

import java.util.regex.Pattern

/**
 * @author ScienJus
 * @date 16/2/20.
 */
class RoutePattern private constructor(pattern: Pattern, pathParameterNames: List<String>) {
    val pattern: Pattern
    val pathParameterNames: List<String>

    init {
        this.pattern = pattern
        this.pathParameterNames = pathParameterNames
    }

    override fun toString(): String {
        return "RoutePattern(pattern=$pattern, pathParameterNames=$pathParameterNames)"
    }

    companion object {
        private val PATH_PARAMETER_REGEX: String = ":([a-zA-Z0-9]+)"    //regex named group not supported '_', so sad
        private val PATH_PARAMETER_REPLACE: String = "(?<$1>[^/]+)"

        fun compile(uriPattern: String): RoutePattern {
            val buffer = StringBuffer()
            val pathParameterNames: MutableList<String> = mutableListOf()
            val pattern = Pattern.compile(PATH_PARAMETER_REGEX)
            val matcher = pattern.matcher(uriPattern)
            while (matcher.find()) {
                pathParameterNames.add(matcher.group(1))
                matcher.appendReplacement(buffer, PATH_PARAMETER_REPLACE)
            }
            matcher.appendTail(buffer)
            return RoutePattern(Pattern.compile(buffer.toString()), pathParameterNames.toList())
        }
    }
}