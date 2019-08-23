package com.zhuorui.securities.infomation.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.infomation.ui.model.JsonBean
import com.zhuorui.securities.infomation.ui.view.CountryDisctView
import com.zhuorui.securities.infomation.ui.viewmodel.CountryDisctViewModel
import com.zhuorui.securities.infomation.util.PinyinUtils
import java.util.regex.Pattern

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class CountryDisctPresenter : AbsNetPresenter<CountryDisctView, CountryDisctViewModel>(){
   var list:ArrayList<JsonBean> = ArrayList()
    lateinit var searchStr:String
    override fun init() {
        super.init()
        view?.init()
    }

    fun deatilJson(detail:ArrayList<JsonBean>,str:String,type:Int):ArrayList<JsonBean>{
        searchStr = if(type==1) {
            val pinyin = PinyinUtils.getPingYin(str)
            pinyin.substring(0, 1).toUpperCase()
        }else{
            str
        }
        for(jsonbean in detail){
           if(jsonbean.sortLetters==searchStr){
               list.add(jsonbean)
           }
        }
        return list
    }

    fun judgeSerachType(str: String) : Int{
        val pattern1 = "^[\\u4E00-\\u9FA5]+\$"
        val pattern2 ="^\\+?[1-9][0-9]*\$"
        val pattern3 ="\"^[a-zA-Z]+\$\""
        //用正则式匹配文本获取匹配器
        val matcher1 = Pattern.compile(pattern1).matcher(str)
        val matcher2 = Pattern.compile(pattern2).matcher(str)
        val matcher3 = Pattern.compile(pattern3).matcher(str)
        return when {
            matcher1.find() -> 1
            matcher2.find() -> 2
            matcher3.find() -> 3
            else -> 0
        }
    }


}