package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.ConfirmDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaOhterNotesBinding
import com.zhuorui.securities.openaccount.ui.presenter.OAOhterNotesPresenter
import com.zhuorui.securities.openaccount.ui.view.OAOhterNotesView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAOhterNotesViewModel
import kotlinx.android.synthetic.main.fragment_oa_ohter_notes.*
import kotlinx.android.synthetic.main.fragment_oa_property_status.*
import kotlinx.android.synthetic.main.fragment_oa_property_status.btn_next
import kotlinx.android.synthetic.main.fragment_oa_property_status.btn_per

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-29 15:13
 *    desc   : 风险测评-个人信息
 */
class OAOhterNotesFragment :
    AbsSwipeBackFragment<FragmentOaOhterNotesBinding, OAOhterNotesViewModel, OAOhterNotesView, OAOhterNotesPresenter>(),
    OAOhterNotesView, View.OnClickListener {


    companion object {
        fun newInstance(): OAOhterNotesFragment {
            return OAOhterNotesFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_ohter_notes

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OAOhterNotesPresenter
        get() = OAOhterNotesPresenter()

    override val createViewModel: OAOhterNotesViewModel?
        get() = ViewModelProviders.of(this).get(OAOhterNotesViewModel::class.java)

    override val getView: OAOhterNotesView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        btn_per.setOnClickListener(this)
        btn_next.setOnClickListener(this)
    }

    override fun toNext() {
        start(OARiskDisclosureFragment.newInstance())
    }

    override fun getSwitchStatus(): MutableList<Boolean> {
        val list: MutableList<Boolean> = mutableListOf()
        list.add(switch1.isChecked)
        list.add(switch2.isChecked)
        list.add(switch3.isChecked)
        list.add(switch4.isChecked)
        return list.toMutableList()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_per -> {
                pop()
            }
            R.id.btn_next -> {
                if (presenter?.checkData()!!) {
                    presenter?.subBasicsInfo()
                } else {
                    context?.let {
                        ConfirmDialog.createWidth265Dialog(it, true, true)
                            .setConfirmText(R.string.str_i_see)
                            .setMsgText(presenter?.getDialogTextSpannable()!!)
                            .show()
                    }
                }
            }

        }
    }
}