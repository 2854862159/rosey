package com.rosey.cloud.remoting.codec.type;

/**
 * ClassName: MyResponse <br/>
 * Description: <br/>
 * date: 2021/8/11 6:58 下午<br/>
 *
 * @author tooru<br />
 */
public class MyResponse {

    private Long reqId;

    private Object data;

    public Long getReqId() {
        return reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
