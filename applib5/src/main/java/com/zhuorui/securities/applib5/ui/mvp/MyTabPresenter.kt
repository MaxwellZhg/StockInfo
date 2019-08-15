package com.zhuorui.securities.applib5.ui.mvp

import com.zhuorui.securities.base2app.mvp.BasePresenter
import com.zhuorui.securities.applib5.ui.contract.MyTabContract

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class MyTabPresenter(view: MyTabContract.View): BasePresenter<MyTabContract.View, MyTabContract.ViewWrapper>(),
    MyTabContract.Presenter{
    init {
        attachView(view)
    }
    override fun fetchData() {

    }

}