package com.example.applib5.ui.mvp

import com.dycm.base2app.mvp.BasePresenter
import com.example.applib5.ui.contract.MyTabContract

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