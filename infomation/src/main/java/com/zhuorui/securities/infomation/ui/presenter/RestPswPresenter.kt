package com.zhuorui.securities.infomation.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.infomation.R
import com.zhuorui.securities.infomation.ui.view.RestPswView
import com.zhuorui.securities.infomation.ui.viewmodel.RestPswViewModel
import java.util.regex.Pattern

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/21
 * Desc:
 */
class RestPswPresenter:AbsNetPresenter<RestPswView, RestPswViewModel>() {
    override fun init() {
        super.init()
        view?.init()
    }

    fun setTips(strnew:String?,strensure:String?){
        viewModel?.strnew?.set(strnew)
        viewModel?.strensure?.set(strensure)
    }
    fun detailtips(strnewpsw:String,strensurepsw:String){
        if (strnewpsw == null || strnewpsw == "") {
            viewModel?.strnew?.set(ResUtil.getString(R.string.input_new_pws_mix))
            return
        }else{
            if (strnewpsw.length<6){
                viewModel?.strnew?.set(ResUtil.getString(R.string.input_new_pws_mix))
                if(strensurepsw != null && strensurepsw != ""){
                    if(strnewpsw==strensurepsw){
                        viewModel?.strensure?.set(ResUtil.getString(R.string.input_new_pws_mix))
                        return
                    }else{
                        viewModel?.strensure?.set(ResUtil.getString(R.string.compare_no_match))
                        return
                    }
                }
            }else {
                val pattern = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{6,20}$"
                //用正则式匹配文本获取匹配器
                val matcher = Pattern.compile(pattern).matcher(strnewpsw)
                if (!matcher.find()) {
                    viewModel?.strnew?.set(ResUtil.getString(R.string.new_psw_no_match))
                    if(strensurepsw != null && strensurepsw != ""){
                        if(strnewpsw==strensurepsw){
                            viewModel?.strensure?.set(ResUtil.getString(R.string.new_psw_no_match))
                            return
                        }else{
                            viewModel?.strensure?.set(ResUtil.getString(R.string.compare_no_match))
                            return
                        }
                    }
                }
            }
        }
        if (strensurepsw == null || strensurepsw == "") {
            viewModel?.strensure?.set(ResUtil.getString(R.string.compare_no_match))
            return
        }else{
            if(strnewpsw==strensurepsw){
                val pattern = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{6,20}$"
                //用正则式匹配文本获取匹配器
                val matcher = Pattern.compile(pattern).matcher(strnewpsw)
                if (!matcher.find()) {
                    viewModel?.strnew?.set(ResUtil.getString(R.string.new_psw_no_match))
                    viewModel?.strensure?.set(ResUtil.getString(R.string.new_psw_no_match))
                    return
                }else {
                    viewModel?.strnew?.set("")
                    viewModel?.strensure?.set("")
                    return
                }
            }else{
                viewModel?.strnew?.set("")
                viewModel?.strensure?.set(ResUtil.getString(R.string.compare_no_match))
            }
            return
        }
    }
}