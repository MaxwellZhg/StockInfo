package com.dycm.modulea.mvp

import com.dycm.base2app.mvp.BasePresenter
import com.dycm.modulea.contract.MainContract
import com.dycm.modulea.model.PersonalInfo
import java.util.ArrayList

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/12
 * Desc:
 */
class MainPresenter( view: MainContract.View) : BasePresenter<MainContract.View, MainContract.ViewWrapper>(),
    MainContract.Presenter {


   init {
       attachView(view)
   }

    override fun fetchData() {
        val infos = ArrayList<PersonalInfo>()
        infos.add(PersonalInfo("Jack", "male", "basketball"))
        infos.add(PersonalInfo("Yann", "male", "football"))
        infos.add(PersonalInfo("Mary", "female", "badminton"))
        infos.add(PersonalInfo("Robson", "male", "foosball"))
        infos.add(PersonalInfo("Selina", "female", "tennis"))
        viewWrapper.setData(infos)
    }

}