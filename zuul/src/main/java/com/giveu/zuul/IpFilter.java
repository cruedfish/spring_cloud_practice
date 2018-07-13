package com.giveu.zuul;

import com.haistore.redis.HaiResult;
import com.haistore.redis.IPUtil;
import com.haistore.redis.JsonUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ocsp.ResponseData;

import java.util.Map;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
public class IpFilter extends ZuulFilter {
    Logger logger = Logger.getLogger(IpFilter.class);
    public IpFilter() {
        super();
    }

    @Override
    public String filterType() {
        return  "pre";
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        String ip = com.giveu.zuul.IPUtil.getIpAddr(ctx.getRequest());
        if (StringUtils.isNotBlank(ip)){
            ctx.set("isSuccess", false);
        ctx.setSendZuulResponse(false);
        Map<String,Object> data = HaiResult.buildFail("-1","非法请求",null);
        ctx.setResponseBody(JsonUtil.write2JsonStr(data));
        ctx.getResponse().setContentType("application/json; charset=utf-8");
    }
        return null;
    }

}
