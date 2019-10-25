package com.zhuorui.securities.market.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.adapter.AllHkStockContainerAdapter
import com.zhuorui.securities.market.ui.adapter.AllHkStockNameAdapter
import com.zhuorui.securities.market.ui.presenter.AllHkStockPresenter
import com.zhuorui.securities.market.ui.view.AllHkStockView
import com.zhuorui.securities.market.ui.viewmodel.AllHkStockViewModel
import kotlinx.android.synthetic.main.fragment_all_hk_stock.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/24
 * Desc:
 */
class AllHkStockFragment :
    AbsSwipeBackNetFragment<com.zhuorui.securities.market.databinding.FragmentAllHkStockBinding, AllHkStockViewModel, AllHkStockView, AllHkStockPresenter>(),
    AllHkStockView {

    private var nameAdapter: AllHkStockNameAdapter? = null
    private var conAdapter: AllHkStockContainerAdapter? = null
    override val layout: Int
        get() = R.layout.fragment_all_hk_stock
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: AllHkStockPresenter
        get() = AllHkStockPresenter()
    override val createViewModel: AllHkStockViewModel?
        get() = ViewModelProviders.of(this).get(AllHkStockViewModel::class.java)
    override val getView: AllHkStockView
        get() = this

    companion object {
        fun newInstance(): AllHkStockFragment {
            return AllHkStockFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        requireActivity().layoutInflater.inflate(R.layout.table_right_title, right_title_container)
        presenter?.setLifecycleOwner(this)
        nameAdapter = presenter?.getAllHkStockNameAdapter()
        conAdapter = presenter?.getAllHkStockContainerAdapter()
        right_container_rv.setHasFixedSize(true)
        presenter?.getAllHkStockNameData()
        presenter?.getAllHkStockContentData()
        left_container_rv.adapter = nameAdapter
        right_container_rv.adapter = conAdapter
        title_horsv.setScrollView(content_horsv)
        content_horsv.setScrollView(title_horsv)
    }

    override fun addIntoAllHkStockName(list: List<Int>) {
        nameAdapter?.clearItems()
        if (nameAdapter?.items == null) {
            nameAdapter?.items = ArrayList()
        }
        nameAdapter?.addItems(list)
    }

    override fun addIntoAllHkContainer(list: List<Int>) {
        conAdapter?.clearItems()
        if (conAdapter?.items == null) {
            conAdapter?.items = ArrayList()
        }
        conAdapter?.addItems(list)
    }


}