package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentStockDetailIndexBinding
import com.zhuorui.securities.market.ui.kline.ChartOneDayFragment
import com.zhuorui.securities.market.ui.kline.KlineFragment
import com.zhuorui.securities.market.ui.presenter.StockDetailIndexPresenter
import com.zhuorui.securities.market.ui.view.StockDetailIndexView
import com.zhuorui.securities.market.ui.viewmodel.StockDetailIndexViewModel
import kotlinx.android.synthetic.main.fragment_stock_detail_index.*
import me.yokeyword.fragmentation.SupportFragment


/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-11-04 15:11
 *    desc   : 指数Fragment
 */
class StockDetailIndexFragment :
    AbsFragment<FragmentStockDetailIndexBinding, StockDetailIndexViewModel, StockDetailIndexView, StockDetailIndexPresenter>(),
    StockDetailIndexView {

    private var mCode: String = ""
    private var mTs: String = ""
    private var mTsCode: String = ""
    private var mType: Int = 0

    companion object {
        fun newInstance(code: String, ts: String, tsCode: String, type: Int): StockDetailIndexFragment {
            val b = Bundle()
            b.putString("code", code)
            b.putString("ts", ts)
            b.putString("tsCode", tsCode)
            b.putInt("type", type)
            val fragment = StockDetailIndexFragment()
            fragment.arguments = b
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_stock_detail_index

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: StockDetailIndexPresenter
        get() = StockDetailIndexPresenter()

    override val createViewModel: StockDetailIndexViewModel?
        get() = ViewModelProviders.of(this).get(StockDetailIndexViewModel::class.java)

    override val getView: StockDetailIndexView
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = arguments!!
        mTs = b.getString("ts")!!
        mCode = b.getString("code")!!
        mTsCode = b.getString("tsCode")!!
        mType = b.getInt("type")!!

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        //ChartOneDayFragment
        loadRootFragment(
            R.id.fl_kline,
            ChartOneDayFragment.newInstance(mTs, mCode, mTsCode, mType, 3)
        )
        v_kline_click.setOnClickListener {
            (parentFragment as SupportFragment).start(MarketPointFragment.newInstance(1))
        }
    }


}