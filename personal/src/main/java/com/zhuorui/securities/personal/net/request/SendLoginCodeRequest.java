package com.zhuorui.securities.personal.net.request;

import com.zhuorui.securities.base2app.network.BaseRequest;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/15
 * Desc:
 */
public class SendLoginCodeRequest extends BaseRequest {
    //    init {
//
//    }
    private String phone;
    private String countryCode;
    private boolean isSend = false;


    public SendLoginCodeRequest(String phone, String countryCode, @NotNull String transaction) {
        super(transaction);
        this.phone = phone;
        this.countryCode = countryCode;
        generateSign();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public boolean getIsSend() {
        return isSend;
    }

    public void setIsSend(boolean send) {
        isSend = send;
    }
}