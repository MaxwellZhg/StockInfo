package com.dycm.applib1.socket;

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/17 17:54
 * desc   :
 */
public class SocketHeader {
    private String dev_id;
    private String language;
    private String req_id;
    private String version;
    private String path;

    public String getDevId() {
        return dev_id;
    }

    public void setDevId(String devId) {
        this.dev_id = devId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getReqId() {
        return req_id;
    }

    public void setReqId(String reqId) {
        this.req_id = reqId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
