package com.zhuorui.securities.personal.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/10
 * Desc:
 */
interface ChangePhoneNumView :AbsView{
  fun gotonext()
  fun showGetCode(str:String)
  fun showProgressDailog(type:Int)
}