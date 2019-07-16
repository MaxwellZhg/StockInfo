package com.dycm.base2app.network

import com.dycm.base2app.Transaction

/**
 * 请求的基类
 */
open class BaseRequest : Transaction {

    /**
     * view 用来区分请求类型
     */
    var view: Int = 0

    /**
     * key 用来区分某次请求
     */
    var key = ""

    constructor(transaction: String) : super(transaction)

    constructor(transaction: String, view: Int) : super(transaction) {
        this.view = view
    }

    constructor(transaction: String, key: String) : super(transaction) {
        this.key = key
    }

    constructor(transaction: String, view: Int, key: String) : super(transaction) {
        this.view = view
        this.key = key
    }

    @Throws(Exception::class)
    fun toMap(): Map<String, Any> {
        val map = HashMap<String, Any>()
        val fields = this.javaClass.declaredFields
        for (field in fields) {
            field.isAccessible = true
            val fieldValue = field.get(this)
            if (fieldValue != null) {
                map[field.name] = fieldValue
            }
        }
        return map
    }

    companion object {

        fun instance(transaction: String): BaseRequest {
            return BaseRequest(transaction)
        }

        fun instance(transaction: String, view: Int): BaseRequest {
            return BaseRequest(transaction, view)
        }
    }
}
