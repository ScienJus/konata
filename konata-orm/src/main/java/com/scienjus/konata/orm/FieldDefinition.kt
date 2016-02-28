package com.scienjus.konata.orm

import java.lang.reflect.Field

/**
 * 保存字段信息
 * @author ScienJus
 * @date 16/2/28.
 */
class FieldDefinition(val field: Field) {

    fun getValue(obj: Any): Any {
        if (!field.isAccessible) {
            field.isAccessible = true
        }
        return field.get(obj);
    }

    fun setValue(obj: Any, value: Any) {
        if (!field.isAccessible) {
            field.isAccessible = true
        }
        field.set(obj, value)
    }
}