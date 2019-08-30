package com.zhuorui.commonwidget.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.zhuorui.commonwidget.BR
import com.zhuorui.commonwidget.R
import com.zhuorui.commonwidget.databinding.CommonCountryCodeFragmentBinding
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.GetJsonDataUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.common_country_code_fragment.*
import me.jessyan.autosize.utils.LogUtils
import org.json.JSONArray
import java.util.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/30
 * Desc:
 */
class CommonCountryCodeFragment :AbsSwipeBackNetFragment<CommonCountryCodeFragmentBinding,CommonCountryViewModel,CommonCountryCodeView,CommonCountryCodePresenter>(),CommonCountryCodeView,TextWatcher{

    private val MSG_LOAD_DATA = 0x0001
    private val MSG_LOAD_SUCCESS = 0x0002
    private val MSG_LOAD_FAILED = 0x0003
    private var thread: Thread? = null
    private lateinit var jsonBean: LinkedList<JsonBean>
    private var result: LinkedList<JsonBean> = LinkedList()
    private var isLoaded: Boolean = false
    private var adapter:SortAdapter?=null
    private var handler = Handler()
    private var getTopicStockDataRunnable: GetTopicStockDataRunnable? = null
    private  var type:CommonEnum?=null
    override val layout: Int
        get() = R.layout.common_country_code_fragment
    override val viewModelId: Int
        get() = BR.viewmodel
    override val createPresenter: CommonCountryCodePresenter
        get() = CommonCountryCodePresenter()
    override val createViewModel: CommonCountryViewModel?
        get() = ViewModelProviders.of(this).get(CommonCountryViewModel::class.java)
    override val getView: CommonCountryCodeView
        get() = this

    companion object {
        fun newInstance(type: CommonEnum?): CommonCountryCodeFragment {
            val fragment = CommonCountryCodeFragment()
            if (type != null) {
                val bundle = Bundle()
                bundle.putSerializable("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
         type = arguments?.getSerializable("type") as CommonEnum?
        initJsonData()
        quickindexbar.setonTouchLetterListener{
            for (i in 0 until jsonBean.size) {

                val city = jsonBean[i]

                val l = city.sortLetters

                if (TextUtils.equals(it, l)) {

                    //匹配成功
                    lv_country.setSelection(i)

                    break

                }

            }
        }
        et_search.addTextChangedListener(this)
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
                            Collections.sort(it, PinyinComparator())
                            adapter = SortAdapter(requireContext(),type)
                            it.addFirst(JsonBean("meiguo","美國","meiguo",false,"+1","United States of America","美国","常用地区","U"))
                            it.addFirst(JsonBean("zhongguotaiwan","中國台灣","zhongguotaiwan",false,"+886","Taiwan,China","中国台湾","常用地区","T"))
                            it.addFirst(JsonBean("zhongguoaomen","中國澳門","zhongguoaomen",false,"+853","Macao,China","中国澳门","常用地区","M"))
                            it.addFirst(JsonBean("zhongguoxianggang","中國香港","zhongguoxianggang",false,"+852","Hongkong,China","中国香港","常用地区","H"))
                            it.addFirst(JsonBean("zhongguo","中國内地","zhongguo",false,"+86","China","中国内地","常用地区","C"))
                            adapter?.addItems(it)
                            lv_country.adapter = adapter
                            adapter?.notifyDataSetChanged()
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

    private fun parseData(result: String): LinkedList<JsonBean> {//Gson 解析
        val detail = LinkedList<JsonBean>()
        try {
            val data = JSONArray(result)
            val gson = Gson()
            for (i in 0 until data.length()) {
                val entity:JsonBean = gson.fromJson<JsonBean>(data.optJSONObject(i).toString(), JsonBean::class.java)
                detail.add(JsonBean(entity.cn_py,entity.hant,entity.hant_py,entity.isUsed,entity.number,entity.en,entity.cn,entity.cn_py.substring(0,1).toUpperCase(),entity.en.substring(0,1).toUpperCase()))
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
        mHandler.removeCallbacksAndMessages(null)
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {

            p0?.toString()?.trim()?.let {
                handler.removeCallbacks(getTopicStockDataRunnable)
                getTopicStockDataRunnable = GetTopicStockDataRunnable(it)
                handler.postDelayed(getTopicStockDataRunnable, 500)
            }

        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    private inner class GetTopicStockDataRunnable(var keyWord: String) : Runnable {
        override fun run() {
            result.clear()
            result.addAll(presenter?.deatilJson(jsonBean,keyWord, presenter?.judgeSerachType(keyWord)!!)!!)
            if(result?.size>0) {
                adapter?.clearItems()
                adapter?.addItems(result)
                adapter?.notifyDataSetChanged()
            }else{
                adapter?.clearItems()
                adapter?.addItems(jsonBean)
                adapter?.notifyDataSetChanged()
            }
        }
    }
}