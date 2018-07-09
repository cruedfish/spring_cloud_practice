package com.giveu.item.guitem;

import org.springframework.stereotype.Component;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Component
public class commonServiceFallback implements commonService {
    @Override
    public String feignTest() {
        return "服务器异常";
    }
}
