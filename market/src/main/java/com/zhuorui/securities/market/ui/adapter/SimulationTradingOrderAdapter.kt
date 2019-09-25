package com.zhuorui.securities.market.ui.adapter

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.model.STOrderData

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 17:24
 *    desc   :
 */
class SimulationTradingOrderAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_TITLE = 1
    private val TYPE_EMPTY = 2
    private val TYPE_ITEM = 3
    private val context = context
    private var vEmpty: TextView? = null
    private var mEmptyMsg: String? = null
    private var types: Array<Int?> = arrayOfNulls(3)
    private val selected: HashSet<Int> = HashSet()
    private var posOff = 0
    var datas: MutableList<STOrderData>? = null
    var listener: MockStockOrderListener? = null
    var canClick = true

    private var vHeader: View? = null

    init {
        datas = mutableListOf()
        initItemViewType()
    }

    fun setHeaderView(v: View?) {
        vHeader = v
        initItemViewType()
    }

    fun clear() {
        datas?.clear()
        initItemViewType()
    }

    fun addDatas(list: List<STOrderData>?) {
        if (list != null)
            datas?.addAll(list)
        initItemViewType()
    }

    fun setEmptyMassge(msg: String) {
        mEmptyMsg = msg
        vEmpty?.text = mEmptyMsg
    }

    private fun initItemViewType() {
        types[2] = if (getDataCount() == 0) TYPE_EMPTY else TYPE_ITEM
        if (vHeader != null) {
            types[0] = TYPE_HEADER
            types[1] = TYPE_TITLE
            posOff = 2
        } else {
            types[0] = TYPE_TITLE
            types[1] = types[2]
            posOff = 1
        }
    }

    override fun getItemCount(): Int {
        return 1 + (if (getDataCount() == 0) 1 else getDataCount()) + (if (vHeader == null) 0 else 1)
    }

    private fun getDataCount(): Int {
        return datas?.size!!
    }

    private fun getDataPosition(position: Int): Int {
        return position - posOff
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
                ViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_simulation_trading_order_title,
                        parent,
                        false
                    )
                )
            }
            TYPE_EMPTY -> {
                ViewHolder(getEmptyView(parent))
            }
            else -> {
                val itemViewHolder =
                    ItemViewHolder(
                        LayoutInflater.from(context).inflate(
                            R.layout.item_simulation_trading_order,
                            parent,
                            false
                        )
                    )
                if (canClick) {
                    itemViewHolder.item?.setOnClickListener {
                        val pos: Int = it.tag as Int
                        if (selected.contains(pos)) {
                            itemViewHolder.hideBtn()
                            selected.remove(pos)
                            datas?.get(pos)?.selected = false
                        } else {
                            itemViewHolder.showBtn()
                            selected.add(pos)
                            datas?.get(pos)?.selected = true
                        }
                    }
                    itemViewHolder.business?.setOnClickListener {
                        val pos: Int = it.tag as Int
                        listener?.toBusiness(datas!![pos])
                    }
                    itemViewHolder.quotation?.setOnClickListener {
                        val pos: Int = it.tag as Int
                        listener?.toQuotation("Quotation:$pos")
                    }
                    itemViewHolder.orderQuotation?.setOnClickListener {
                        val pos: Int = it.tag as Int
                        listener?.toQuotation("OrderQuotation$pos")
                    }
                    itemViewHolder.change?.setOnClickListener {
                        val pos: Int = it.tag as Int
                        listener?.toChangeOrder(datas!![pos])
                    }
                    itemViewHolder.cancel?.setOnClickListener {
                        val pos: Int = it.tag as Int
                        listener?.toCancelOrder(datas!![pos].id!!)
                    }
                }
                itemViewHolder.canClick = canClick
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
            val dataPos = getDataPosition(position)
            val data = datas?.get(dataPos)!!
            if (data?.selected == null) data?.selected = selected.contains(dataPos)
            itemHolder.bindData(dataPos, data)

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
        var orderType: TextView? = null
        var orderStatus: TextView? = null
        var stockName: TextView? = null
        var stockTsCode: TextView? = null
        var number: TextView? = null
        var presentPrice: TextView? = null
        var orderDate: TextView? = null
        var orderTime: TextView? = null
        var menuType: Int = 0 //0 无菜单 1订单菜单 2购买菜单
        var canClick: Boolean = true

        init {
            item = v.findViewById(R.id.item_bg)
            orderBtnGroup = v.findViewById(R.id.order_btn_group)
            businessBtnGroup = v.findViewById(R.id.business_btn_group)
            business = v.findViewById(R.id.tv_business)
            quotation = v.findViewById(R.id.tv_quotation)
            orderQuotation = v.findViewById(R.id.tv_order_quotation)
            change = v.findViewById(R.id.tv_change)
            cancel = v.findViewById(R.id.tv_cancel)
            orderType = v.findViewById(R.id.tv_order_type)
            orderStatus = v.findViewById(R.id.tv_order_status)
            stockName = v.findViewById(R.id.tv_stock_name)
            stockTsCode = v.findViewById(R.id.tv_stock_code)
            number = v.findViewById(R.id.tv_number)
            presentPrice = v.findViewById(R.id.tv_present_price)
            orderDate = v.findViewById(R.id.tv_order_date)
            orderTime = v.findViewById(R.id.tv_order_time)
        }

        fun bindData(position: Int, data: STOrderData) {
            stockName?.text = data.stockName
            stockTsCode?.text = data.code + "." + data.ts
            number?.text = data.holdStockCount.toString()
            presentPrice?.text = data.holeCost.toString()
//            orderDate?.text = TimeZoneUtil.timeFormat(data.createTime!!, "yyyy-MM-ddTHH:mm:ss","MM-dd")
//            orderTime?.text = TimeZoneUtil.timeFormat(data.createTime!!, "yyyy-MM-ddTHH:mm:ss","HH:mm:ss")
            item?.tag = position
            business?.tag = position
            quotation?.tag = position
            orderQuotation?.tag = position
            change?.tag = position
            cancel?.tag = position
            orderType?.text = data?.trustName
            orderType?.setTextColor(data?.trustColor!!)
            orderStatus?.text = data?.statusName
            orderStatus?.setCompoundDrawablesWithIntrinsicBounds(data?.statusLogo!!, 0, 0, 0)
            if (canClick) {
                val status = data?.status
                menuType = if (status == 1 || status == 3) {
                    1
                } else if (status == 4 || status == 5 || status == 6) {
                    2
                } else {
                    0
                }
                if (data?.selected!!) {
                    showBtn()
                } else {
                    hideBtn()
                }
            }
        }

        fun showBtn() {
            when (menuType) {
                1 -> {
                    orderBtnGroup?.visibility = View.VISIBLE
                    businessBtnGroup?.visibility = View.GONE
                }
                2 -> {
                    orderBtnGroup?.visibility = View.GONE
                    businessBtnGroup?.visibility = View.VISIBLE
                }
                else -> {
                    hideBtn()
                }
            }
        }

        fun hideBtn() {
            orderBtnGroup?.visibility = View.GONE
            businessBtnGroup?.visibility = View.GONE
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    interface MockStockOrderListener : HoldPositionsListAdapter.HoldPositionsListListener {
        /**
         * 改单
         */
        fun toChangeOrder(data: STOrderData)

        /**
         * 撤单
         */
        fun toCancelOrder(id: String)
    }

}