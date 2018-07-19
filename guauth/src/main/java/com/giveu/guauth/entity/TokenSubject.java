package com.giveu.guauth.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@TableName(value = "cs_subject")
public class TokenSubject  extends SuperEntity<TokenSubject>  {
   private String salt;
   private String subject;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "TokenSubject{" +
                "salt='" + salt + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
