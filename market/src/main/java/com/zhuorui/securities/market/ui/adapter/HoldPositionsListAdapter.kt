package com.zhuorui.securities.market.ui.adapter

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.model.STOrderData
import com.zhuorui.securities.market.model.STPositionData
import com.zhuorui.securities.market.ui.SimulationTradingStocksFragment
import java.text.DecimalFormat

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 17:24
 *    desc   :
 */
class HoldPositionsListAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_TITLE = 1
    private val TYPE_EMPTY = 2
    private val TYPE_ITEM = 3
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

    fun clearSelectd() {
        selected.clear()
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
                ViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.item_hold_positions_title,
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
                        LayoutInflater.from(
                            context
                        ).inflate(R.layout.item_hold_positions, parent, false)
                    )
                itemViewHolder.item?.setOnClickListener {
                    val pos: Int = it.tag as Int
                    if (selected.contains(pos)) {
                        itemViewHolder.btnGroup?.visibility = View.GONE
                        selected.remove(pos)
                        datas?.get(pos)?.selected = false
                        listener?.onItemClick(posOff + pos, false)
                    } else {
                        itemViewHolder.btnGroup?.visibility = View.VISIBLE
                        datas?.get(pos)?.selected = true
                        selected.add(pos)
                        listener?.onItemClick(posOff + pos, true)
                    }
                }
                itemViewHolder.business?.setOnClickListener {
                    val pos: Int = it.tag as Int
                    hideMu(itemViewHolder, pos)
                    listener?.toBusiness(SimulationTradingStocksFragment.TRAD_TYPE_DEFAULT, datas!![pos])
                }
                itemViewHolder.quotation?.setOnClickListener {
                    val pos: Int = it.tag as Int
                    val data = datas!![pos]
                    hideMu(itemViewHolder, pos)
                    listener?.toQuotation(
                        data.code.toString(),
                        data.ts.toString(),
                        data.getTsCode(),
                        data.stockName.toString()
                    )
                }
                itemViewHolder
            }
        }
    }

    private fun hideMu(itemViewHolder: ItemViewHolder, pos: Int) {
        itemViewHolder.btnGroup?.visibility = View.GONE
        selected.remove(pos)
        datas?.get(pos)?.selected = false
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
            if (data.selected == null)
                data.selected = selected.contains(dataPos)
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
        val color1 = Color.parseColor("#232323")
        val color2 = Color.parseColor("#A1A1A1")
        val upColor = LocalSettingsConfig.getInstance().getUpColor()
        val downColor = LocalSettingsConfig.getInstance().getDownColor()


        init {
            (v.findViewById(R.id.root_view) as ViewGroup).setLayoutTransition(LayoutTransition())
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
            stockName?.text = data.stockName
            stockTsCode?.text = data.code + "." + data.ts
            marketValue?.text = data.marketValue.toString()
            presentPrice?.text = data.currentPrice.toString()
            number?.text = data.holdStockCount.toString()
            cost?.text = data.unitCost.toString()
            profitAndLoss?.text = data.profitAndLoss.toString()
            btnGroup?.visibility = if (data.selected!!) View.VISIBLE else View.GONE
            when {
                data.profitAndLoss != null && data.profitAndLoss!!.toFloat() > 0 -> {
                    profitAndLoss?.setTextColor(upColor)
                    profitAndLossPercentage?.text = "+" + DecimalFormat("0.00###%").format(data.profitAndLossPercentage)
                    profitAndLossPercentage?.setTextColor(upColor)
                }
                data.profitAndLoss != null && data.profitAndLoss!!.toFloat() < 0 -> {
                    profitAndLoss?.setTextColor(downColor)
                    profitAndLossPercentage?.text = DecimalFormat("0.00###%").format(data.profitAndLossPercentage)
                    profitAndLossPercentage?.setTextColor(downColor)
                }
                else -> {
                    profitAndLoss?.setTextColor(color1)
                    profitAndLossPercentage?.text = "---"
                    profitAndLossPercentage?.setTextColor(color2)
                }
            }

        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)


    interface HoldPositionsListListener {

        /**
         * 去买卖
         */
        fun toBusiness(type: Int, data: STOrderData)

        /**
         * 去行情
         */
        fun toQuotation(code: String, ts: String, tsCode: String, name: String)

        /**
         * 选择监听
         */
        fun onItemClick(position: Int, selected: Boolean)
    }

}