package com.scienjus.konata.orm

import java.io.Serializable

/**
 * ActiveRecord提供查询方法的入口
 * @author ScienJus
 * @date 16/2/27.
 */
open class ActiveRecord<T : Model, ID : Serializable>() {

    val clazz: Class<T>

    init {
        try {
            clazz = getParameterizedTypes(this)[0] as Class<T>
        } catch (e: Exception) {
            throw RuntimeException("ActiveRecord Init Error, Class: ${this.javaClass.name}", e)
        }
    }

    val all: List<T> get() = all()
    val first: T get() = first()
    val last: T get() = last()

    private fun all(): List<T> {
        return listOf()
    }

    private fun first(): T {
        return clazz.newInstance()
    }

    private fun last(): T {
        return clazz.newInstance()
    }

    fun find(id: ID): T {
        return clazz.newInstance()
    }

    fun findBy(keyValue: Pair<String, Any>): T {
        return clazz.newInstance()
    }

    fun where(where: String, vararg params: Any): QueryBuilder {
        return QueryBuilder().where(where, *params)
    }

    fun where(keyValue: Pair<String, Any>): QueryBuilder {
        return where("", keyValue)
    }

    fun select(vararg fields: String): QueryBuilder {
        return QueryBuilder()
    }



}

