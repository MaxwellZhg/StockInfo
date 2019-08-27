package com.zhuorui.securities.openaccount.ui

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.ZRUploadImageView
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.GetPhotoFromAlbumUtil
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.databinding.FragmentOaUploadDocumentsBinding
import com.zhuorui.securities.openaccount.ui.presenter.OAUploadDocumentsPresenter
import com.zhuorui.securities.openaccount.ui.view.OAUploadDocumentsView
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
    OAUploadDocumentsView, View.OnClickListener,ZRUploadImageView.OnUploadImageListener {

    companion object {
        fun newInstance(): OAUploadDocumentsFragment {
            return OAUploadDocumentsFragment()
        }
    }

    override val layout: Int
        get() = com.zhuorui.securities.openaccount.R.layout.fragment_oa_upload_documents

    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: OAUploadDocumentsPresenter
        get() = OAUploadDocumentsPresenter()
    override val createViewModel: OAUploadDocumentsViewModel?
        get() = ViewModelProviders.of(this).get(OAUploadDocumentsViewModel::class.java)
    override val getView: OAUploadDocumentsView
        get() = this

    override fun goCamera(requestCode: Int, uri: Uri?) {
        GetPhotoFromAlbumUtil.goCamera(this,requestCode,uri)

    }

    override fun goAlbum(requestCode: Int) {
        GetPhotoFromAlbumUtil.goAlbum(this,requestCode)

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            com.zhuorui.securities.openaccount.R.id.btn_next -> {
                start(OAConfirmDocumentsFragment.newInstance())
            }
        }
    }

    override fun init() {
        btn_next.setOnClickListener(this)
        idcard_portrait.setOnUploadImageListener(this)
        idcard_national_emblem.setOnUploadImageListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        idcard_portrait.onActivityResult(requestCode,resultCode,data)
        idcard_national_emblem.onActivityResult(requestCode,resultCode,data)
    }

}