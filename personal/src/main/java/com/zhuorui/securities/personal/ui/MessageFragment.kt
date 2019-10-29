package com.zhuorui.securities.personal.ui

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.FragmentMessageBinding
import com.zhuorui.securities.personal.model.MessageViewModel
import com.zhuorui.securities.personal.ui.adapter.MessageAdapter
import com.zhuorui.securities.personal.ui.model.MessageModel
import com.zhuorui.securities.personal.ui.presenter.MessagePresenter
import com.zhuorui.securities.personal.ui.view.MessageView
import kotlinx.android.synthetic.main.fragment_message.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/5 11:19
 *    desc   : 消息界面
 */
class  MessageFragment :
    AbsSwipeBackNetFragment<FragmentMessageBinding, MessageViewModel, MessageView, MessagePresenter>(), MessageView {

    var mAdapter: MessageAdapter? = null

    companion object {
        fun newInstance(): MessageFragment {
            return MessageFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_message

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: MessagePresenter
        get() = MessagePresenter()

    override val createViewModel: MessageViewModel?
        get() = ViewModelProviders.of(this).get(MessageViewModel::class.java)

    override val getView: MessageView
        get() = this

    override fun init() {
        // 初始化界面
        message_list.layoutManager = LinearLayoutManager(context)
        mAdapter = MessageAdapter()
        message_list.adapter = mAdapter
    }

    override fun notifyDataSetChanged(list: List<MessageModel>?) {
        if (mAdapter?.items == null) {
            mAdapter?.items = list
        } else {
            mAdapter?.notifyDataSetChanged()
        }
    }
}