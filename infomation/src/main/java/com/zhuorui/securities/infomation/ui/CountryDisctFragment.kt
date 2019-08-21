package com.zhuorui.securities.infomation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.GetJsonDataUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.infomation.BR
import com.zhuorui.securities.infomation.R
import com.zhuorui.securities.infomation.ui.adapter.SortAdapter
import com.zhuorui.securities.infomation.ui.model.JsonBean
import com.zhuorui.securities.infomation.ui.presenter.CountryDisctPresenter
import com.zhuorui.securities.infomation.ui.view.CountryDisctView
import com.zhuorui.securities.infomation.ui.viewmodel.CountryDisctViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.country_city_fragment.*
import me.jessyan.autosize.utils.LogUtils
import org.json.JSONArray
import java.util.*
import com.zhuorui.securities.infomation.databinding.CountryCityFragmentBinding
import com.zhuorui.securities.infomation.ui.viewmodel.OpenAccountTabViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/19
 * Desc:
 */
class CountryDisctFragment :AbsSwipeBackNetFragment<CountryCityFragmentBinding, CountryDisctViewModel, CountryDisctView, CountryDisctPresenter>(),View.OnClickListener,CountryDisctView {
    private val MSG_LOAD_DATA = 0x0001
    private val MSG_LOAD_SUCCESS = 0x0002
    private val MSG_LOAD_FAILED = 0x0003
    private var thread: Thread? = null
    private lateinit var jsonBean: ArrayList<JsonBean>
    private var isLoaded: Boolean = false
    override val layout: Int
        get() = R.layout.country_city_fragment
    override val viewModelId: Int
        get() = BR.viewmodel
    override val createPresenter: CountryDisctPresenter
        get() = CountryDisctPresenter()
    override val createViewModel: CountryDisctViewModel?
        get() = ViewModelProviders.of(this).get(CountryDisctViewModel::class.java)
    override val getView: CountryDisctView
        get() = this

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_back -> {
                pop()
            }
        }
    }

    override fun init() {
        iv_back.setOnClickListener(this)
    }

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initJsonData()
    }

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_LOAD_DATA -> if (thread == null) {//如果已创建就不再重新创建子线程了

                    // Toast.makeText(this@AddintoAddressActivity, "Begin Parse Data", Toast.LENGTH_SHORT).show()
                    thread = Thread(Runnable {
                        // 子线程中解析省市区数据
                        initJsonData()
                    })
                    thread!!.start()
                }

                MSG_LOAD_SUCCESS -> {
                    //Toast.makeText(AddintoAddressActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    LogUtils.e(jsonBean.toString())
                    Observable.just(jsonBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            var adpter = SortAdapter(requireContext())
                            adpter.addItems(it)
                            lv_country.adapter = adpter
                            adpter.notifyDataSetChanged()
                        }
                    isLoaded = true
                }

                MSG_LOAD_FAILED -> {
                    ToastUtil.instance.toast("failed")
                }
            }
        }
    }

    private fun initJsonData() {
        val JsonData = GetJsonDataUtil().getJson(requireContext(), "countrys.json")//获取assets目录下的json文件数据
        jsonBean = parseData(JsonData)//用Gson 转成实体
    }

    private fun parseData(result: String): ArrayList<JsonBean> {//Gson 解析
        val detail = ArrayList<JsonBean>()
        try {
            val data = JSONArray(result)
            val gson = Gson()
            for (i in 0 until data.length()) {
                val entity = gson.fromJson<JsonBean>(data.optJSONObject(i).toString(), JsonBean::class.java)
                detail.add(entity as JsonBean)
            }
            mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS)
        } catch (e: Exception) {
            e.printStackTrace()
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED)
        }

        return detail
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler?.removeCallbacksAndMessages(null)
    }


}

