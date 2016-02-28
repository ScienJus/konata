package com.scienjus.konata.orm


/**
 * Model对应实体类, 提供一个对象的创建/删除/更新方法
 * @author ScienJus
 * @date 16/2/27.
 */
open class Model {

    private val id: Int? = null

    fun save() {
        DataSource.save(this)
    }

    fun destroy() {
        DataSource.destroy(this)
    }

}