package com.zhuorui.securities.openaccount.ui

import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaAuthenticationBinding
import com.zhuorui.securities.openaccount.databinding.FragmentOaUploadDocumentsBinding
import com.zhuorui.securities.openaccount.ui.presenter.OAAuthenticationPresenter
import com.zhuorui.securities.openaccount.ui.presenter.OAUploadDocumentsPresenter
import com.zhuorui.securities.openaccount.ui.view.OAAuthenticationView
import com.zhuorui.securities.openaccount.ui.view.OAUploadDocumentsView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAAuthenticationViewModel
import com.zhuorui.securities.openaccount.ui.viewmodel.OAUploadDocumentsViewModel
import kotlinx.android.synthetic.main.fragment_oa_upload_documents.*

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-23 14:09
 *    desc   : 上传身份证信息
 */
class OAUploadDocumentsFragment :
    AbsFragment<FragmentOaUploadDocumentsBinding, OAUploadDocumentsViewModel, OAUploadDocumentsView, OAUploadDocumentsPresenter>(),
    OAUploadDocumentsView, View.OnClickListener {

    companion object {
        fun newInstance(): OAUploadDocumentsFragment {
            return OAUploadDocumentsFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_upload_documents

    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: OAUploadDocumentsPresenter
        get() = OAUploadDocumentsPresenter()
    override val createViewModel: OAUploadDocumentsViewModel?
        get() = ViewModelProviders.of(this).get(OAUploadDocumentsViewModel::class.java)
    override val getView: OAUploadDocumentsView
        get() = this

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_next -> {
                start(OAConfirmDocumentsFragment.newInstance())
            }
        }
    }

    override fun init() {
        btn_next.setOnClickListener(this)
    }

}