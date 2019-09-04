package com.zhuorui.securities.openaccount.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.GetPicturesModeDialog
import com.zhuorui.commonwidget.dialog.OptionsPickerDialog
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.base2app.util.GetPhotoFromAlbumUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.custom.TakePhotoBankCardTipsDialog
import com.zhuorui.securities.openaccount.databinding.FragmentOaTakeBankCardPhotoBinding
import com.zhuorui.securities.openaccount.ui.presenter.OATakeBankCradPhotoPresenter
import com.zhuorui.securities.openaccount.ui.view.OATakeBankCradPhotoView
import com.zhuorui.securities.openaccount.ui.viewmodel.OATakeBankCradPhotoViewModel
import com.zhuorui.securities.openaccount.widget.BankCardTextWatcher
import com.zhuorui.securities.pickerview.option.OnOptionSelectedListener
import kotlinx.android.synthetic.main.fragment_oa_take_bank_card_photo.*
import kotlinx.android.synthetic.main.fragment_oa_take_bank_card_photo.btn_next
import kotlinx.android.synthetic.main.fragment_oa_upload_documents.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/26 17:08
 *    desc   : 拍摄银行卡页面
 */
class OATakeBankCradPhotoFragment :
    AbsSwipeBackFragment<FragmentOaTakeBankCardPhotoBinding, OATakeBankCradPhotoViewModel, OATakeBankCradPhotoView, OATakeBankCradPhotoPresenter>(),
    OATakeBankCradPhotoView, View.OnClickListener, OnOptionSelectedListener<String>,
    GetPicturesModeDialog.OnGetPicturesModeListener {

    var dialog: GetPicturesModeDialog? = null
    var loading: ProgressDialog? = null

    companion object {
        fun newInstance(): OATakeBankCradPhotoFragment {
            return OATakeBankCradPhotoFragment()
        }
    }

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

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        tv_take_sample.setOnClickListener(this)
        tv_bank.vEt.setOnClickListener(this)
        tv_card_id?.vRightIcon?.setOnClickListener(this)
        btn_next.setOnClickListener(this)

        tv_card_id.vEt.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
        tv_card_id.vEt.filters = arrayOf(InputFilter.LengthFilter(26))
        tv_card_id.vEt.maxLines = 1
        BankCardTextWatcher.bind(tv_card_id.vEt)
    }

    override fun onBankOcrSuccess(bankCardNo: String, bankCardName: String) {
        tv_card_id.text = bankCardNo
        tv_bank.text = bankCardName
    }

    override fun showUpLoading() {
        if (loading == null) {
            loading = context?.let { ProgressDialog(it) }
        }
        loading?.show()
    }

    override fun hideUpLoading() {
        loading?.dismiss()
    }

    override fun getBankCardNo(): String {
        return tv_card_id.text
    }

    override fun getBankName(): String {
       return tv_bank.text
    }

    override fun showToast(message: String?) {
        ToastUtil.instance.toast(message.toString())
    }

    /**
     * 返回图片地址（调用dialog的回调方法处理，此方法才会有结果返回）
     */
    override fun onPicturePath(path: String?) {
        presenter?.bankOcr(path)
    }

    /**
     * 返回图片bitmap 调用dialog的回调方法处理，此方法才会有结果返回）
     */
    override fun onPictureBitmap(bm: Bitmap?) {
        presenter?.bankOcr(bm)
    }

    /**
     * 去拍照
     */
    override fun goCamera(toCameraRequestCode: Int?, uri: Uri?) {
        startForResult(TakePhotoFragment.newInstance(TakePhotoFragment.BANK_CRAD), toCameraRequestCode!!)
    }

    /**
     * 去相册
     */
    override fun goAlbum(toAlbumRequestCode: Int?) {
        GetPhotoFromAlbumUtil.goAlbum(this, toAlbumRequestCode!!)
    }

    override fun toNext() {
        // 跳转到下一步
        start(OAPersonalInformationFragment.newInstance())
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btn_next -> {
                presenter?.bankCardVerification()
            }
            tv_bank.vEt -> {
                // 选择银行
                val dialog = context?.let { OptionsPickerDialog<String>(it) }
                val regionData: MutableList<String> =
                    mutableListOf("中国工商银行", "中国农业银行", "中国银行", "中国建设银行", "中国邮政储蓄银行", "交通银行", "招商银行")
                dialog?.setData(regionData)
                dialog?.setOnOptionSelectedListener(this)
                dialog?.show()
            }
            tv_card_id.vRightIcon -> {
                // 选取银行卡照片方式
                if (dialog == null) {
                    dialog = context?.let { GetPicturesModeDialog(it) }
                    dialog?.listener = this
                }
                dialog?.show()
            }
            else -> {
                // 拍照示例
                context?.let { TakePhotoBankCardTipsDialog(it).show() }
            }
        }
    }

    override fun onOptionSelected(data: MutableList<String>?) {
        tv_bank.vEt.text = data?.get(0)
    }


    /**
     * 调用系统返回
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        dialog?.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 调用APP返回
     */
    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        dialog?.onFragmentResult(requestCode, resultCode, data)
    }

}