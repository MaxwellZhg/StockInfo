package com.zhuorui.securities.personal.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.personal.model.MessageViewModel
import com.zhuorui.securities.personal.ui.model.MessageModel
import com.zhuorui.securities.personal.ui.view.MessageView

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/5 11:23
 *    desc   :
 */
class MessagePresenter : AbsNetPresenter<MessageView, MessageViewModel>() {

    override fun init() {
        super.init()
        view?.init()
        // 加载数据
        loadData()
    }

    private fun loadData() {
        // TODO 造假数据
        val data = ArrayList<MessageModel>()
        data.add(MessageModel("尊敬的客户，6月7日（周五）端午节，港股及A股通休市一日。港股休市期间，资金和股票的相关服务暂停处理。如有疑问，可咨询在线客服或电话1234567"))
        data.add(MessageModel("您好！您的开户申请已开始处理。预计1个工作日完成处理。届时请留意短信邮件等通知。"))
        view?.notifyDataSetChanged(data)
    }
}