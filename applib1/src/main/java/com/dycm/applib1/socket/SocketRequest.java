package com.dycm.applib1.socket;

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/17 17:53
 * desc   :
 */
public class SocketRequest {

    private SocketHeader header;
    private SubTopic subTopic;
    private Object body;

    public SocketHeader getHeader() {
        return header;
    }

    public void setHeader(SocketHeader header) {
        this.header = header;
    }

    public SubTopic getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(SubTopic subTopic) {
        this.subTopic = subTopic;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
