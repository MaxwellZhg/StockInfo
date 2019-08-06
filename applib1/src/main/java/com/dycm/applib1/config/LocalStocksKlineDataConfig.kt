package com.dycm.applib1.config

import android.os.Looper
import com.dycm.applib1.socket.vo.kline.MinuteKlineData
import com.dycm.base2app.infra.AbsConfig
import com.dycm.base2app.infra.LogInfra
import com.dycm.base2app.infra.StorageInfra
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/6 16:44
 *    desc   : 自选股分时K线图本地缓存数据
 */
class LocalStocksKlineDataConfig : AbsConfig() {

    private var klineData = HashMap<String, ArrayList<MinuteKlineData>>()

    /***
     * 添加K线缓存数据
     */
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

            Observable.just(write())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /***
     * 删除K线缓存数据
     */
    fun remove(ts: String?, code: String?): Boolean {
        try {
            val key = "$ts-$code"
            if (klineData.remove(key) != null) {
                Observable.just(write())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe()
                return true
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    @Synchronized
    override fun write() {
        LogInfra.Log.d("LocalStocksKlineDataConfig", "write thread: " + Looper.myLooper()?.thread?.name)
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
            LogInfra.Log.d("LocalStocksKlineDataConfig", "read thread: " + Looper.myLooper()?.thread?.name)
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