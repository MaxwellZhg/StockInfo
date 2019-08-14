package com.dycm.modulea

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.dycm.modulea.model.PersonalInfo

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/13
 * Desc:
 */
 class ModuleaViewModel : BaseObservable() {
    private var info: PersonalInfo? = null

    private var introduction: String? = null


    fun getInfo(): PersonalInfo? {
        return info
    }

    fun setInfo(info: PersonalInfo) {
        this.info = info
        val introduction = "I'm " + info.name + ", " + info.gender + ", I like " + info.hobbies
        //setIntroduction(introduction)
    }

    @Bindable
    fun getIntroduction(): String? {
        return introduction
    }

    fun setIntroduction(introduction: String) {
        this.introduction = introduction
        notifyPropertyChanged(BR.introduction)
    }

}