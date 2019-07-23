package com.dycm.applib1.socket;

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/17 17:46
 * desc   :
 */
public class SocketResponse {

    private String code;// 0代表成功
    private String msg;
    private String path;
    private String resp_id;

    public boolean isSuccessful() {
        return code!= null && code.equals("0");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getResp_id() {
        return resp_id;
    }

    public void setResp_id(String resp_id) {
        this.resp_id = resp_id;
    }
}
