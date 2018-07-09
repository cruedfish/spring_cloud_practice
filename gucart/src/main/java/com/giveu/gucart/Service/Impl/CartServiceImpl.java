package com.giveu.gucart.Service.Impl;

import com.giveu.gucart.FastJsonConvert;
import com.giveu.gucart.HaiResult;
import com.giveu.gucart.Service.CartService;
import com.giveu.gucart.mapper.TbItemMapper;
import com.giveu.gucart.pojo.CartInfo;
import com.giveu.gucart.pojo.TbItem;
import com.haistore.redis.JedisClientSingle;
import com.haistore.redis.JsonUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(value = "API - CartServiceImpl", description = "购物车操作")
@RestController
@RefreshScope
public class CartServiceImpl implements CartService {
    protected  Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    TbItemMapper itemMapper;

    @Value("${redisKey.prefix.cart_info_profix}")
    private String CART_INFO_PROFIX;
    @Value("${redisKey.prefix.redis_cart_expire_time}")
    private Integer REDIS_CART_EXPIRE_TIME;
    @Value("${redisKey.prefix.item_info_profix}")
    private String ITEM_INFO_PROFIX;
    @Value("${redisKey.prefix.item_info_base_suffix}")
    private String ITEM_INFO_BASE_SUFFIX;

    JedisClientSingle jedisClient=new JedisClientSingle();
    @Override
    @ApiOperation("购物车添加商品")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "pid", value = "", required = true, dataType = "Long"),
                    @ApiImplicitParam(name = "pcount", value = "", required = true, dataType = "Long"),
                    @ApiImplicitParam(name = "uuid", value = "", required = true, dataType = "Long"),
            }
    )
    @ApiResponses(
            {
                    @ApiResponse(code = 200, message = "Successful — 请求已完成"),
                    @ApiResponse(code = 400, message = "请求中有语法问题，或不能满足请求"),
                    @ApiResponse(code = 401, message = "未授权客户机访问数据"),
                    @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
                    @ApiResponse(code = 500, message = "服务器不能完成请求")
            }
    )
    public Map<String,Object> addCart(Long pid, Integer pcount, String uuid) {

        String key = CART_INFO_PROFIX + uuid;
        String cartInfoString = null;
        try {
            cartInfoString = jedisClient.get(key);
        } catch (Exception e) {
            logger.error("Redis出错!", e);
        }

        TbItem item = null;

        String redisItem = null;
        try {
            redisItem = jedisClient.get(ITEM_INFO_PROFIX + pid + ITEM_INFO_BASE_SUFFIX);
        } catch (Exception e) {
            logger.error("Redis error", e);
        }

        if (StringUtils.isNotBlank(redisItem)) {
            item = (TbItem)JsonUtil.json2Object(redisItem, TbItem.class);

        } else {
            List<TbItem> itemList = null;

            try {
                itemList = itemMapper.selectItemByPid(pid);
            } catch (Exception e) {
                logger.error("select DB error", e);
            }

            if (itemList != null && itemList.size() > 0) {
                item = itemList.get(0);
            } else {
                return HaiResult.buildsucc("500", "商品查询不到!",item);
            }
        }

        CartInfo cartInfo = new CartInfo();

        cartInfo.setId(item.getId());
        cartInfo.setName(item.getTitle());
        String[] split = item.getImage().split(",");
        cartInfo.setImageUrl(split[0]);
        cartInfo.setColour("黑色");
        cartInfo.setNum(pcount);
        cartInfo.setPrice(item.getPrice());
        cartInfo.setSize("32GB");

        if (StringUtils.isBlank(cartInfoString)) {

            ArrayList<CartInfo> cartInfos = new ArrayList<>();

            cartInfos.add(cartInfo);

            logger.debug("第一次保存商品到Redis uuid:" + uuid);

            try {
                jedisClient.set(key, JsonUtil.write2JsonStr(cartInfos),REDIS_CART_EXPIRE_TIME);
            } catch (Exception e) {
                logger.error("Redis error", e);
            }

            return HaiResult.buildsucc("200", "ok", cartInfo);

        } else {
            List<CartInfo> list = FastJsonConvert.convertJSONToArray(cartInfoString, CartInfo.class);
            if (list != null && list.size() > 0) {
                boolean flag = true;
                for (int i = 0; i < list.size(); i++) {
                    CartInfo Info = list.get(i);
                    if (Info.getId().equals(item.getId())) {
                        Info.setNum(Info.getNum() + pcount);
                        list.remove(i);
                        list.add(Info);
                        flag = false;

                        logger.debug("商品已经存在数量加" + pcount + "个 uuid:" + uuid);
                    }
                }
                if (flag) {
                    list.add(cartInfo);
                    logger.debug("商品不存在数量为" + pcount + "个 uuid:" + uuid);
                }
            }

            logger.debug("商品添加完成 购物车" + list.size() + "件商品 uuid:" + uuid);

            try {
                jedisClient.set(key, FastJsonConvert.convertObjectToJSON(list),REDIS_CART_EXPIRE_TIME);
            } catch (Exception e) {
                logger.error("Redis出错!", e);
            }

            return HaiResult.buildsucc("200", "ok", cartInfo);
        }

    }
}
