package com.zhuorui.securities.openaccount.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.ZRUploadImageView
import com.zhuorui.commonwidget.dialog.GetPicturesModeDialog
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
import java.io.File

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
        get() = R.layout.fragment_oa_upload_documents

    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: OAUploadDocumentsPresenter
        get() = OAUploadDocumentsPresenter()
    override val createViewModel: OAUploadDocumentsViewModel?
        get() = ViewModelProviders.of(this).get(OAUploadDocumentsViewModel::class.java)
    override val getView: OAUploadDocumentsView
        get() = this

    override fun goCamera(requestCode: Int) {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        var uri : Uri?= null
//        var cameraSaveFile : File = File(listener?.getCameraSavePath())
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            uri = FileProvider.getUriForFile(context!!, "com.photocameratest2.fileprovider", cameraSaveFile)
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        } else {
//            uri = Uri.fromFile(cameraSaveFile)
//        }
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
//        startActivityForResult(intent, requestCode)
    }

    override fun goAlbum(requestCode: Int) {
        val intent = Intent()
        intent.action = Intent.ACTION_PICK
        intent.type = "image/*"
        startActivityForResult(intent,requestCode)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_next -> {
//                val intent = Intent()
//                intent.action = Intent.ACTION_PICK
//                intent.type = "image/*"
////                var reqCode : Int = listener?.getToAlbumRequestCode()!!
//                startActivityForResult(intent,1000)
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