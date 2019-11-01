package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.event.MarketDetailInfoEvent
import com.zhuorui.securities.market.ui.adapter.MarketInfoAdapter
import com.zhuorui.securities.market.ui.presenter.MarketDetailCapitalPresenter
import com.zhuorui.securities.market.ui.presenter.MarketDetailInformationPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailCapitalView
import com.zhuorui.securities.market.ui.view.MarketDetailInformationView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailCapitalViewModel
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailInformationViewModel
import kotlinx.android.synthetic.main.fragment_market_detail_information.*

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:51
 *    desc   :
 */
class MarketDetailInformationFragment :
    AbsSwipeBackNetFragment<FragmentMarketDetailBinding, MarketDetailInformationViewModel, MarketDetailInformationView, MarketDetailInformationPresenter>(),
    MarketDetailInformationView,View.OnClickListener {
    var infoAdapter: MarketInfoAdapter?=null
    override val layout: Int
        get() = R.layout.fragment_market_detail_information
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketDetailInformationPresenter
        get() = MarketDetailInformationPresenter()
    override val createViewModel: MarketDetailInformationViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailInformationViewModel::class.java)
    override val getView: MarketDetailInformationView
        get() = this

    companion object {
        fun newInstance(): MarketDetailInformationFragment {
            return MarketDetailInformationFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        presenter?.setLifecycleOwner(this)
        infoAdapter=presenter?.getInfoAdapter()
        presenter?.getInfoData()
        rv_market_info.adapter = infoAdapter
        tv_info.setOnClickListener(this)
        tv_report.setOnClickListener(this)
    }
    override fun addIntoInfoData(list: List<Int>) {
        infoAdapter?.clearItems()
        if (infoAdapter?.items == null) {
            infoAdapter?.items = ArrayList()
        }
        infoAdapter?.addItems(list)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_info->{
                RxBus.getDefault().post(MarketDetailInfoEvent(1))
                detailType(1)
            }
            R.id.tv_report->{
                RxBus.getDefault().post(MarketDetailInfoEvent(2))
                detailType(2)
            }
        }
    }
    override fun changeInfoTypeData(event: MarketDetailInfoEvent) {
           detailType(event.type)
    }

    fun detailType(type:Int){
        when(type){
            1->{
                ResUtil.getColor(R.color.color_53A0FD)?.let { tv_info.setTextColor(it) }
                tv_info.background=ResUtil.getDrawable(R.drawable.market_info_selected_bg)
                ResUtil.getColor(R.color.color_C0CCE0)?.let { tv_report.setTextColor(it) }
                tv_report.background=ResUtil.getDrawable(R.drawable.market_info_unselect_bg)
            }
            2->{
                ResUtil.getColor(R.color.color_53A0FD)?.let { tv_report.setTextColor(it) }
                tv_report.background=ResUtil.getDrawable(R.drawable.market_info_selected_bg)
                ResUtil.getColor(R.color.color_C0CCE0)?.let { tv_info.setTextColor(it) }
                tv_info.background=ResUtil.getDrawable(R.drawable.market_info_unselect_bg)
            }
        }
    }



}