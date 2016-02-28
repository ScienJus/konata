package com.scienjus.konata.orm

/**
 * 构造查询语句的类
 * @author ScienJus
 * @date 16/2/27.
 */
//这里希望能支持四种执行方式,默认为Select, 还支持Update和Delete还有Count
//Example:
//val users = User.select("id", "name").where("age", 18).order("-id").offset(5).limit(10)   Select默认执行
//User.where("age", 18).update("age", 19)  Update
//User.where("age", 18).delete  Delete
//val count = User.where("age", 18).count   Count
class QueryBuilder {

    var where = ""

    var params: List<Any> = emptyList()

    var orders: MutableList<String> = mutableListOf()

    var select: List<String> = listOf()

    fun order(vararg fields: String): QueryBuilder {
        orders.addAll(fields)
        return this
    }

    fun where(where: String, vararg params: Any): QueryBuilder {
        if (params.isNotEmpty() || where.isNotBlank()) {
            throw RuntimeException("more than one where sql")
        }
        this.where = where
        this.params = params.toList()
        return this
    }

    fun where(keyValue: Pair<String, Any>): QueryBuilder {
        return where("", keyValue)
    }

    fun select(vararg fields: String): QueryBuilder {
        if (select.isNotEmpty()) {
            throw RuntimeException("more than one select sql")
        }
        select = fields.toList()
        return this
    }

    fun execute() {
        println("do it")
    }

}