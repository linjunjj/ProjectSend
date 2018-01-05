package com.linjun.projectsend.common;

/**
 * Created by linjun on 2018/1/5.
 */

public class JsonResult {
    public JsonResult(){}
    public JsonResult(String errorcode) {
        this.errorcode = errorcode;
    }

    public JsonResult(Object data){
        this.data = data;
    }

    public JsonResult(String errorcode, String errormsg) {
        this.errorcode = errorcode;
        this.errormsg = errormsg;
    }

    public JsonResult(String errorcode, Object data){
        this.errorcode = errorcode;
        this.data = data;
    }

    public JsonResult(String errorcode, String errormsg, Object data){
        this.errorcode = errorcode;
        this.errormsg = errormsg;
        this.data = data;
    }

    private String errorcode = "0";
    private String errormsg = "";
    private Object data = "";

    public String getErrorcode(){
        return errorcode;
    }

    public void setErrorcode(String errorcode){
        this.errorcode = errorcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
