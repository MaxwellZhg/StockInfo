package com.zhuorui.securities.market.ui.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.commonwidget.ZrCompareTextView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.config.LocalSearchConfig
import com.zhuorui.securities.market.event.ChageSearchTabEvent
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/23
 * Desc:
 */
class SearchStockInfoAdapter(str:String) : BaseListAdapter<SearchStockInfo>(){
    private val default = 0x00
    private val bottom = 0x01
    private var str:String = str
    var onTopicStockInfoListener: OnTopicStockInfoListener? =null
    override fun getLayout(viewType: Int): Int {
        return when (viewType) {
            default -> R.layout.item_stock_search_layout
            else -> {
                R.layout.item_search_stock_tips_more
            }
        }
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            default -> ViewHolder(v, false, false)
            else -> {
                ViewHolderBottom(v, true, false)
            }
        }
    }


   inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean) :
    ListItemViewHolder<SearchStockInfo>(v, needClick, needLongClick) {
       @BindView(R2.id.tv_stock_info_name)
       lateinit var tv_stock_info_name: ZrCompareTextView
       @BindView(R2.id.iv_stock_logo)
       lateinit var iv_stock_logo: AppCompatImageView
       @BindView(R2.id.tv_stock_code)
       lateinit var tv_stock_code: ZrCompareTextView
       @BindView(R2.id.iv_topic)
       lateinit var iv_topic: AppCompatImageView
       @BindView(R2.id.rl_stock)
       lateinit var rl_stock: ConstraintLayout
       override fun bind(item: SearchStockInfo?, position: Int) {
            tv_stock_info_name.setText(item?.name,str)
            tv_stock_code.setText(item?.code,str)
           when (item?.ts) {
               "SH" -> {
                   iv_stock_logo.background = ResUtil.getDrawable(R.mipmap.ic_ts_sh)
               }
               "SZ" -> {
                   iv_stock_logo.background = ResUtil.getDrawable(R.mipmap.ic_ts_sz)
               }
               "HK" -> {
                   iv_stock_logo.background = ResUtil.getDrawable(R.mipmap.ic_ts_hk)
               }
           }
           when(item?.collect){
               true->{
                   iv_topic.background=ResUtil.getDrawable(R.mipmap.icon_stock_topiced)
               }
               false->{
                   iv_topic.background=ResUtil.getDrawable(R.mipmap.ic_topic_history_d)
               }
           }

       }
       init {
           iv_topic.setOnClickListener(this)
           rl_stock.setOnClickListener(this)
       }

       override fun onClick(v: View) {
           if (v == iv_topic) {
               getItem(position)?.let { onTopicStockInfoListener?.topicStockInfo(it) }
           }else if(v == rl_stock){
               getItem(position)?.let { LocalSearchConfig.getInstance().add(it) }
               ToastUtil.instance.toastCenter("加入历史记录")
           } else{
               super.onClick(v)
           }
       }
   }

    inner class ViewHolderBottom(v: View?, needClick: Boolean, needLongClick: Boolean):
        ListItemViewHolder<SearchStockInfo>(v, needClick, needLongClick) {
        @BindView(R2.id.rl_content)
        lateinit var rl_content: ConstraintLayout

        init {
            rl_content.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v == rl_content) {
                LogUtils.e("ttttttt")
                RxBus.getDefault().post(ChageSearchTabEvent(SearchStokcInfoEnum.Stock))
            } else {
                super.onClick(v)
            }
        }
           override fun bind(item: SearchStockInfo?, position: Int) {

           }

      }

    override fun getItemViewType(position: Int): Int {
        return if (items.size > 5) {
            default
        } else if (items.size == 5) {
            when (position) {
                itemCount - 1 -> {
                    bottom
                }
                else -> {
                    default
                }
            }
        } else {
            default
        }
    }

    override fun getItemCount(): Int {
        return when {
            items == null -> return 0
            items.size > 5 -> items.size
            items.size == 5 -> items.size + 1
            else -> items.size
        }
    }

    override fun getItem(position: Int): SearchStockInfo? {
        if (position > items.size || position == items.size) return null
        return super.getItem(position)
    }

    interface OnTopicStockInfoListener{
        fun topicStockInfo(stockInfo:SearchStockInfo)
    }


}