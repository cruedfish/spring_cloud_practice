package com.giveu.guauth.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@TableName(value = "cs_user")
public class User extends Model<User> {
    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private BigInteger id;

    @TableField("name")
    private String name;

    @TableField("password")
    private String password;

    @TableField("age")
    private String age;

    @TableField("sex")
    private String sex;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
