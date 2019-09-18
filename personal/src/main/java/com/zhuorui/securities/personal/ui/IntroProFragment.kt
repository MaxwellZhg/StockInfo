package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.FragmentIntroProBinding
import com.zhuorui.securities.personal.ui.presenter.IntroProPresenter
import com.zhuorui.securities.personal.ui.view.IntroProView
import com.zhuorui.securities.personal.ui.viewmodel.IntroProViewModel
import kotlinx.android.synthetic.main.fragment_intro_pro.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/12
 * Desc:
 */
class IntroProFragment :AbsSwipeBackNetFragment<FragmentIntroProBinding,IntroProViewModel,IntroProView,IntroProPresenter>(),IntroProView,View.OnClickListener{

    override val layout: Int
        get() = R.layout.fragment_intro_pro
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: IntroProPresenter
        get() = IntroProPresenter()
    override val createViewModel: IntroProViewModel?
        get() = ViewModelProviders.of(this).get(IntroProViewModel::class.java)
    override val getView: IntroProView
        get() = this
    companion object {
        fun newInstance(): IntroProFragment {
            return IntroProFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        tv_about_zr.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
       when(p0?.id){
           R.id.tv_about_zr->{
               start(IntroProInfoFragment.newInstance())
           }
       }
    }
}