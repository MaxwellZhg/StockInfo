package com.zhuorui.commonwidget.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.zhuorui.commonwidget.R
import com.zhuorui.commonwidget.R2
import com.zhuorui.securities.base2app.dialog.BaseBottomSheetsDialog
import com.zhuorui.securities.pickerview.IWheelData
import com.zhuorui.securities.pickerview.date.DatePicker
import com.zhuorui.securities.pickerview.option.OnOptionSelectedListener
import com.zhuorui.securities.pickerview.option.OptionsPicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.Typography.times


/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-21 18:33
 *    desc   :
 */
class DatePickerDialog(context: Context) : BaseBottomSheetsDialog(context),
    View.OnClickListener {

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.confirm -> {
                hide()
//                pickver.confirm()
            }
            R.id.cancel -> {
                hide()
            }
        }
    }


    @BindView(R2.id.picker)
    lateinit var pickver: DatePicker
    @BindView(R2.id.confirm)
    lateinit var confirm: TextView
    @BindView(R2.id.cancel)
    lateinit var cancel: TextView
    @BindView(R2.id.title)
    lateinit var title: TextView

    override val layout: Int
        get() = R.layout.dialog_date_picker

    init {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        confirm.setOnClickListener(this)
        cancel.setOnClickListener(this)
        title.setOnClickListener(this)
    }

    fun setCurrentData(timeInMillis: Long) {
        var tms = Calendar.getInstance()
        tms.timeInMillis = timeInMillis
        pickver.setDate(tms.get(Calendar.YEAR), tms.get(Calendar.MONTH), Calendar.DAY_OF_MONTH)
    }

    fun setCurrentData(timeStr: String, format: String) {
        setCurrentData(SimpleDateFormat(format).parse(timeStr).time)
    }

    fun setOnOptionSelectedListener(l: DatePicker.OnDateSelectedListener) {
        pickver.setOnDateSelectedListener(l)
    }


}

