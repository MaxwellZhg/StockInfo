package com.dycm.base2app.infra

/**
 * 抽象定义Config配置,存储配置信息类需要继承于此类
 */

abstract class AbsConfig protected constructor() {

    init {
        initDefaultAttrs()
    }

    /**
     * 用于构建默认值属性
     */
    protected abstract fun initDefaultAttrs()

    fun write() {
        StorageInfra.put(this)
    }
}