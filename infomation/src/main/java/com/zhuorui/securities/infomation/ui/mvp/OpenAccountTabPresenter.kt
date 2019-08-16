package com.zhuorui.securities.infomation.ui.mvp

import com.zhuorui.securities.infomation.ui.contract.OpenAccountTabContract
import com.zhuorui.securities.base2app.mvp.BasePresenter

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class OpenAccountTabPresenter (view: OpenAccountTabContract.View): BasePresenter<OpenAccountTabContract.View, OpenAccountTabContract.ViewWrapper>(),
    OpenAccountTabContract.Presenter{
    init {
        attachView(view)
    }
    override fun fetchData() {
        viewWrapper.setData()
    }

}