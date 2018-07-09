package com.giveu.gucart.Service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "xbin-store-cloud-service-cart",fallback = CartServiceHystrix.class)
public interface CartService {
    @RequestMapping(value = "/addCart",method = RequestMethod.POST)
    Map<String,Object> addCart(
            @RequestParam("pid") Long pid,
            @RequestParam("pcount") Integer pcount,
            @RequestParam("uuid") String uuid
    );
}
