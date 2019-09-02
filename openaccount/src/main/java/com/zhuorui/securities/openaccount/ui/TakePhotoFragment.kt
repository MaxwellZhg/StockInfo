package com.zhuorui.securities.openaccount.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentTakePhotoBinding
import com.zhuorui.securities.openaccount.ui.presenter.TakePhotoPresenter
import com.zhuorui.securities.openaccount.ui.view.TakePhotoView
import com.zhuorui.securities.openaccount.ui.viewmodel.TakePhotoViewModel

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/9/2 17:47
 * desc   : 拍摄身份证、银行卡页面
 */
class TakePhotoFragment :
    AbsFragment<FragmentTakePhotoBinding, TakePhotoViewModel, TakePhotoView, TakePhotoPresenter>(),
    TakePhotoView {

    companion object {
        fun newInstance(): TakePhotoFragment {
            return TakePhotoFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_take_photo

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: TakePhotoPresenter
        get() = TakePhotoPresenter()

    override val createViewModel: TakePhotoViewModel?
        get() = ViewModelProviders.of(this).get(TakePhotoViewModel::class.java)

    override val getView: TakePhotoView
        get() = this
}
