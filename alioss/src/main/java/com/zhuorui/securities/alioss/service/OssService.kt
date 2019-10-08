package com.zhuorui.securities.alioss.service

import android.content.Context
import android.text.TextUtils
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSS
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken
import com.alibaba.sdk.android.oss.model.OSSRequest
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.zhuorui.securities.alioss.net.IOssNet
import com.zhuorui.securities.alioss.net.response.TokenResponse
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseRequest
import com.zhuorui.securities.base2app.network.Transactions
import com.zhuorui.securities.base2app.util.TimeZoneUtil
import io.reactivex.Observable
import retrofit2.Call
import java.util.HashSet as HashSet1


/**
 * Created by mOss on 2015/12/7 0007.
 * 支持普通上传，普通下载
 */
class OssService(applicationContext: Context, type: String, endpoint: String, bucketName: String) {

    var mOss: OSS
    private var mBucket = if (TextUtils.isEmpty(endpoint)) "zhuorui-dev" else bucketName
    private var mEndpoint = if (TextUtils.isEmpty(endpoint)) "http://oss-cn-shenzhen.aliyuncs.com" else endpoint
    private var mType = type

    private val credentialProvider = object : OSSFederationCredentialProvider() {
        @Throws(ClientException::class)
        override fun getFederationToken(): OSSFederationToken? {
            // 您需要在这里实现获取一个FederationToken，并构造成OSSFederationToken对象返回
            // 如果因为某种原因获取失败，可直接返回null
            val re = BaseRequest(Transactions().createTransaction())
            re.generateSign()
            val call: Call<TokenResponse> = Cache[IOssNet::class.java]?.token(re)!!
            val response = call.execute()
            if (response.isSuccessful) {
                val token = response.body()
                if (token != null && token.isSuccess()) {
                    return OSSFederationToken(
                        token.data.accessKeyId,
                        token.data.accessKeySecret,
                        token.data.securityToken,
                        token.data.expiration
                    )
                } else if (token != null) {
                    throw ClientException(token.msg)
                }
            } else {
                throw ClientException(response.message())
            }
            return throw ClientException("获取Token失败")
        }
    }

    private val VIDEO_SUFFIX: MutableSet<String> = mutableSetOf(".mp4")

    init {
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒
        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒
        conf.maxConcurrentRequest = 5 // 最大并发请求书，默认5个
        conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次
        mOss = OSSClient(applicationContext, mEndpoint, credentialProvider, conf)
    }

    fun getPutObjectObservable(objectName: String, uploadData: ByteArray): Observable<String> {
        return Observable.create { emitter ->
            val put = PutObjectRequest(mBucket, objectName, uploadData)
            put.crC64 = OSSRequest.CRC64Config.YES
//            put.progressCallback = OSSProgressCallback { request, currentSize, totalSize ->
//                LogInfra.Log.i("lw", "currentSize:$currentSize totalSize:$totalSize")
//            }
            val putResult = mOss.putObject(put)
            if (putResult.statusCode == 200) {
                emitter.onNext(objectName)
                emitter.onComplete()
            } else {
                emitter.onError(RuntimeException(putResult.statusCode.toString()))
            }
        }
    }

    fun getPutObjectObservable(uploadFilePath: String): Observable<String> {
        val suffix = uploadFilePath.substring(uploadFilePath.lastIndexOf("."))
        val name = if (VIDEO_SUFFIX.contains(suffix)) createUpVideoName(suffix) else createUpImageName(suffix)
        return getPutObjectObservable(name, uploadFilePath)
    }

    fun getPutObjectObservable(objectName: String, uploadFilePath: String): Observable<String> {
        return Observable.create { emitter ->
            val put = PutObjectRequest(mBucket, objectName, uploadFilePath)
            put.crC64 = OSSRequest.CRC64Config.YES
//            put.progressCallback = OSSProgressCallback { request, currentSize, totalSize ->
//                LogInfra.Log.i("lw", "currentSize:$currentSize totalSize:$totalSize")
//            }
            val putResult = mOss.putObject(put)
            if (putResult.statusCode == 200) {
                emitter.onNext(objectName)
                emitter.onComplete()
            } else {
                emitter.onError(RuntimeException(putResult.statusCode.toString()))
            }
        }
    }

    fun createUpImageName(suffix: String): String {
        return createName("images", suffix)
    }

    fun createUpVideoName(suffix: String): String {
        return createName("video", suffix)
    }

    fun createName(fileType: String, suffix: String): String {
        val timeMillis = TimeZoneUtil.currentTimeMillis()
        var strRand = ""
        for (i in 0..3) {
            strRand += (Math.random() * 10).toInt().toString()
        }
        val date = TimeZoneUtil.timeFormat(timeMillis, "yyyy/MM/dd")
        return "$mType/$fileType/$date/$timeMillis$strRand$suffix"
    }

    companion object {
        const val TYPE_OPEN = "open"//开户类型
    }


}
