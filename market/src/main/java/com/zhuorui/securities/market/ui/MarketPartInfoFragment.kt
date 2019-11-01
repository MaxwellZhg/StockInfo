package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketPartInfoBinding
import com.zhuorui.securities.market.ui.presenter.MarketPartInfoPresenter
import com.zhuorui.securities.market.ui.view.MarketPartInfoView
import com.zhuorui.securities.market.ui.viewmodel.MarketPartInfoViewModel
import kotlinx.android.synthetic.main.fragment_market_part_info.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/24
 * Desc:港股板块
 * */
class MarketPartInfoFragment :AbsSwipeBackNetFragment<FragmentMarketPartInfoBinding,MarketPartInfoViewModel,MarketPartInfoView,MarketPartInfoPresenter>(),MarketPartInfoView{
    private var type: Int? = null
    override val layout: Int
        get() = R.layout.fragment_market_part_info
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketPartInfoPresenter
        get() = MarketPartInfoPresenter()
    override val createViewModel: MarketPartInfoViewModel?
        get() = ViewModelProviders.of(this).get(MarketPartInfoViewModel::class.java)
    override val getView: MarketPartInfoView
        get() =this
  companion object{
      fun newInstance(type:Int):MarketPartInfoFragment{
          val fragment = MarketPartInfoFragment()
          if (type != null) {
              val bundle = Bundle()
              bundle.putSerializable("type", type)
              fragment.arguments = bundle
          }
          return fragment
      }
  }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        type = arguments?.getSerializable("type") as Int?
       when(type){
           1->{
               top_bar.setTitle("行业板块")
           }
           2->{
               top_bar.setTitle("概念板块")
           }
       }
    }
}
