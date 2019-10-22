package com.zhuorui.securities.openaccount.ui

import android.Manifest
import android.content.Intent
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.qw.soul.permission.SoulPermission
import com.qw.soul.permission.bean.Permission
import com.qw.soul.permission.bean.Permissions
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener
import com.zhuorui.commonwidget.ZRUploadImageView
import com.zhuorui.commonwidget.dialog.ConfirmToCancelDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.GetPhotoFromAlbumUtil
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.camera.CameraHelper
import com.zhuorui.securities.openaccount.custom.UploadDocumentsTipsDialog
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
    AbsSwipeBackNetFragment<FragmentOaUploadDocumentsBinding, OAUploadDocumentsViewModel, OAUploadDocumentsView, OAUploadDocumentsPresenter>(),
    OAUploadDocumentsView, View.OnClickListener, ZRUploadImageView.OnUploadImageListener {

    var sampleSialog: UploadDocumentsTipsDialog? = null

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

    override fun goCamera(requestCode: Int, uri: Uri?) {
        startForResult(TakePhotoFragment.newInstance(TakePhotoFragment.ID_CRAD), requestCode)
    }

    override fun goAlbum(requestCode: Int) {
        GetPhotoFromAlbumUtil.goAlbum(this, requestCode)
    }

    override fun setCardFrontUrl(cardFrontPhoto: String?) {
        idcard_portrait.setUrl(cardFrontPhoto)
    }

    override fun cardBackPhotoUrl(cardBackPhoto: String?) {
        idcard_national_emblem.setUrl(cardBackPhoto)
    }

    override fun onPicturePath(v: ZRUploadImageView?, path: String?) {
        checkUpload()
    }

    override fun onUploadError(v: ZRUploadImageView?, code: String?, msg: String?) {
        checkUpload()
        if (!TextUtils.equals("000301",code)){//000301 当天调用次数已用完
            showTipsDialog(getString(R.string.str_upload_err))
        }
    }

    private fun checkUpload() {
        var idPath1: String? = idcard_portrait.path
        var idPath2: String? = idcard_national_emblem.path
        btn_next.isEnabled = !TextUtils.isEmpty(idPath1) && !TextUtils.isEmpty(idPath2)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            btn_next.id -> {
                start(OAConfirmDocumentsFragment.newInstance())
            }
            btn_sample.id -> {
                showTipsDialog(btn_sample.text)
            }
        }
    }

    private fun showTipsDialog(text: CharSequence?) {
        if (sampleSialog == null) {
            sampleSialog = context?.let { UploadDocumentsTipsDialog(it) }
        }
        sampleSialog?.show(text.toString())
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        btn_next.setOnClickListener(this)
        btn_sample.setOnClickListener(this)
        idcard_portrait.setOnUploadImageListener(this)
        idcard_national_emblem.setOnUploadImageListener(this)
        idcard_portrait.setUploader(presenter?.getUploader(0))
        idcard_national_emblem.setUploader(presenter?.getUploader(1))
        presenter?.setDefData()
    }

    /**
     * 调用系统返回
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        idcard_portrait.onActivityResult(requestCode, resultCode, data)
        idcard_national_emblem.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 调用APP返回
     */
    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        idcard_portrait.onFragmentResult(requestCode, resultCode, data)
        idcard_national_emblem.onFragmentResult(requestCode, resultCode, data)
    }
}