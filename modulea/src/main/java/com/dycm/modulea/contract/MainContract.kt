package com.dycm.modulea.contract

import com.dycm.base2app.mvp.IBaseView
import com.dycm.modulea.model.PersonalInfo

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/12
 * Desc:
 */
interface MainContract {
    interface View : IBaseView {
        fun showPersonalInfoDialog(info: PersonalInfo)
    }

    interface Presenter {
        fun fetchData()
    }

    interface ViewWrapper {
        fun setData(infos: List<PersonalInfo>)
    }
}