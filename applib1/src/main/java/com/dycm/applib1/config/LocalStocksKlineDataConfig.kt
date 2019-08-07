package com.dycm.applib1.config

import android.os.Looper
import com.dycm.applib1.socket.vo.kline.MinuteKlineData
import com.dycm.base2app.infra.AbsConfig
import com.dycm.base2app.infra.LogInfra
import com.dycm.base2app.infra.StorageInfra


/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/6 16:44
 *    desc   : 自选股分时K线图本地缓存数据
 */
class LocalStocksKlineDataConfig : AbsConfig() {

    private var klineData = HashMap<String, ArrayList<MinuteKlineData>>()

    /**
     * 获取数据
     */
    fun getKlineData(ts: String?, code: String?): ArrayList<MinuteKlineData>? {
        val key = "$ts-$code"
        if (key.isEmpty()) return null
        return klineData[key]!!
    }

    /***
     * 添加K线缓存数据
     */
    @Synchronized
    fun add(ts: String?, code: String?, data: ArrayList<MinuteKlineData>?): Boolean {
        try {
            val key = "$ts-$code"
            var value = klineData[key]
            if (value.isNullOrEmpty()) {
                value = data
            } else {
                value.addAll(data!!)
            }
            klineData[key] = value!!

            write()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /***
     * 删除K线缓存数据
     */
    @Synchronized
    fun remove(ts: String?, code: String?): Boolean {
        try {
            val key = "$ts-$code"
            if (klineData.remove(key) != null) {
                write()
                return true
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun write() {
        LogInfra.Log.d("LocalStocksKlineDataConfig", "write thread: " + Thread.currentThread().name)
        if (Thread.currentThread().name == Looper.getMainLooper().thread.name) {
            throw RuntimeException("IO操作必须在子线程中更新本地数据")
        }
        StorageInfra.put(LocalStocksKlineDataConfig::class.java.simpleName, this)
    }

    companion object {

        var instance: LocalStocksKlineDataConfig? = null
            get() {
                if (field == null) {
                    synchronized(LocalStocksKlineDataConfig::class) {
                        field = read()
                    }
                }
                return field
            }

        private fun read(): LocalStocksKlineDataConfig {
            LogInfra.Log.d("LocalStocksKlineDataConfig", "read thread: " + Thread.currentThread().name)
            var config: LocalStocksKlineDataConfig? =
                StorageInfra.get(
                    LocalStocksKlineDataConfig::class.java.simpleName,
                    LocalStocksKlineDataConfig::class.java
                )
            if (config == null) {
                config = LocalStocksKlineDataConfig()
                config.write()
            }
            return config
        }
    }
}