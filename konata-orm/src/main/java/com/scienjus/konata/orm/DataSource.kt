package com.scienjus.konata.orm

import com.alibaba.druid.pool.DruidDataSource

/**
 * DataSource负责实质性的数据库操作
 * @author ScienJus
 * @date 16/2/27.
 */
object DataSource {

    val tableDefinitions: MutableMap<Class<out Model>, TableDefinition> = mutableMapOf()

    val dataSource: javax.sql.DataSource by lazy {
        val dataSource = DruidDataSource()
        with(dataSource) {
            url = "jdbc:mysql://127.0.0.1:3306/konata"
            username = ""
            password = ""
        }
        dataSource
    }

    fun <T : Model> save(t: T) {
        if (tableDefinitions[t.javaClass] == null) {
            initTableDefinition(t.javaClass)
        }
        val tableDefinition = tableDefinitions[t.javaClass]!!
        dataSource.use { conn ->
            tableDefinition.buildInsertStatement(conn, t).use { statement ->
                statement.execute()
            }
        }
    }

    fun <T : Model> destroy(t: T) {
        if (tableDefinitions[t.javaClass] == null) {
            initTableDefinition(t.javaClass)
        }
        val tableDefinition = tableDefinitions[t.javaClass]!!
        println(tableDefinition.buildDeleteSQL())
        dataSource.use { conn ->
            tableDefinition.buildDeleteStatement(conn, t).use { statement ->
                statement.execute()
            }
        }
    }

    private fun <T : Model> initTableDefinition(clazz: Class<T>) {
        val fields = clazz.declaredFields.filter { it.name != "Companion" }.map { FieldDefinition(it) }
        val primaryKey = FieldDefinition(clazz.declaredFields.firstOrNull { it.isAnnotationPresent(Id::class.java) } ?: Model::class.java.getDeclaredField("id"))
        tableDefinitions[clazz] = TableDefinition(clazz.simpleName, primaryKey, fields)
    }

}

