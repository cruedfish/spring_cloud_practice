package com.giveu.item.guitem;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@FeignClient(name ="cart",fallback = commonServiceFallback.class)
public interface commonService {
    @RequestMapping("/feignTest")
    String feignTest();
}


