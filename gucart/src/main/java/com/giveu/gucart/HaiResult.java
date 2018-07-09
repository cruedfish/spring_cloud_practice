package com.giveu.gucart;

import java.util.LinkedHashMap;
import java.util.Map;

public class HaiResult {
    private  String code;
    private  String msg;
    private  String getCode;
    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getGetCode() {
        return getCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setGetCode(String getCode) {
        this.getCode = getCode;
    }
    public static Map<String,Object> buildsucc(String code,String msg,Object data){
        Map<String, Object> res = new LinkedHashMap<String, Object>();
        res.put(JsendNorm.CODE_KEY,code);
        res.put(JsendNorm.STATUS_KEY, JsendNorm.Status.SUCCESS.toString());
        res.put(JsendNorm.DATA_KEY, data);
        res.put(JsendNorm.MSG_KEY, msg);
        return res;
    }
}
