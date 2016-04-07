package com.scienjus.konata.core.route

import java.util.regex.Pattern

/**
 * @author ScienJus
 * @date 16/2/20.
 */
data class RoutePattern private constructor(val regex: String, val pathVariableNames: List<String>) {
    

    companion object {
        private val PATH_VARIABLE_REGEX: String = ":([a-zA-Z0-9]+)"
        private val PATH_VARIABLE_REPLACE: String = "([^/]+)"

        fun compile(uriPattern: String): RoutePattern {
            val buffer = StringBuffer()
            val pathVariableNames: MutableList<String> = mutableListOf()
            val pattern = Pattern.compile(PATH_VARIABLE_REGEX)
            val matcher = pattern.matcher(uriPattern)
            while (matcher.find()) {
                pathVariableNames.add(matcher.group(1))
                matcher.appendReplacement(buffer, PATH_VARIABLE_REPLACE)
            }
            matcher.appendTail(buffer)
            return RoutePattern(buffer.toString(), pathVariableNames.toList())
        }
    }
}