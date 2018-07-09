package com.giveu.gucart.Service;


import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 购物车服务 熔断处理
 *
 * @author xubin.
 * @create 2017-05-04 下午11:59
 */

@Component
public class CartServiceHystrix implements CartService {

    @Override
    public Map<String,Object> addCart(Long pid, Integer pcount, String uuid) {
        return null;
    }
}
