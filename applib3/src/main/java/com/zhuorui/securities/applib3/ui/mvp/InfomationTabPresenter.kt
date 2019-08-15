package com.zhuorui.securities.applib3.ui.mvp

import com.zhuorui.securities.applib3.ui.contract.InfomationTabContract
import com.zhuorui.securities.base2app.mvp.BasePresenter

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class InfomationTabPresenter( view: InfomationTabContract.View) : BasePresenter<InfomationTabContract.View, InfomationTabContract.ViewWrapper>(),
    InfomationTabContract.Presenter {


    init {
        attachView(view)
    }

    override fun fetchData() {

        viewWrapper.setData()
    }

}