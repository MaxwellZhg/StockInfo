package com.zhuorui.securities.personal.ui.presenter
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.ui.adapter.HelpCenterInfoAdapter
import com.zhuorui.securities.personal.ui.model.HelpCenterInfoData
import com.zhuorui.securities.personal.ui.view.OAHelpCenterInfoView
import com.zhuorui.securities.personal.ui.viewmodel.OAHelpCenterInfoViewModel
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/5
 * Desc:
 */
class OAHelpCenterInfoPresenter :AbsNetPresenter<OAHelpCenterInfoView, OAHelpCenterInfoViewModel>(){
    var listinfo=ArrayList<HelpCenterInfoData>()
    override fun init() {
        super.init()
    }

    fun getTipsInfo(type:Int){
         listinfo.clear()
         when(type){
             1->{
                 listinfo.add(HelpCenterInfoData(null,null))
                 listinfo.add(HelpCenterInfoData(ResUtil.getString(R.string.member_of_china),ResUtil.getString(R.string.member_of_china_tips)))
                 listinfo.add(HelpCenterInfoData(ResUtil.getString(R.string.member_of_hk),ResUtil.getString(R.string.member_of_hk_tips)))
                 listinfo.add(HelpCenterInfoData(ResUtil.getString(R.string.member_of_marcao),ResUtil.getString(R.string.member_of_marcao_tips)))
                 listinfo.add(HelpCenterInfoData(ResUtil.getString(R.string.member_of_other),ResUtil.getString(R.string.member_of_other_tips)))
                 listinfo.add(HelpCenterInfoData(null,null))
             }
             2->{
                 listinfo.add(HelpCenterInfoData(null,null))
                 listinfo.add(HelpCenterInfoData(ResUtil.getString(R.string.openaccount_by_info),ResUtil.getString(R.string.update_zhuori_info)))
                 listinfo.add(HelpCenterInfoData(null,null))
             }
             3->{
                 listinfo.add(HelpCenterInfoData(null,null))
                 listinfo.add(HelpCenterInfoData(ResUtil.getString(R.string.openaccount_by_time),ResUtil.getString(R.string.how_long_to_openaccount)))
                 listinfo.add(HelpCenterInfoData(null,null))
             }
             4->{
                 listinfo.add(HelpCenterInfoData(null,null))
                 listinfo.add(HelpCenterInfoData(ResUtil.getString(R.string.openaccount_by_age),ResUtil.getString(R.string.age_range_tips)))
                 listinfo.add(HelpCenterInfoData(null,null))
             }
         }
        viewModel?.adapter?.value?.clearItems()
        if (viewModel?.adapter?.value?.items == null) {
            viewModel?.adapter?.value?.items = ArrayList()
        }
        viewModel?.adapter?.value?.addItems(listinfo)
        LogUtils.e(viewModel?.adapter?.value?.items?.size.toString())

    }

    fun getAdapter(type:Int): HelpCenterInfoAdapter? {
        if (viewModel?.adapter?.value == null) {
            viewModel?.adapter?.value = HelpCenterInfoAdapter(type)
        }
        return viewModel?.adapter?.value
    }
}