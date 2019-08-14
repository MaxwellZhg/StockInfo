package com.dycm.applib2.contract

import com.dycm.base2app.mvp.IBaseView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
interface MarketTabContract {
    interface View : IBaseView {

    }

    interface Presenter {
        fun fetchData()
    }

    interface ViewWrapper {
        fun setData()
    }
}