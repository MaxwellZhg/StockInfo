package com.zhuorui.securities.personal.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.R2
import com.zhuorui.securities.personal.ui.model.MessageModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/5 11:36
 *    desc   : 消息列表适配器
 */
class MessageAdapter : BaseListAdapter<MessageModel>() {

    override fun getLayout(viewType: Int): Int {
        return R.layout.item_message
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v, false, false)
    }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<MessageModel>(v, needClick, needLongClick) {

        @BindView(R2.id.tv_time)
        lateinit var tv_time: TextView
        @BindView(R2.id.tv_msg)
        lateinit var tv_msg: TextView

        override fun bind(item: MessageModel?, position: Int) {
            tv_msg.text = item?.msg
        }
    }
}