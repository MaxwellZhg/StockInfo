package com.zhuorui.securities.openaccount.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.openaccount.model.OADataTips
import com.zhuorui.securities.openaccount.ui.view.OADataTipsView
import com.zhuorui.securities.openaccount.ui.view.OASeletRegionView
import com.zhuorui.securities.openaccount.ui.viewmodel.OADataTipsViewModel
import com.zhuorui.securities.openaccount.ui.viewmodel.OASelectRegonViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:19
 *    desc   :
 */
class OADataTipsPresenter : AbsNetPresenter<OADataTipsView, OADataTipsViewModel>() {
    override fun init() {
        super.init()
        view?.init();
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.datas?.observe(it,
                androidx.lifecycle.Observer<List<OADataTips>> { t -> view?.notifyDataSetChanged(t) })
        }
    }

    fun getDataTips(){
        val datas = mutableListOf<OADataTips>()
        datas.add(OADataTips(1))
        datas.add(OADataTips(2))
        datas.add(OADataTips(3))
        viewModel?.datas?.value = datas
    }
}