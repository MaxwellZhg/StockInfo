package com.zhuorui.securities.market.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.DevComfirmDailog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackEventFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentRemindSettingBinding
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.StockTsEnum
import com.zhuorui.securities.market.ui.presenter.RemindSettingPresenter
import com.zhuorui.securities.market.ui.view.RemindSettingView
import com.zhuorui.securities.market.ui.viewmodel.RemindSettingViewModel
import com.zhuorui.securities.market.util.GotoSettingUtil
import com.zhuorui.securities.market.util.MathUtil
import kotlinx.android.synthetic.main.fragment_remind_setting.*
import java.math.BigDecimal
import java.util.regex.Pattern

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 14:22
 *    desc   :
 */
class RemindSettingFragment :
    AbsSwipeBackEventFragment<FragmentRemindSettingBinding, RemindSettingViewModel, RemindSettingView, RemindSettingPresenter>(),
    RemindSettingView, View.OnClickListener, TextWatcher, View.OnTouchListener, View.OnFocusChangeListener {

    private var upPrice: Boolean = false
    private var downPrice: Boolean = false
    private var upRate: Boolean = false
    private var downRate: Boolean = false
    private val pattern = "^([1-9]\\d*(\\.\\d*[1-9])?)|(0\\.\\d*[1-9])\$"
    /* 加载对话框 */
    private lateinit var phoneDevDailog: DevComfirmDailog

    companion object {
        fun newInstance(stockInfo: StockMarketInfo?): RemindSettingFragment {
            val fragment = RemindSettingFragment()
            if (stockInfo != null) {
                val bundle = Bundle()
                bundle.putParcelable(StockMarketInfo::class.java.simpleName, stockInfo)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_remind_setting

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: RemindSettingPresenter
        get() = RemindSettingPresenter()

    override val createViewModel: RemindSettingViewModel?
        get() = ViewModelProviders.of(this).get(RemindSettingViewModel::class.java)

    override val getView: RemindSettingView
        get() = this

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        val stockInfo = arguments?.getParcelable<StockMarketInfo>(StockMarketInfo::class.java.simpleName)
        presenter?.setStockInfo(stockInfo)
        presenter?.checkSetting().let {
            if (it!!) {
                rl_notice.visibility = View.GONE
            } else {
                rl_notice.visibility = View.VISIBLE
            }
        }

        iv_back.setOnClickListener(this)
        tv_stock_name.text = stockInfo?.name
        when (stockInfo?.ts) {
            StockTsEnum.HK.name -> {
                iv_ts.setImageResource(R.mipmap.ic_ts_hk)
            }
            StockTsEnum.SH.name -> {
                iv_ts.setImageResource(R.mipmap.ic_ts_sh)
            }
            StockTsEnum.SZ.name -> {
                iv_ts.setImageResource(R.mipmap.ic_ts_sz)
            }
        }
        tv_stock_code.text = stockInfo?.code

        et_up_price.addTextChangedListener(this)
        et_down_price.addTextChangedListener(this)
        et_up_rate.addTextChangedListener(this)
        et_down_rate.addTextChangedListener(this)
        et_up_price.onFocusChangeListener = this
        et_down_price.onFocusChangeListener = this
        et_up_rate.onFocusChangeListener = this
        et_down_rate.onFocusChangeListener = this
        et_up_price.setOnTouchListener(this)
        et_down_price.setOnTouchListener(this)
        et_up_rate.setOnTouchListener(this)
        et_down_rate.setOnTouchListener(this)
        iv_up_price.setOnClickListener(this)
        iv_down_price.setOnClickListener(this)
        iv_up_rate.setOnClickListener(this)
        iv_down_rate.setOnClickListener(this)
        tv_save.setOnClickListener(this)
        tv_open_setting.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                pop()
            }
            R.id.iv_up_price -> {
                deatilSwithBtn(iv_up_price, upPrice)
            }
            R.id.iv_down_price -> {
                deatilSwithBtn(iv_down_price, downPrice)
            }
            R.id.iv_up_rate -> {
                deatilSwithBtn(iv_up_rate, upRate)
            }
            R.id.iv_down_rate -> {
                deatilSwithBtn(iv_down_rate, downRate)
            }
            R.id.tv_save -> {
                deatilSave(
                    et_up_price.text.toString(),
                    et_down_price.text.toString(),
                    et_up_rate.text.toString(),
                    et_down_rate.text.toString(),
                    presenter?.getStockInfo()
                )
            }
            R.id.tv_open_setting -> {
                GotoSettingUtil.gotoSetting(requireContext())
            }
        }
    }

    private fun deatilSave(
        upprice: String,
        downprice: String,
        uprate: String,
        downrate: String,
        stockinfo: StockMarketInfo?
    ) {
        if (!TextUtils.isEmpty(upprice) && upprice.toBigDecimal() < stockinfo?.last) {
            context?.let { ResUtil.getString(R.string.up_setting_tips)?.let { it1 -> setDailog(it, it1) } }
            phoneDevDailog.show()
        } else if (!TextUtils.isEmpty(downprice) && downprice.toBigDecimal() > stockinfo?.last) {
            context?.let { ResUtil.getString(R.string.down_setting_tips)?.let { it1 -> setDailog(it, it1) } }
            phoneDevDailog.show()
        } else if (!TextUtils.isEmpty(uprate) && !Pattern.compile(pattern).matcher(uprate).find()) {
            context?.let { ResUtil.getString(R.string.up_rate_tips)?.let { it1 -> setDailog(it, it1) } }
            phoneDevDailog.show()
        } else if (!TextUtils.isEmpty(downrate) && !Pattern.compile(pattern).matcher(downrate).find()) {
            context?.let { ResUtil.getString(R.string.down_rate_tips)?.let { it1 -> setDailog(it, it1) } }
            phoneDevDailog.show()
        } else if (TextUtils.isEmpty(upprice) && TextUtils.isEmpty(downprice) && TextUtils.isEmpty(uprate) && TextUtils.isEmpty(
                downrate
            )
        ) {
            ResUtil.getString(R.string.plaease_input_num)?.let { ToastUtil.instance.toastCenter(it) }
        }
    }

    private fun setDailog(context: Context, str: String) {
        phoneDevDailog = DevComfirmDailog.createWidth255Dialog(context, true, true)
            .setNoticeText(R.string.tips)
            .setMsgText(str)
            .setCancelText(R.string.cancle)
            .setConfirmText(R.string.ensure)
            .setCallBack(object : DevComfirmDailog.CallBack {
                override fun onCancel() {

                }

                override fun onConfirm() {

                }
            })
    }

    override fun afterTextChanged(p0: Editable?) {
        if (!TextUtils.isEmpty(p0.toString())) {
            when {
                et_up_price.isFocused -> {
                    showCancleDrawable(et_up_price, iv_up_price, et_up_price.text.toString(), tv_up_nomatch_tips)
                }
                et_down_price.isFocused -> {
                    showCancleDrawable(
                        et_down_price,
                        iv_down_price,
                        et_down_price.text.toString(),
                        tv_down_nomatch_tips
                    )
                }
                et_up_rate.isFocused -> {
                    showCancleDrawable(et_up_rate, iv_up_rate, et_up_rate.text.toString(), tv_uprate_nomatch_tips)
                }
                else -> {
                    showCancleDrawable(
                        et_down_rate,
                        iv_down_rate,
                        et_down_rate.text.toString(),
                        tv_downrate_nomatch_tips
                    )
                }
            }
        } else {
            when {
                et_up_price.isFocused -> {
                    detailCancle(et_up_price, iv_up_price, tv_up_nomatch_tips)
                }
                et_down_price.isFocused -> {
                    detailCancle(et_down_price, iv_down_price, tv_down_nomatch_tips)
                }
                et_up_rate.isFocused -> {
                    detailCancle(et_up_rate, iv_up_rate, tv_uprate_nomatch_tips)
                }
                else -> {
                    detailCancle(et_down_rate, iv_down_rate, tv_downrate_nomatch_tips)
                }
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
        // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
        return when (p0) {
            et_up_price -> {
                et_up_price.isFocusableInTouchMode = true
                detailEditDrawable(et_up_price, event)
            }
            et_down_price -> {
                et_down_price.isFocusableInTouchMode = true
                detailEditDrawable(et_down_price, event)
            }
            et_up_rate -> {
                et_up_rate.isFocusableInTouchMode = true
                detailEditDrawable(et_up_rate, event)
            }
            else -> {
                et_down_rate.isFocusableInTouchMode = true
                detailEditDrawable(et_down_rate, event)
            }
        }
    }

    private fun detailEditDrawable(edittext: EditText, event: MotionEvent?): Boolean {
        val drawable = edittext.compoundDrawables[2] ?: return false
        //如果右边没有图片，不再处理
        //如果不是按下事件，不再处理
        if (event?.action != MotionEvent.ACTION_UP)
            return false
        if (event.x > (edittext.width - edittext.paddingRight - drawable.intrinsicWidth)) {
            edittext.setText("")
            edittext.hint = ResUtil.getString(R.string.setting_no_info)
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun showCancleDrawable(edittext: EditText, iv: ImageButton, str: String, tv: TextView) {
        iv.setImageResource(R.mipmap.ic_switch_open)
        edittext.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            ResUtil.getDrawable(R.mipmap.icon_et_set_cancle),
            null
        )
        when (edittext) {
            et_up_price -> {
                upPrice = true
                if (str.toBigDecimal() < presenter?.getStockInfo()?.last) {
                    tv.visibility = View.VISIBLE
                    tv_down_nomatch_tips.visibility = View.INVISIBLE
                    tv_uprate_nomatch_tips.visibility = View.INVISIBLE
                    tv_downrate_nomatch_tips.visibility = View.INVISIBLE
                    ResUtil.getColor(R.color.color_FF0000)?.let { et_up_price.setTextColor(it) }
                    tv.text = ResUtil.getString(R.string.up_setting_tips)
                } else {
                    tv.visibility = View.INVISIBLE
                    tv_down_nomatch_tips.visibility = View.INVISIBLE
                    tv_uprate_nomatch_tips.visibility = View.INVISIBLE
                    tv_downrate_nomatch_tips.visibility = View.INVISIBLE
                    val count: BigDecimal? =
                        presenter?.getStockInfo()?.last?.let { MathUtil.divide2(str.toBigDecimal(), it) }?.let {
                            MathUtil.multiply2(
                                it, 100.toBigDecimal()
                            ) - 100.toBigDecimal()
                        }
                    tv.text = ResUtil.getString(R.string.compare_price_up) + count.toString() + "%"
                    tv.visibility = View.VISIBLE
                    ResUtil.getColor(R.color.color_FFFFFFFF)?.let { et_up_price.setTextColor(it) }

                }
            }
            et_down_price -> {
                downPrice = true
                if (str.toBigDecimal() > presenter?.getStockInfo()?.last) {
                    tv.visibility = View.VISIBLE
                    tv_up_nomatch_tips.visibility = View.INVISIBLE
                    tv_uprate_nomatch_tips.visibility = View.INVISIBLE
                    tv_downrate_nomatch_tips.visibility = View.INVISIBLE
                    ResUtil.getColor(R.color.color_FF0000)?.let { et_down_price.setTextColor(it) }
                    tv.text = ResUtil.getString(R.string.down_setting_tips)
                } else {
                    tv.visibility = View.VISIBLE
                    tv_up_nomatch_tips.visibility = View.INVISIBLE
                    tv_uprate_nomatch_tips.visibility = View.INVISIBLE
                    tv_downrate_nomatch_tips.visibility = View.INVISIBLE
                    val count: BigDecimal? = presenter?.getStockInfo()?.last?.let {
                        presenter?.getStockInfo()?.last?.let { MathUtil.subtract2(it, str.toBigDecimal()) }
                            ?.let { it1 ->
                                MathUtil.divide2(
                                    it1,
                                    it
                                )
                            }
                    }
                    val downprecent: BigDecimal? = count?.let { MathUtil.multiply2(it, 100.toBigDecimal()) }
                    tv.text = ResUtil.getString(R.string.compare_price_down) + downprecent.toString() + "%"
                    tv.visibility = View.VISIBLE
                    ResUtil.getColor(R.color.color_FFFFFFFF)?.let { et_down_price.setTextColor(it) }
                }
            }
            et_up_rate -> {
                upRate = true
                //用正则式匹配文本获取匹配器
                val matcher = Pattern.compile(pattern).matcher(str)
                if (!matcher.find()) {
                    tv.visibility = View.VISIBLE
                    tv_up_nomatch_tips.visibility = View.INVISIBLE
                    tv_down_nomatch_tips.visibility = View.INVISIBLE
                    tv_downrate_nomatch_tips.visibility = View.INVISIBLE
                    ResUtil.getColor(R.color.color_FF0000)?.let { et_up_rate.setTextColor(it) }
                    tv.text = ResUtil.getString(R.string.up_rate_tips)
                } else {
                    tv.visibility = View.VISIBLE
                    tv_up_nomatch_tips.visibility = View.INVISIBLE
                    tv_down_nomatch_tips.visibility = View.INVISIBLE
                    tv_downrate_nomatch_tips.visibility = View.INVISIBLE
                    val count: BigDecimal =
                        MathUtil.add2(MathUtil.divide2(str.toBigDecimal(), 100.toBigDecimal()), 1.toBigDecimal())
                    val upprice: BigDecimal? = presenter?.getStockInfo()?.last?.let { MathUtil.multiply2(it, count) }
                    tv.text = ResUtil.getString(R.string.compare_price_to) + upprice
                    ResUtil.getColor(R.color.color_FFFFFFFF)?.let { et_down_rate.setTextColor(it) }
                }
            }
            et_down_rate -> {
                downRate = false
                //用正则式匹配文本获取匹配器
                val matcher = Pattern.compile(pattern).matcher(str)
                if (!matcher.find()) {
                    tv.visibility = View.VISIBLE
                    tv_up_nomatch_tips.visibility = View.INVISIBLE
                    tv_down_nomatch_tips.visibility = View.INVISIBLE
                    tv_uprate_nomatch_tips.visibility = View.INVISIBLE
                    ResUtil.getColor(R.color.color_FF0000)?.let { et_down_rate.setTextColor(it) }
                    tv.text = ResUtil.getString(R.string.down_rate_tips)

                } else {
                    tv.visibility = View.VISIBLE
                    tv_up_nomatch_tips.visibility = View.INVISIBLE
                    tv_down_nomatch_tips.visibility = View.INVISIBLE
                    tv_uprate_nomatch_tips.visibility = View.INVISIBLE
                    val count: BigDecimal =
                        MathUtil.subtract2(1.toBigDecimal(), MathUtil.divide2(str.toBigDecimal(), 100.toBigDecimal()))
                    val downprice: BigDecimal? = presenter?.getStockInfo()?.last?.let { MathUtil.multiply2(it, count) }
                    tv.text = ResUtil.getString(R.string.compare_price_to) + downprice
                    ResUtil.getColor(R.color.color_FFFFFFFF)?.let { et_down_rate.setTextColor(it) }
                }
            }
        }
    }

    private fun detailCancle(edittext: EditText, iv: ImageButton, tv: TextView) {
        iv.setImageResource(R.mipmap.ic_switch_close)
        edittext.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        tv.visibility = View.INVISIBLE
        // tips.visibility = View.INVISIBLE
        when (edittext) {
            et_up_price -> {
                upPrice = false
            }
            et_down_price -> {
                downPrice = false
            }
            et_up_rate -> {
                upRate = false
            }
            et_down_rate -> {
                downRate = false
            }
        }
    }

    private fun deatilSwithBtn(iv: ImageButton, bool: Boolean) {
        when (iv) {
            iv_up_price -> {
                upPrice = if (upPrice) {
                    iv_up_price.setImageResource(R.mipmap.ic_switch_close)
                    false
                } else {
                    iv_up_price.setImageResource(R.mipmap.ic_switch_open)
                    true
                }
            }
            iv_down_price -> {
                downPrice = if (downPrice) {
                    iv_down_price.setImageResource(R.mipmap.ic_switch_close)
                    false
                } else {
                    iv_down_price.setImageResource(R.mipmap.ic_switch_open)
                    true
                }
            }
            iv_up_rate -> {
                upRate = if (upRate) {
                    iv_up_rate.setImageResource(R.mipmap.ic_switch_close)
                    false
                } else {
                    iv_up_rate.setImageResource(R.mipmap.ic_switch_open)
                    true
                }
            }
            iv_down_rate -> {
                downRate = if (downRate) {
                    iv_down_rate.setImageResource(R.mipmap.ic_switch_close)
                    false
                } else {
                    iv_down_rate.setImageResource(R.mipmap.ic_switch_open)
                    true
                }
            }
        }
    }

    override fun onFocusChange(p0: View?, hasFocus: Boolean) {
        when (p0) {
            et_up_price -> {
                detailFcousOn(et_up_price, hasFocus)
            }
            et_down_price -> {
                detailFcousOn(et_down_price, hasFocus)
            }
            et_up_rate -> {
                detailFcousOn(et_up_rate, hasFocus)
            }
            et_down_rate -> {
                detailFcousOn(et_down_rate, hasFocus)
            }
        }
    }

    fun detailFcousOn(et: EditText, bool: Boolean) {
        if (et.isFocused) {
            if (bool) {
                when (TextUtils.isEmpty(et.text.toString())) {
                    true -> {
                        et.hint = ResUtil.getString(R.string.setting_no_info)
                    }
                }
            } else {
                when (TextUtils.isEmpty(et.text.toString())) {
                    true -> {
                        et.hint = ResUtil.getString(R.string.setting_no_info)
                    }
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        hideSoftInput()
    }

    override fun updateStockPrice(price: BigDecimal?, diffPrice: BigDecimal?, diffRate: BigDecimal?, diffState: Int) {
        tv_stock_price.setText(price?.toString() ?: "--", diffState)
        tv_diff_pirce.setText(diffPrice?.toString() ?: "--", diffState)
        tv_diff_rate.setText(diffRate?.toString() ?: "--", diffState)
    }
}