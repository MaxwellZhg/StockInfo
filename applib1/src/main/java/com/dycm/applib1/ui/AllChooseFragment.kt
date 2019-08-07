package com.dycm.applib1.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dycm.applib1.R
import com.dycm.applib1.model.ChooseStockData
import com.dycm.base2app.adapter.BaseListAdapter
import com.dycm.base2app.ui.fragment.AbsBackFinishNetFragment
import kotlinx.android.synthetic.main.fragment_all_choose_stock.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/7
 * Desc:
 */
class AllChooseFragment (var type:Int): AbsBackFinishNetFragment(), BaseListAdapter.OnClickItemCallback<ChooseStockData>{

    private var list :ArrayList<ChooseStockData> = ArrayList()
    var mAdapter: ChooseStockAdapter? = null
    override val layout: Int
        get() = R.layout.fragment_all_choose_stock

    override fun init() {
        list.clear()
        list.add(ChooseStockData("恒生指数","800000","27498.77","+3.41%",1,1))
        list.add(ChooseStockData("上证指数","000001","2901.77","-3.41%",2,2))
        rv_stock.layoutManager = LinearLayoutManager(context)
        mAdapter = ChooseStockAdapter(this)
        mAdapter?.setClickItemCallback(this)
        rv_stock.adapter = mAdapter
        mAdapter?.addItems(list)

    }
    override fun onClickItem(pos: Int, item: ChooseStockData?, v: View?) {

    }

}