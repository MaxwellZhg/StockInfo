package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.activity.AbsActivity
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaWaitAuditBinding
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.ui.presenter.OAWaitAuditPresenter
import com.zhuorui.securities.openaccount.ui.view.OAWaitAuditView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAWaitAuditViewModel
import kotlinx.android.synthetic.main.fragment_oa_wait_audit.*
import me.yokeyword.fragmentation.ISupportFragment

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/28 17:23
 *    desc   : 等待审核/审核不通过页面
 */
class OAWaitAuditFragment :
    AbsSwipeBackFragment<FragmentOaWaitAuditBinding, OAWaitAuditViewModel, OAWaitAuditView, OAWaitAuditPresenter>(),
    OAWaitAuditView, View.OnClickListener {
    private var errMsg:String? = null

    companion object {
        fun newInstance(): OAWaitAuditFragment {
            return OAWaitAuditFragment()
        }

        fun newInstance(errMsg: String): OAWaitAuditFragment {
            val fragment = OAWaitAuditFragment()
            val b = Bundle()
            b.putString("errMsg", errMsg)
            fragment.arguments = b
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_wait_audit

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OAWaitAuditPresenter
        get() = OAWaitAuditPresenter()

    override val createViewModel: OAWaitAuditViewModel?
        get() = ViewModelProviders.of(this).get(OAWaitAuditViewModel::class.java)

    override val getView: OAWaitAuditView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        errMsg = arguments?.getString("errMsg")
        if (TextUtils.isEmpty(errMsg)) {
            iv_wait_audit.setImageResource(R.mipmap.ic_wait_audit)
            tv_wait_audit.setText(R.string.str_wait_audit)
            str_wait_audit_tips.setText(R.string.str_wait_audit_tips)
            return_to_main.setText(R.string.btn_return_to_main)
        } else {
            iv_wait_audit.setImageResource(R.mipmap.ic_warnings_red)
            tv_wait_audit.setText(R.string.str_audit_failed)
            str_wait_audit_tips.text = ResUtil.getString(R.string.str_reasons_fail_tips) + errMsg
            return_to_main.setText(R.string.btn_edit_open_data)
        }
        return_to_main.setOnClickListener(this)
        top_bar.setBackClickListener {
            back()
        }
    }

    override fun onClick(p0: View?) {
        if (p0 == return_to_main) {
            if (TextUtils.isEmpty(errMsg)) {
                back()
            } else {
                val fragment = OpenInfoManager.getInstance().getFailStartFragment()
                if (fragment != null)
                    startWithPop(fragment)
            }
        }

    }

    override fun onBackPressedSupport(): Boolean {
        back()
        return true
    }

    fun back() {
        if (TextUtils.isEmpty(errMsg)) {
            // 返回首页
            val homeFragment = (activity as AbsActivity).supportFragmentManager.fragments[0] as AbsFragment<*, *, *, *>
            popTo(homeFragment::class.java, false)
        } else {
            pop()
        }

    }
}