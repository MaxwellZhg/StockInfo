package com.zhuorui.securities.applib5.ui.contract

import com.zhuorui.securities.base2app.mvp.IBaseView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
interface MyTabContract{
    interface View : IBaseView {

    }

    interface Presenter {
        fun fetchData()
    }

    interface ViewWrapper {
        fun setData()
    }
}