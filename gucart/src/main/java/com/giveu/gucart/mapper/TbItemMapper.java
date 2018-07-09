package com.giveu.gucart.mapper;

import com.giveu.gucart.pojo.TbItem;

import java.util.List;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */

public interface TbItemMapper {
    /**
       *@Author: YinHai
       *@Descripation:根据产品id查询商品
       *@Date: Created in 17:23 2018/5/24
    */
    public List<TbItem> selectItemByPid(Long pid);


}
