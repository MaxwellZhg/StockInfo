package com.zhuorui.securities.market.ui.presenter

import androidx.core.app.NotificationManagerCompat
import com.zhuorui.commonwidget.model.Observer
import com.zhuorui.commonwidget.model.Subject
import com.zhuorui.securities.base2app.ui.fragment.AbsEventPresenter
import com.zhuorui.securities.market.manager.StockPriceDataManager
import com.zhuorui.securities.market.model.PushStockPriceData
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.ui.view.RemindSettingView
import com.zhuorui.securities.market.ui.viewmodel.RemindSettingViewModel
import com.zhuorui.securities.market.util.MathUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.math.BigDecimal
import java.util.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 14:28
 *    desc   :
 */
class RemindSettingPresenter : AbsEventPresenter<RemindSettingView, RemindSettingViewModel>(), Observer {

    private var priceDataManager: StockPriceDataManager? = null
    private val disposables = LinkedList<Disposable>()

    fun setStockInfo(stockInfo: StockMarketInfo?) {
        viewModel?.stockInfo?.value = stockInfo
        // 取消股价订阅
        if (stockInfo != null) {
            priceDataManager = StockPriceDataManager.getInstance(stockInfo.ts!!, stockInfo.code!!, stockInfo.type!!)
            // 尝试从缓存中获取股价
            val priceData = priceDataManager?.priceData
            if (priceData != null) {
                updatePrice(priceData)
            } else {
                // 添加查询价格监听
                priceDataManager?.registerObserver(this)
            }
        }
    }

    fun getStockInfo(): StockMarketInfo? {
        return viewModel?.stockInfo?.value
    }

    /**
     * 查询股价
     */
    override fun update(subject: Subject<*>?) {
        if (subject is StockPriceDataManager) {
            updatePrice(subject.priceData)
            // 拿到股价后，删掉回调
            priceDataManager?.removeObserver(this)
            priceDataManager = null
        }
    }

    private fun updatePrice(priceData: PushStockPriceData?) {
        if (priceData == null) {
            return
        }
        // 在主线程更新数据
        val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
            emitter.onNext(true)
            emitter.onComplete()
        }).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                // 获取当前最新股价
                val last = MathUtil.rounded3(priceData.last!!)
                viewModel?.stockInfo?.value?.last = last
                // 计算跌涨价格
                val diffPrice = MathUtil.rounded3(priceData.last!!.subtract(priceData.open))
                viewModel?.stockInfo?.value?.diffPrice = diffPrice
                // 计算跌涨幅百分比
                val diffRate = MathUtil.divide2(diffPrice.multiply(BigDecimal.valueOf(100)), priceData.open!!)
                viewModel?.stockInfo?.value?.diffRate = diffRate

                // 更新界面
                var diffState = MathUtil.rounded(diffRate).toInt()
                if (diffState > 1) {
                    diffState = 1
                } else if (diffState < -1) {
                    diffState = -1
                }
                view?.updateStockPrice(last, diffPrice, diffRate, diffState)
            }
        disposables.add(disposable)
    }

    fun checkSetting(): Boolean? {
        val notificationManager: NotificationManagerCompat? = context?.let { NotificationManagerCompat.from(it) }
        return notificationManager?.areNotificationsEnabled()
    }

    override fun destroy() {
        super.destroy()

        // 取消股价回调
        val stockInfo = viewModel?.stockInfo?.value
        if (stockInfo != null && priceDataManager != null) {
            priceDataManager?.removeObserver(this)
        }

        // 释放disposable
        if (disposables.isNullOrEmpty()) return
        for (disposable in disposables) {
            disposable.dispose()
        }
        disposables.clear()
    }
}