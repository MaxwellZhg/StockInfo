package com.dycm.modulea.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/13
 * Desc:
 */
data class InfoModel(@SerializedName("name") var name: String,

                     @SerializedName("lastname") var lastname: String)