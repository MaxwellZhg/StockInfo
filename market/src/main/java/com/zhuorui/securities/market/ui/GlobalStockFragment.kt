package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentGlobalStockBinding
import com.zhuorui.securities.market.model.GlobalStockInfo
import com.zhuorui.securities.market.ui.adapter.GlobalStockInfoAdapter
import com.zhuorui.securities.market.ui.presenter.GlobalStockPresenter
import com.zhuorui.securities.market.ui.view.GlobalStockView
import com.zhuorui.securities.market.ui.viewmodel.GlobalStockViewModel
import kotlinx.android.synthetic.main.fragment_global_stock.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/29
 * Desc:
 */
class GlobalStockFragment :AbsSwipeBackNetFragment<FragmentGlobalStockBinding,GlobalStockViewModel,GlobalStockView,GlobalStockPresenter>(),GlobalStockView{
    private var globalAdapter: GlobalStockInfoAdapter? = null
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
        globalAdapter = presenter?.getGlobalInfoAdapter()
        rv_data.adapter = globalAdapter
        presenter?.getData()
    }
    override fun addIntoData(list: List<GlobalStockInfo>) {
        globalAdapter?.clearItems()
        if (globalAdapter?.items == null) {
            globalAdapter?.items = ArrayList()
        }
        globalAdapter?.addItems(list)
    }
}