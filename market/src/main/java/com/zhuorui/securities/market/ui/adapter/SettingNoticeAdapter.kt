package com.zhuorui.securities.market.ui.adapter

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2
import com.zhuorui.securities.market.model.SettingNoticeData
import java.math.BigDecimal


/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/25
 * Desc:
 */
class SettingNoticeAdapter : BaseListAdapter<SettingNoticeData>() {

    override fun getLayout(viewType: Int): Int {
        return R.layout.item_setting_notice_layout
    }

    override fun createViewHolder(v: View?, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(v, false, false)
    }

    inner class ViewHolder(v: View?, needClick: Boolean, needLongClick: Boolean) :
        ListItemViewHolder<SettingNoticeData>(v, needClick, needLongClick), TextWatcher, View.OnTouchListener {
        @BindView(R2.id.ic_rise_arrow)
        lateinit var ic_rise_arrow: ImageView

        @BindView(R2.id.tv_rise_threshold)
        lateinit var tv_rise_threshold: TextView
        @BindView(R2.id.tips_info)
        lateinit var tips_info: TextView
        @BindView(R2.id.tv_mark)
        lateinit var tv_mark: TextView
        @BindView(R2.id.tv_nomatch_tips)
        lateinit var tv_nomatch_tips: TextView

        @BindView(R2.id.et_setting)
        lateinit var et_setting: AppCompatEditText

        @BindView(R2.id.ic_switch)
        lateinit var ic_switch: ImageButton
        lateinit var iteminfo: SettingNoticeData
        var pos: Int = -1
        override fun bind(item: SettingNoticeData?, position: Int) {
            iteminfo = items[position]
            pos = position
            ic_rise_arrow.background = item?.res?.let { ResUtil.getDrawable(it) }
            tv_rise_threshold.text = item?.str
            when (item?.isSelect) {
                true -> {
                    ic_switch.setImageResource(R.mipmap.ic_switch_open)
                }
                false -> {
                    ic_switch.setImageResource(R.mipmap.ic_switch_close)
                }
            }
            if(position>1){
                tv_mark.visibility = View.VISIBLE
            }else{
                tv_mark.visibility = View.INVISIBLE
            }
            //todo
            et_setting.addTextChangedListener(this)
            et_setting.setOnTouchListener(this)
            et_setting.setText(iteminfo?.content)
        }

        override fun afterTextChanged(p0: Editable?) {
            var count: BigDecimal
            if (!TextUtils.isEmpty(p0.toString())) {
                iteminfo.setIsSelect(true)
                iteminfo.setEtContent(p0.toString())
                ic_switch.setImageResource(R.mipmap.ic_switch_open)
                et_setting.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ResUtil.getDrawable(R.mipmap.icon_et_set_cancle),
                    null
                )
                for (item in items) {
                    item.setShowTips(false)
                }
                iteminfo.setShowTips(true)
                count= p0.toString().toBigDecimal()
               when(pos){
                   0->{
                       if(count < iteminfo.stockInfo?.last){
                           ResUtil.getColor(R.color.color_FF0000)?.let { et_setting.setTextColor(it) }
                           tv_nomatch_tips.visibility=View.VISIBLE
                           tv_nomatch_tips.text = ResUtil.getString(R.string.up_setting_tips)
                           tips_info.visibility=View.INVISIBLE
                       }else{
                           ResUtil.getColor(R.color.color_FFFFFFFF)?.let { et_setting.setTextColor(it) }
                           tv_nomatch_tips.visibility=View.INVISIBLE
                           tips_info.visibility=View.VISIBLE
                           tips_info.text = ResUtil.getString(R.string.compare_price_up)
                       }
                   }
                   1->{
                       if(count > iteminfo.stockInfo?.last){
                           ResUtil.getColor(R.color.color_FF0000)?.let { et_setting.setTextColor(it) }
                           tv_nomatch_tips.visibility=View.VISIBLE
                           tv_nomatch_tips.text = ResUtil.getString(R.string.down_setting_tips)
                       }else{
                           ResUtil.getColor(R.color.color_FFFFFFFF)?.let { et_setting.setTextColor(it) }
                           tv_nomatch_tips.visibility=View.INVISIBLE
                           tips_info.visibility=View.VISIBLE
                       }
                   }
                   2->{
                       if(count<=0.00.toBigDecimal()){
                           ResUtil.getColor(R.color.color_FF0000)?.let { et_setting.setTextColor(it) }
                           tv_nomatch_tips.visibility=View.VISIBLE
                           tv_nomatch_tips.text = ResUtil.getString(R.string.up_rate_tips)
                       }else{
                           ResUtil.getColor(R.color.color_FFFFFFFF)?.let { et_setting.setTextColor(it) }
                           tv_nomatch_tips.visibility=View.INVISIBLE
                           tips_info.visibility=View.VISIBLE
                       }
                   }
                   3->{
                       if(count<=0.00.toBigDecimal()){
                           ResUtil.getColor(R.color.color_FF0000)?.let { et_setting.setTextColor(it) }
                           tv_nomatch_tips.visibility=View.VISIBLE
                           tv_nomatch_tips.text = ResUtil.getString(R.string.down_rate_tips)
                       }else{
                           ResUtil.getColor(R.color.color_FFFFFFFF)?.let { et_setting.setTextColor(it) }
                           tv_nomatch_tips.visibility=View.INVISIBLE
                           tips_info.visibility=View.VISIBLE
                       }
                   }
               }
            } else {
                iteminfo.setIsSelect(false)
                iteminfo.setEtContent("0.00")
                ic_switch.setImageResource(R.mipmap.ic_switch_close)
                et_setting.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                for (item in items) {
                    item.setShowTips(false)
                }
                tv_nomatch_tips.visibility=View.INVISIBLE
                tips_info.visibility=View.INVISIBLE

            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
            // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
            val drawable = et_setting.compoundDrawables[2] ?: return false
            //如果右边没有图片，不再处理
            //如果不是按下事件，不再处理
            if (event?.action !== MotionEvent.ACTION_UP)
                return false
            if (event.x > (et_setting.width - et_setting.paddingRight - drawable.intrinsicWidth)) {
                et_setting.setText("")
                et_setting.hint = "0.00"
            }
            return false
        }


    }


}

