package com.dycm.applib1.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dycm.applib.TopicStockSearchFragment
import com.dycm.applib1.R
import com.dycm.applib1.model.ChooseStockData
import com.dycm.applib1.model.StockMarketData
import com.dycm.base2app.adapter.BaseListAdapter
import com.dycm.base2app.ui.fragment.AbsBackFinishNetFragment
import com.dycm.base2app.ui.fragment.AbsFragment
import kotlinx.android.synthetic.main.fragment_all_choose_stock.*
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/7
 * Desc: 自选股列表界面
 */
class TopicStockListFragment : AbsBackFinishNetFragment(), BaseListAdapter.OnClickItemCallback<ChooseStockData>,
    TopicStocksAdapter.AddTopicCallback {

    private var type = 0
    private var list: ArrayList<ChooseStockData> = ArrayList()
    private var mAdapter: TopicStocksAdapter? = null

    companion object {

        const val ALL = 1 // 全部
        const val HK = 2 // 港股
        const val HS = 3 // 沪深

        fun newInstance(type: Int): TopicStockListFragment {
            val fragment = TopicStockListFragment()
            val bundle = Bundle()
            bundle.putInt("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_all_choose_stock

    override fun init() {
        type = arguments?.getInt("type")!!
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        // TODO 假数据
        list.add(ChooseStockData("恒生指数", "800000", "27498.77", "+3.41%", 1, 1))
        list.add(ChooseStockData("上证指数", "000001", "2901.77", "-3.41%", 2, 2))

        // 设置列表数据适配器
        rv_stock.layoutManager = LinearLayoutManager(context)
        mAdapter = TopicStocksAdapter(this)
        mAdapter?.setClickItemCallback(this)
        rv_stock.adapter = mAdapter
        mAdapter?.addItems(list)
        mAdapter?.addTopicCallback=this
    }

    override fun onClickItem(pos: Int, item: ChooseStockData?, v: View?) {
    }

    override fun addTopicCallback(pos: Int, item: ChooseStockData?, view: View) {
        (parentFragment as AbsFragment).start(TopicStockSearchFragment.newInstance(1))
    }

}