package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackEventFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentRemindSettingBinding
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.StockTsEnum
import com.zhuorui.securities.market.ui.adapter.SettingNoticeAdapter
import com.zhuorui.securities.market.ui.presenter.RemindSettingPresenter
import com.zhuorui.securities.market.ui.view.RemindSettingView
import com.zhuorui.securities.market.ui.viewmodel.RemindSettingViewModel
import com.zhuorui.securities.market.util.MathUtil
import kotlinx.android.synthetic.main.fragment_remind_setting.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 14:22
 *    desc   :
 */
class RemindSettingFragment :
    AbsSwipeBackEventFragment<FragmentRemindSettingBinding, RemindSettingViewModel, RemindSettingView, RemindSettingPresenter>(),
    RemindSettingView, View.OnClickListener{

    private  var  adapter: SettingNoticeAdapter? = null
    private var stockInfo:StockMarketInfo?=null
    companion object {
        fun newInstance(stockInfo: StockMarketInfo?): RemindSettingFragment {
            val fragment = RemindSettingFragment()
            if (stockInfo != null) {
                val bundle = Bundle()
                bundle.putSerializable("stockInfo", stockInfo)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_remind_setting

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: RemindSettingPresenter
        get() = RemindSettingPresenter()

    override val createViewModel: RemindSettingViewModel?
        get() = ViewModelProviders.of(this).get(RemindSettingViewModel::class.java)

    override val getView: RemindSettingView
        get() = this

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        stockInfo=arguments?.getSerializable("stockInfo") as StockMarketInfo?
        iv_back.setOnClickListener(this)
        textView.text=stockInfo?.name
        when (stockInfo?.ts) {
            StockTsEnum.HK.name -> {
                imageView.setImageResource(R.mipmap.ic_ts_hk)
            }
            StockTsEnum.SH.name -> {
                imageView.setImageResource(R.mipmap.ic_ts_sh)
            }
            StockTsEnum.SZ.name -> {
                imageView.setImageResource(R.mipmap.ic_ts_sz)
            }
        }
        textView2.text=stockInfo?.code
        // 跌涨幅是否大于0或者等于0
        val diffPriceVal = if (stockInfo?.diffPrice == null) 0 else MathUtil.rounded(stockInfo?.diffPrice!!).toInt()
        if (diffPriceVal == 0 || diffPriceVal > 0) {
            tv_price.setUpDownChange(true)
            tv_price.text = if (stockInfo?.price == null) "0.00" else stockInfo?.price.toString()
            tv_diff_price_count.text =  if (stockInfo?.diffPrice == null) "0.00" else stockInfo?.diffPrice.toString()
            tv_diff_rate_count.text = if (stockInfo?.diffRate == null) "0.00" else stockInfo?.diffRate .toString()
        } else {
            tv_price.setUpDownChange(false)
            tv_price.text = if (stockInfo?.price == null) "0.00" else stockInfo?.price.toString()
            tv_diff_price_count.text = stockInfo?.diffPrice.toString()
            tv_diff_rate_count.text = stockInfo?.diffRate.toString()
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back-> {
              pop()
            }
            else -> {
            }
        }
    }



}