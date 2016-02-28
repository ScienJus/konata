package com.scienjus.konata.orm

import java.sql.Connection
import java.sql.PreparedStatement

/**
 * 保存表信息并负责与表相关的基础SQL的生成
 * @author ScienJus
 * @date 16/2/28.
 */
class TableDefinition(val tableName: String, val primaryKey: FieldDefinition, val fields: List<FieldDefinition>) {


    fun buildInsertSQL(): String {
        val fieldSQL = fields.map { it.field.name }.joinToString(",", prefix = "(", postfix = ")")
        val valueSQL = fields.map { "?" }.joinToString(",", prefix = "(", postfix = ")")
        var sql = "insert into $tableName $fieldSQL values $valueSQL"
        return sql
    }

    fun buildInsertStatement(conn: Connection, obj: Any): PreparedStatement {
        val sql = buildInsertSQL();
        val statement = conn.prepareStatement(sql)
        fields.forEachIndexed { i, field ->
            statement.setObject(i + 1, field.getValue(obj))
        }
        return statement
    }

    fun buildUpdateSQL(): String {
        val updateFields = fields.filter { it != primaryKey }
        val fieldSQL = updateFields.map { "${it.field.name}=?" }.joinToString(",", prefix = "(", postfix = ")")
        var sql = "update $tableName set $fieldSQL where ${primaryKey.field.name}=?"
        return sql
    }

    fun buildUpdateStatement(conn: Connection, obj: Any): PreparedStatement {
        val sql = buildUpdateSQL()
        val statement = conn.prepareStatement(sql)
        var i = 1
        fields.filter { it != primaryKey }.forEach { field ->
            statement.setObject(i++, field.getValue(obj))
        }
        statement.setObject(i, primaryKey.getValue(obj))
        return statement
    }

    fun buildDeleteSQL(): String {
        return "delete from $tableName where ${primaryKey.field.name}=?"
    }

    fun buildDeleteStatement(conn: Connection, obj: Any): PreparedStatement {
        val sql = buildDeleteSQL()
        val statement = conn.prepareStatement(sql)
        statement.setObject(1, primaryKey.getValue(obj))
        return statement
    }

    fun buildPrimaryKeyExistsSQL(): String {
        return "select count(*) from $tableName where ${primaryKey.field.name}=?"
    }


    fun buildPrimaryKeyExistsStatement(conn: Connection, obj: Any): PreparedStatement {
        val sql = buildPrimaryKeyExistsSQL()
        val statement = conn.prepareStatement(sql)
        statement.setObject(1, primaryKey.getValue(obj))
        return statement
    }
}