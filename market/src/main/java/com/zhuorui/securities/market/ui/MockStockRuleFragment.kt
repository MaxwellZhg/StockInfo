package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMockStockRuleBinding
import com.zhuorui.securities.market.ui.presenter.MockStockRulePresenter
import com.zhuorui.securities.market.ui.view.MockStockRuleView
import com.zhuorui.securities.market.ui.viewmodel.MockStockRuleViewModel
import kotlinx.android.synthetic.main.fragment_mock_stock_rule.*

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 14:05
 *    desc   : 模拟炒股规则
 */
class MockStockRuleFragment :
    AbsSwipeBackNetFragment<FragmentMockStockRuleBinding, MockStockRuleViewModel, MockStockRuleView, MockStockRulePresenter>(),
    MockStockRuleView {


    companion object {
        fun newInstance(): MockStockRuleFragment {
            return MockStockRuleFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_mock_stock_rule

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: MockStockRulePresenter
        get() = MockStockRulePresenter()

    override val createViewModel: MockStockRuleViewModel?
        get() = ViewModelProviders.of(this).get(MockStockRuleViewModel::class.java)

    override val getView: MockStockRuleView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        rule.text = getRuleText()
        rule.movementMethod = LinkMovementMethod.getInstance()
    }

    fun getRuleText(): SpannableString {
        var afr = ResUtil.getString(R.string.str_click_view)
        var text = rule.text.toString()
        var spannableString = SpannableString(text)
        val s = text.indexOf(afr!!)
        val e = s + afr.length
        spannableString.setSpan(AgreementClickableSpan(), s, e, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        var color = ResUtil.getColor(R.color.color_1A6ED2)
        spannableString.setSpan(ForegroundColorSpan(color!!), s, e, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    class AgreementClickableSpan() : ClickableSpan() {
        override fun onClick(p0: View) {
            ToastUtil.instance.toast("点击查看")
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ds.linkColor
            ds.isUnderlineText = false
        }

    }


}