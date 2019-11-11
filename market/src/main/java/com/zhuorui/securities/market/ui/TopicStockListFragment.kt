package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zhuorui.commonwidget.dialog.ConfirmToCancelDialog
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.StockPopupWindow
import com.zhuorui.securities.market.databinding.FragmentAllChooseStockBinding
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.StockTsEnum
import com.zhuorui.securities.market.ui.adapter.TopicStocksAdapter
import com.zhuorui.securities.market.ui.presenter.TopicStockListPresenter
import com.zhuorui.securities.market.ui.view.TopicStockListView
import com.zhuorui.securities.market.ui.viewmodel.TopicStockListViewModel
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.ui.LoginRegisterFragment
import kotlinx.android.synthetic.main.fragment_all_choose_stock.*
import kotlinx.android.synthetic.main.layout_guide_open_accout.*
import kotlinx.android.synthetic.main.layout_topic_stock_list_empty.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/7
 * Desc: 自选股列表界面
 */
class TopicStockListFragment :
    AbsFragment<FragmentAllChooseStockBinding, TopicStockListViewModel, TopicStockListView, TopicStockListPresenter>(),
    BaseListAdapter.OnClickItemCallback<StockMarketInfo>, View.OnClickListener,
    TopicStockListView, BaseListAdapter.onLongClickItemCallback<StockMarketInfo> {

    private var mAdapter: TopicStocksAdapter? = null

    companion object {
        fun newInstance(type: StockTsEnum?): TopicStockListFragment {
            val fragment = TopicStockListFragment()
            if (type != null) {
                val bundle = Bundle()
                bundle.putSerializable("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_all_choose_stock

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: TopicStockListPresenter
        get() = TopicStockListPresenter()

    override val createViewModel: TopicStockListViewModel?
        get() = ViewModelProviders.of(this).get(TopicStockListViewModel::class.java)

    override val getView: TopicStockListView
        get() = this

    override fun init() {
        val type = arguments?.getSerializable("type") as StockTsEnum?
        if (type == null && !LocalAccountConfig.getInstance().isLogin()) {
            guide_open_accout.inflate()
            tv_register_now.setOnClickListener(this)
        }
        if (type == StockTsEnum.HS) {
            root_view.addView(View.inflate(context, R.layout.layout_trans_index, null), 0)
        }
        presenter?.setType(type)

        // 设置列表数据适配器
        (rv_stock.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        rv_stock.layoutManager = LinearLayoutManager(context)
        mAdapter = TopicStocksAdapter()
        mAdapter?.setClickItemCallback(this)
        mAdapter?.setLongClickItemCallback(this)
        rv_stock.adapter = mAdapter
        btn_add_stotcks.setOnClickListener(this)
        rv_stock.setEmptyView(list_empty_view)
    }

    override fun onClickItem(pos: Int, item: StockMarketInfo?, v: View?) {
        if (item != null) {
            // 跳转到详情页
            val stock = SearchStockInfo()
            stock.code = item.code
            stock.ts = item.ts
            stock.tsCode = item.code+"."+item.ts
            stock.name = item.name
            stock.type = 2
//          startActivity(Intent(context, KlineLandActivity::class.java))
            (parentFragment as AbsFragment<*, *, *, *>).startWithPopTo(
                MarketDetailFragment.newInstance(stock),
                MarketDetailFragment::class.java,
                true
            )
        } else {
            // 跳转到搜索
            (parentFragment as AbsFragment<*, *, *, *>).start(SearchInfoFragment.newInstance())
        }
    }

    override fun onLongClickItem(pos: Int, item: StockMarketInfo?, view: View?) {
        item?.longClick = true
        mAdapter?.notifyItemChanged(pos)

        // 获取需要在其上方显示的控件的位置信息
        val location = IntArray(2)
        view?.getLocationOnScreen(location)
        // 显示更多操作
        context?.let {
            StockPopupWindow.create(it, object : StockPopupWindow.CallBack {
                override fun onStickyOnTop() {
                    presenter?.onStickyOnTop(item)
                }

                override fun onRemind() {
                    (parentFragment as AbsFragment<*, *, *, *>).start(RemindSettingFragment.newInstance(item))
                }

                override fun onDelete() {
                    context?.let { context ->
                        // 显示确定删除对话框
                        ConfirmToCancelDialog.createWidth250Dialog(context, false, true)
                            .setMsgText(R.string.delete_topic_stock_tips)
                            .setCancelText(R.string.cancle)
                            .setConfirmText(R.string.ensure)
                            .setCallBack(object : ConfirmToCancelDialog.CallBack {
                                override fun onCancel() {

                                }

                                override fun onConfirm() {
                                    presenter?.onDeleteStock(item)
                                }
                            })
                            .show()
                    }
                }

                override fun onDismiss() {
                    item?.longClick = false
                    mAdapter?.notifyItemChanged(pos)
                }
            }).showAtLocation(view, Gravity.TOP, location[0], location[1] + ResUtil.getDimensionDp2Px(12f))
        }
    }

    override fun notifyDataSetChanged(list: List<StockMarketInfo>?) {
        _mActivity?.runOnUiThread {
            if (mAdapter?.items == null) {
                mAdapter?.items = list
            } else {
                mAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun notifyItemChanged(index: Int) {
        _mActivity?.runOnUiThread { mAdapter?.notifyItemChanged(index) }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_register_now -> {
                (parentFragment as AbsFragment<*, *, *, *>).start(LoginRegisterFragment.newInstance(1))
            }
            R.id.btn_add_stotcks -> {
                // 跳转到搜索
                (parentFragment as AbsFragment<*, *, *, *>).start(SearchInfoFragment.newInstance())
            }
        }
    }

    override fun hideRegisterNow() {
        _mActivity?.runOnUiThread { viewInfalatedRootId?.visibility = View.GONE }
    }
}