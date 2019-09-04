package com.zhuorui.securities.openaccount.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.openaccount.ui.view.TakePhotoView
import com.zhuorui.securities.openaccount.ui.viewmodel.TakePhotoViewModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/2 18:21
 *    desc   :
 */
class TakePhotoPresenter : AbsPresenter<TakePhotoView, TakePhotoViewModel>() {

    /**
     * 是否拍摄了照片
     */
    fun takePhoto(takePhotoed: Boolean) {
        viewModel?.takePhotoed?.value = takePhotoed
    }
}