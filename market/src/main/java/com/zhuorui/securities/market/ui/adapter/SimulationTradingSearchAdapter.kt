package com.zhuorui.securities.market.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.model.SearchStockInfo

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 17:24
 *    desc   :
 */
class SimulationTradingSearchAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_FOOTER = 1
    val TYPE_EMPTY = 2
    val TYPE_ITEM = 3
    val context = context
    var vEmpty: TextView? = null
    var mEmptyMsg: String? = null
    var types: Array<Int?> = arrayOfNulls(2)
    var listener: OnSimulationTradingSearchListener? = null
    var datas: MutableList<SearchStockInfo>? = null
    var posOff = 0


    private var vHeader: View? = null
    private var vFooter: View? = null


    init {
        datas = mutableListOf()
        initItemViewType()
    }

    fun setHeaderView(v: View?) {
        vHeader = v
        initItemViewType()
    }

    fun setFooterView(v: View?) {
        vFooter = v
        initItemViewType()
    }

    fun setData(list: MutableList<SearchStockInfo>?) {
        datas?.clear()
        list?.let { datas?.addAll(it) }
        initItemViewType()
    }

    fun setEmptyMassge(msg: String) {
        mEmptyMsg = msg
        vEmpty?.text = mEmptyMsg
    }

    private fun initItemViewType() {
        types[1] = if (getDataCount() == 0) TYPE_EMPTY else TYPE_ITEM
        types[0] = if (vHeader != null) TYPE_HEADER else types[1]
        posOff = if (vHeader == null) 0 else 1

    }

    override fun getItemCount(): Int {
        val dataCount = if (getDataCount() == 0) 1 else getDataCount()
        val hnum = if (vHeader == null) 0 else 1
        val fnum = if (getDataCount() == 0 || vFooter == null) 0 else 1
        return dataCount + hnum + fnum
    }

    private fun getDataCount(): Int {
        return datas?.size!!
    }

    private fun getDataPosition(position: Int): Int {
        return position - posOff
    }

    override fun getItemViewType(position: Int): Int {
        return if (getDataCount() != 0 && vFooter != null && position == itemCount - 1) {
            TYPE_FOOTER
        } else {
            types[if (position > 1) 1 else position]!!
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                ViewHolder(vHeader!!)
            }
            TYPE_FOOTER -> {
                ViewHolder(vFooter!!)
            }
            TYPE_EMPTY -> {
                ViewHolder(getEmptyView(parent))
            }
            else -> {
                val itemViewHolder =
                    ItemViewHolder(
                        LayoutInflater.from(context).inflate(
                            R.layout.item_simulation_trading_search,
                            parent,
                            false
                        )
                    )
                itemViewHolder.itemView?.setOnClickListener {
                    val pos: Int = it.tag as Int
                    val data = datas?.get(getDataPosition(pos))
                    listener?.onItemClick(data!!)
                }
                itemViewHolder
            }
        }
    }

    private fun getEmptyView(parent: ViewGroup): View {
        val v = LayoutInflater.from(context).inflate(R.layout.item_empty_view, parent, false)
        vEmpty = v.findViewById(R.id.tv_msg)
        vEmpty?.text = if (TextUtils.isEmpty(mEmptyMsg)) ResUtil.getString(R.string.str_no_data) else mEmptyMsg
        return v
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ITEM) {
            val itemHolder = (holder as ItemViewHolder)
            itemHolder.itemView?.tag = position
            val data = datas?.get(getDataPosition(position))
            itemHolder.bindData(data!!)
        }
    }

    class ItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var name: TextView? = null
        var code: TextView? = null

        init {
            name = itemView.findViewById(R.id.tv_name)
            code = itemView.findViewById(R.id.tv_code)
        }

        fun bindData(data: SearchStockInfo) {
            name?.text = data.name
            code?.text = data.tsCode
        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    interface OnSimulationTradingSearchListener {
        fun onItemClick(stocks: SearchStockInfo)
    }


}