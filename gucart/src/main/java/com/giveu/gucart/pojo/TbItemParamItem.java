package com.giveu.gucart.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class TbItemParamItem {
    private Long id;

    private Long itemId;

    private Date created;

    private Date updated;

    private String paramData;

}