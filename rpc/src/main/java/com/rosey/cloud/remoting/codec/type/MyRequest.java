package com.rosey.cloud.remoting.codec.type;


import java.util.List;

/**
 * ClassName: MyRequest <br/>
 * Description: <br/>
 * date: 2021/8/11 6:56 下午<br/>
 *
 * @author tooru<br />
 */
public class MyRequest {

    private Long reqId;

    private String invocation;

    private Class target;

    private String methodName;

    private Object[] args;

    public Long getReqId() {
        return reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    public String getInvocation() {
        return invocation;
    }

    public void setInvocation(String invocation) {
        this.invocation = invocation;
    }

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
