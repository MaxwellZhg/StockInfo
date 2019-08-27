package com.zhuorui.securities.openaccount.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaTakeBankCardPhotoBinding
import com.zhuorui.securities.openaccount.ui.presenter.OATakeBankCradPhotoPresenter
import com.zhuorui.securities.openaccount.ui.view.OATakeBankCradPhotoView
import com.zhuorui.securities.openaccount.ui.viewmodel.OATakeBankCradPhotoViewModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/26 17:08
 *    desc   :
 */
class OATakeBankCradPhotoFragment :
    AbsFragment<FragmentOaTakeBankCardPhotoBinding, OATakeBankCradPhotoViewModel, OATakeBankCradPhotoView, OATakeBankCradPhotoPresenter>(),
    OATakeBankCradPhotoView {

    override val layout: Int
        get() = R.layout.fragment_oa_take_bank_card_photo

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OATakeBankCradPhotoPresenter
        get() = OATakeBankCradPhotoPresenter()

    override val createViewModel: OATakeBankCradPhotoViewModel?
        get() = ViewModelProviders.of(this).get(OATakeBankCradPhotoViewModel::class.java)

    override val getView: OATakeBankCradPhotoView
        get() = this
}