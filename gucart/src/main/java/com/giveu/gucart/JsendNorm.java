package com.giveu.gucart;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
public class JsendNorm {
    public  static  final  String STATUS_KEY="status";
    public  enum Status{
        SUCCESS("success"),
        FAIL("fail"),
        ERROR("error");
        public  String value;

        private    Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
    public  static  final  String CODE_KEY="key";
    public static final String DATA_KEY="data";
    public static final String MSG_KEY="msg";
}
