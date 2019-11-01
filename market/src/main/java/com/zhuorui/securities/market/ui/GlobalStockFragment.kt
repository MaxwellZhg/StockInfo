package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentGlobalStockBinding
import com.zhuorui.securities.market.ui.adapter.GlobalStockInfoTipsAdapter
import com.zhuorui.securities.market.ui.presenter.GlobalStockPresenter
import com.zhuorui.securities.market.ui.view.GlobalStockView
import com.zhuorui.securities.market.ui.viewmodel.GlobalStockViewModel
import kotlinx.android.synthetic.main.fragment_global_stock.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/29
 * Desc:全球指数
 * */
class GlobalStockFragment :AbsSwipeBackNetFragment<FragmentGlobalStockBinding,GlobalStockViewModel,GlobalStockView,GlobalStockPresenter>()
    ,GlobalStockView {
    private var coutomAdapter: GlobalStockInfoTipsAdapter? = null
    private var usaAdapter: GlobalStockInfoTipsAdapter? = null
    private var enuAdapter: GlobalStockInfoTipsAdapter? = null
    private var asiaAdapter: GlobalStockInfoTipsAdapter? = null
    override val layout: Int
        get() = R.layout.fragment_global_stock
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: GlobalStockPresenter
        get() = GlobalStockPresenter()
    override val createViewModel: GlobalStockViewModel?
        get() = ViewModelProviders.of(this).get(GlobalStockViewModel::class.java)
    override val getView: GlobalStockView
        get() = this


    companion object {
        fun newInstance(): GlobalStockFragment {
            return GlobalStockFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        presenter?.setLifecycleOwner(this)
        coutomAdapter = presenter?.getGlobalInfoTipsAdapter()
        usaAdapter = presenter?.getGlobalInfoTipsAdapter()
        enuAdapter = presenter?.getGlobalInfoTipsAdapter()
        asiaAdapter = presenter?.getGlobalInfoTipsAdapter()
        //解决数据加载不完的问题
        rv_coustom.isNestedScrollingEnabled = false
        rv_coustom.setHasFixedSize(true)
        rv_usa.isNestedScrollingEnabled = false
        rv_usa.setHasFixedSize(true)
        rv_enu.isNestedScrollingEnabled = false
        rv_enu.setHasFixedSize(true)
        rv_aisa.isNestedScrollingEnabled = false
        rv_aisa.setHasFixedSize(true)
        //解决数据加载完成后, 没有停留在顶部的问题
        rv_coustom.isFocusable = false
        rv_usa.isFocusable = false
        rv_enu.isFocusable = false
        rv_aisa.isFocusable = false
        rv_coustom.adapter=coutomAdapter
        rv_usa.adapter=usaAdapter
        rv_enu.adapter=enuAdapter
        rv_aisa.adapter=asiaAdapter
        presenter?.getData()
        scroll_global_stock_view.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            if(scrollY<tv_global_usa.top){
                tv_top_tips.setTipsData(1)
            }else if(scrollY>=tv_global_usa.top&&scrollY<tv_global_enu.top){
                tv_top_tips.setTipsData(2)
            }else if(scrollY>=tv_global_enu.top){
                tv_top_tips.setTipsData(3)
            }
        }
    }

    override fun addIntoCoustomData(list: List<Int>) {
        coutomAdapter?.clearItems()
        if (coutomAdapter?.items == null) {
            coutomAdapter?.items = ArrayList()
        }
        coutomAdapter?.addItems(list)
    }

    override fun addIntoUsaData(list: List<Int>) {
        usaAdapter?.clearItems()
        if (usaAdapter?.items == null) {
            usaAdapter?.items = ArrayList()
        }
        usaAdapter?.addItems(list)
    }

    override fun addIntoEnuData(list: List<Int>) {
        enuAdapter?.clearItems()
        if (enuAdapter?.items == null) {
            enuAdapter?.items = ArrayList()
        }
        enuAdapter?.addItems(list)
    }

    override fun addIntoAsiaData(list: List<Int>) {
        asiaAdapter?.clearItems()
        if (asiaAdapter?.items == null) {
            asiaAdapter?.items = ArrayList()
        }
        asiaAdapter?.addItems(list)
    }
}