package com.zhuorui.securities.market.ui

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 17:24
 *    desc   :
 */
class MockStockOrderAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_TITLE = 1
    val TYPE_EMPTY = 2
    val TYPE_ITEM = 3
    val context = context
    var vEmpty: TextView? = null
    var mEmptyMsg: String? = null

    var types: Array<Int?> = arrayOfNulls(3)
    val selected: HashSet<Int> = HashSet()
    var listener: MockStockOrderListener? = null


    private var vHeader: View? = null


    init {
        initItemViewType()
    }

    fun setHeaderView(v: View?) {
        vHeader = v
        initItemViewType()
    }

    fun addData() {
        initItemViewType()
    }

    fun setEmptyMassge(msg: String) {
        mEmptyMsg = msg
        vEmpty?.text = mEmptyMsg
    }

    private fun initItemViewType() {
        types[0] = if (vHeader != null) TYPE_HEADER else TYPE_TITLE
        types[1] = if (types[0] == TYPE_HEADER) TYPE_TITLE else if (getDataCount() == 0) TYPE_EMPTY else TYPE_ITEM
        types[2] = if (types[1] != TYPE_TITLE) types[1] else if (getDataCount() == 0) TYPE_EMPTY else TYPE_ITEM
    }

    override fun getItemCount(): Int {
        val dataCount = if (getDataCount() == 0) 1 else getDataCount()
        return dataCount + 1 + if (vHeader == null) 0 else 1
    }

    private fun getDataCount(): Int {
        return 50
    }

    override fun getItemViewType(position: Int): Int {
        return types[if (position > 2) 2 else position]!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                ViewHolder(vHeader!!)
            }
            TYPE_TITLE -> {
                ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mock_stock_order_title, parent, false))
            }
            TYPE_EMPTY -> {
                ViewHolder(getEmptyView(parent))
            }
            else -> {
                val itemViewHolder =
                    ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mock_stock_order, parent, false))
                itemViewHolder.item?.setOnClickListener {
                    val pos: Int = it.tag as Int
                    if (selected.contains(pos)) {
                        itemViewHolder.hideBtn(pos)
                        selected.remove(pos)
                    } else {
                        itemViewHolder.showBtn(pos)
                        selected.add(pos)
                    }
                }
                itemViewHolder.business?.setOnClickListener {
                    val pos:Int = it.tag as Int
                    listener?.toBusiness("Business:$pos") }
                itemViewHolder.quotation?.setOnClickListener {
                    val pos:Int = it.tag as Int
                    listener?.toQuotation("Quotation:$pos") }
                itemViewHolder.orderQuotation?.setOnClickListener {
                    val pos:Int = it.tag as Int
                    listener?.toQuotation("OrderQuotation$pos") }
                itemViewHolder.change?.setOnClickListener {
                    val pos:Int = it.tag as Int
                    listener?.toChangeOrder("change$pos") }
                itemViewHolder.cancel?.setOnClickListener {
                    val pos:Int = it.tag as Int
                    listener?.toCancelOrder("CancelOrder:$pos") }
                itemViewHolder
            }
        }
    }

    private fun getEmptyView(parent: ViewGroup): View {
        val v = LayoutInflater.from(context).inflate(R.layout.item_empty_view, parent, false)
        vEmpty = v.findViewById(R.id.tv_msg)
        vEmpty?.text = if (TextUtils.isEmpty(mEmptyMsg)) ResUtil.getString(R.string.str_no_order) else mEmptyMsg
        return v
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ITEM) {
            val itemHolder = (holder as ItemViewHolder)
            itemHolder.item?.tag = position
            itemHolder.business?.tag = position
            itemHolder.quotation?.tag = position
            itemHolder.orderQuotation?.tag = position
            itemHolder.change?.tag = position
            itemHolder.cancel?.tag = position
            if (selected.contains(position)) {
                itemHolder.showBtn(position)
            } else {
                itemHolder.hideBtn(position)
            }
        }
    }

    class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var item: View? = null
        var orderBtnGroup: View? = null
        var businessBtnGroup: View? = null
        var business: View? = null
        var quotation: View? = null
        var orderQuotation: View? = null
        var change: View? = null
        var cancel: View? = null

        init {
            item = v.findViewById(R.id.item_bg)
            orderBtnGroup = v.findViewById(R.id.order_btn_group)
            businessBtnGroup = v.findViewById(R.id.business_btn_group)
            business = v.findViewById(R.id.tv_business)
            quotation = v.findViewById(R.id.tv_quotation)
            orderQuotation = v.findViewById(R.id.tv_order_quotation)
            change = v.findViewById(R.id.tv_change)
            cancel = v.findViewById(R.id.tv_cancel)
        }


        fun bindData(position: Int) {

        }

        fun showBtn(pos: Int) {
            if (pos % 2 == 0) {
                orderBtnGroup?.visibility = View.VISIBLE
                businessBtnGroup?.visibility = View.GONE
            } else {
                orderBtnGroup?.visibility = View.GONE
                businessBtnGroup?.visibility = View.VISIBLE
            }
        }

        fun hideBtn(pos: Int) {
            orderBtnGroup?.visibility = View.GONE
            businessBtnGroup?.visibility = View.GONE
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    interface MockStockOrderListener : HoldPositionsListAdapter.HoldPositionsListListener {
        /**
         * 改单
         */
        fun toChangeOrder(id: String)

        /**
         * 撤单
         */
        fun toCancelOrder(id: String)
    }

}