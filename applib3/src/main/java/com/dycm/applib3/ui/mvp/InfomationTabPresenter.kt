package com.dycm.applib3.ui.mvp

import com.dycm.applib3.ui.contract.InfomationTabContract
import com.dycm.base2app.mvp.BasePresenter
import java.util.ArrayList

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