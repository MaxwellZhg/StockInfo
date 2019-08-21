package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaDataTipsBinding
import com.zhuorui.securities.openaccount.model.OADataTips
import com.zhuorui.securities.openaccount.ui.presenter.OADataTipsPresenter
import com.zhuorui.securities.openaccount.ui.view.OADataTipsView
import com.zhuorui.securities.openaccount.ui.viewmodel.OADataTipsViewModel
import kotlinx.android.synthetic.main.fragment_oa_data_tips.*


/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:01
 *    desc   : 提示用户准备开户资料
 */

class OADataTipsFragment :
    AbsSwipeBackNetFragment<FragmentOaDataTipsBinding, OADataTipsViewModel, OADataTipsView, OADataTipsPresenter>(),
    OADataTipsView,
    CompoundButton.OnCheckedChangeListener {

    private var mAdapter: OADataTipsAdapter? = null

    override fun init() {
        cbox.setOnCheckedChangeListener(this)
        presenter?.setLifecycleOwner(this)
        var txt = getAgreementText()
        agreement.text = txt;
        agreement.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun getAgreementText(): SpannableString {
        var afr = "开户协议"
        var text = "本人已仔细阅读并同意签署全部"
        var spannableString = SpannableString(text + afr)
        spannableString.setSpan(
            AgreementClickableSpan(),
            text.length,
            spannableString?.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.app_bule)),
            text.length,
            spannableString?.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    class AgreementClickableSpan() : ClickableSpan() {
        override fun onClick(p0: View) {
            ToastUtil.instance.toast("点击开户协议")
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ds.linkColor
            ds.isUnderlineText = false
        }

    }


    companion object {
        fun newInstance(): OADataTipsFragment {
            return OADataTipsFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_data_tips

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OADataTipsPresenter
        get() = OADataTipsPresenter()

    override val createViewModel: OADataTipsViewModel?
        get() = ViewModelProviders.of(this).get(OADataTipsViewModel::class.java)

    override val getView: OADataTipsView
        get() = this

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        open_btn?.isEnabled = p1;
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        (rv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        rv.layoutManager = LinearLayoutManager(context)
        mAdapter = OADataTipsAdapter()
        rv.adapter = mAdapter;
        presenter?.getDataTips()
    }

    override fun notifyDataSetChanged(list: List<OADataTips>?) {
        mAdapter?.addItems(list)
    }

}
