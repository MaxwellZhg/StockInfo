package com.zhuorui.securities.market.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.model.STPositionData
import java.text.DecimalFormat

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 17:24
 *    desc   :
 */
class HoldPositionsListAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_TITLE = 1
    val TYPE_EMPTY = 2
    val TYPE_ITEM = 3
    val context = context
    var vEmpty: TextView? = null
    var listener: HoldPositionsListListener? = null
    var types: Array<Int?> = arrayOfNulls(3)
    val selected: HashSet<Int> = HashSet()
    var posOff = 0
    var datas: MutableList<STPositionData>? = null

    init {
        datas = mutableListOf()
        initItemViewType()
    }


    private var vHeader: View? = null

    fun setHeaderView(v: View?) {
        vHeader = v
        initItemViewType()
    }

    fun setData(list: List<STPositionData>?) {
        datas?.clear()
        if (list != null) datas?.addAll(list)
        initItemViewType()
    }

    fun setEmptyMassge(msg: String) {
        vEmpty?.text = msg
    }

    private fun getDataPosition(position: Int): Int {
        return position - posOff
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
        val dataCount = if (getDataCount() == 0) 1 else getDataCount()
        return dataCount + 1 + if (vHeader == null) 0 else 1
    }

    private fun getDataCount(): Int {
        return datas?.size!!
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
                ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hold_positions_title, parent, false))
            }
            TYPE_EMPTY -> {
                ViewHolder(getEmptyView(parent))
            }
            else -> {
                val itemViewHolder =
                    ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hold_positions, parent, false))
                itemViewHolder.item?.setOnClickListener {
                    val pos: Int = it.tag as Int
                    if (selected.contains(pos)) {
                        selected.remove(pos)
                        datas?.get(pos)?.selected = false
                        itemViewHolder.btnGroup?.visibility = View.GONE
                    } else {
                        datas?.get(pos)?.selected = true
                        selected.add(pos)
                        itemViewHolder.btnGroup?.visibility = View.VISIBLE
                    }
                }
                itemViewHolder.business?.setOnClickListener {
                    val pos: Int = it.tag as Int
                    listener?.toBusiness("HP_business:$pos")
                }
                itemViewHolder.quotation?.setOnClickListener {
                    val pos: Int = it.tag as Int
                    listener?.toQuotation("HP_quotation:$pos")
                }
                itemViewHolder
            }
        }
    }

    private fun getEmptyView(parent: ViewGroup): View {
        val v = LayoutInflater.from(context).inflate(R.layout.item_empty_view, parent, false)
        vEmpty = v.findViewById(R.id.tv_msg)
        vEmpty?.text = ResUtil.getString(R.string.str_no_position_present)
        return v
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ITEM) {
            val itemHolder = (holder as ItemViewHolder)
            val dataPos = getDataPosition(position)
            val data = datas?.get(dataPos)!!
            if (data?.selected == null)
                data?.selected = selected.contains(dataPos)
            itemHolder.bindData(dataPos, data)
        }
    }

    class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var item: View? = null
        var btnGroup: View? = null
        var business: View? = null
        var quotation: View? = null
        var stockName: TextView? = null
        var marketValue: TextView? = null
        var presentPrice: TextView? = null
        var stockTsCode: TextView? = null
        var number: TextView? = null
        var cost: TextView? = null
        var profitAndLoss: TextView? = null
        var profitAndLossPercentage: TextView? = null
        val color = Color.parseColor("#D9001B")


        init {
            item = v.findViewById(R.id.item_bg)
            btnGroup = v.findViewById(R.id.btn_group)
            business = v.findViewById(R.id.tv_business)
            quotation = v.findViewById(R.id.tv_quotation)
            stockName = v.findViewById(R.id.tv_stock_name)
            marketValue = v.findViewById(R.id.tv_market_value)
            presentPrice = v.findViewById(R.id.tv_present_price)
            profitAndLoss = v.findViewById(R.id.tv_profit_and_loss)
            stockTsCode = v.findViewById(R.id.tv_stock_code)
            number = v.findViewById(R.id.tv_number)
            cost = v.findViewById(R.id.tv_cost)
            profitAndLossPercentage = v.findViewById(R.id.tv_profit_and_loss_percentage)
        }

        fun bindData(position: Int, data: STPositionData) {
            item?.tag = position
            business?.tag = position
            quotation?.tag = position
            stockName?.text = data?.stockName
            stockTsCode?.text = data?.stockCode + "." + data?.stockType
            marketValue?.text = data?.marketValue.toString()
            presentPrice?.text = data?.presentPrice.toString()
            number?.text = data?.holdStockCount.toString()
            cost?.text = data?.holeCost.toString()
            profitAndLoss?.text = data?.profitAndLoss.toString()
            btnGroup?.visibility = if (data?.selected!!) View.VISIBLE else View.GONE
            when {
                data?.profitAndLoss!!.toFloat() > 0 -> {
                    profitAndLoss?.setTextColor(color)
                    profitAndLossPercentage?.text = "+" + DecimalFormat("0.00%").format(data?.profitAndLossPercentage)
                    profitAndLossPercentage?.setTextColor(color)
                }
                data?.profitAndLoss!!.toFloat() < 0 -> {
                    profitAndLoss?.setTextColor(color)
                    profitAndLossPercentage?.text = "-" + DecimalFormat("0.00%").format(data?.profitAndLossPercentage)
                    profitAndLossPercentage?.setTextColor(color)
                }
                else -> {
                    profitAndLoss?.setTextColor(color)
                    profitAndLossPercentage?.text = "---"
                    profitAndLossPercentage?.setTextColor(color)
                }
            }

        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)


    interface HoldPositionsListListener {

        /**
         * 去买卖
         */
        fun toBusiness(id: String)

        /**
         * 去行情
         */
        fun toQuotation(id: String)
    }

}