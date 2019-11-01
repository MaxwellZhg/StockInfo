package com.zhuorui.securities.market.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.adapter.AllHkStockContainerAdapter
import com.zhuorui.securities.market.ui.adapter.AllHkStockNameAdapter
import com.zhuorui.securities.market.ui.presenter.AllHkStockPresenter
import com.zhuorui.securities.market.ui.view.AllHkStockView
import com.zhuorui.securities.market.ui.viewmodel.AllHkStockViewModel
import kotlinx.android.synthetic.main.fragment_all_hk_stock.*
import kotlinx.android.synthetic.main.layout_filters_hk_stock_info.*
import com.zhuorui.securities.market.databinding.FragmentAllHkStockBinding
/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/24
 * Desc:全部港股
 * */
class AllHkStockFragment :
    AbsSwipeBackNetFragment<FragmentAllHkStockBinding, AllHkStockViewModel, AllHkStockView, AllHkStockPresenter>(),
    AllHkStockView, View.OnClickListener {
    private var type: Int? = null
    private var nameAdapter: AllHkStockNameAdapter? = null
    private var conAdapter: AllHkStockContainerAdapter? = null
    private var showFilters = false
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
        fun newInstance(type :Int): AllHkStockFragment {
            val fragment = AllHkStockFragment()
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
                top_bar.setTitle("全部港股")
            }
            2->{
                top_bar.setTitle("主板港股")
            }
            3->{
                top_bar.setTitle("创业板港股")
            }
        }
        top_bar.setRight2ClickListener {
            // 搜索
            start(SearchInfoFragment.newInstance())
        }
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
        tv_filters.setOnClickListener(this)
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_filters -> {
                val values = IntArray(2)
                if (showFilters) {
                    values[0] =  ResUtil.getDimensionDp2Px(330f)
                    values[1] = 0
                    showFilters = false
                } else {
                    values[0] = 0
                    values[1] = ResUtil.getDimensionDp2Px(330f)
                    showFilters = true
                }
                val valueAnimator = ValueAnimator.ofInt(values[0], values[1])
                valueAnimator.duration = 150
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    val layoutParams = ll_pick_tips.layoutParams
                    layoutParams.height = value
                    ll_pick_tips?.layoutParams = layoutParams
                }
                valueAnimator.start()
            }
        }
    }


}