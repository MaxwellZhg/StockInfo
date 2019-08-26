package com.zhuorui.securities.openaccount.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaConfirmDocumentsBinding
import com.zhuorui.securities.openaccount.databinding.FragmentOaUploadDocumentsBinding
import com.zhuorui.securities.openaccount.ui.presenter.OAConfirmDocumentsPresenter
import com.zhuorui.securities.openaccount.ui.presenter.OAUploadDocumentsPresenter
import com.zhuorui.securities.openaccount.ui.view.OAConfirmDocumentsView
import com.zhuorui.securities.openaccount.ui.view.OAUploadDocumentsView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAConfirmDocumentsViewModel
import com.zhuorui.securities.openaccount.ui.viewmodel.OAUploadDocumentsViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-23 14:09
 *    desc   : 确认身份信息
 */
class OAConfirmDocumentsFragment :
    AbsFragment<FragmentOaConfirmDocumentsBinding, OAConfirmDocumentsViewModel, OAConfirmDocumentsView, OAConfirmDocumentsPresenter>(),
    OAConfirmDocumentsView {

    companion object {
        fun newInstance(): OAConfirmDocumentsFragment {
            return OAConfirmDocumentsFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_confirm_documents

    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: OAConfirmDocumentsPresenter
        get() = OAConfirmDocumentsPresenter()
    override val createViewModel: OAConfirmDocumentsViewModel?
        get() = ViewModelProviders.of(this).get(OAConfirmDocumentsViewModel::class.java)
    override val getView: OAConfirmDocumentsView
        get() = this

    override fun init() {

    }

}