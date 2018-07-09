package com.giveu.gucart.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Configuration
public class Resolver {
    /**
       *@Author: YinHai
       *@Descripation:设置ThymeLeaf ViewReslover
       *@Date: Created in 10:23 2018/7/2
    */
//    @Autowired
//    SpringTemplateEngine templateEngine;
//    @Bean
//    public ViewResolver viewResolver( templateEngine){
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        viewResolver.setTemplateEngine(templateEngine);
//        return viewResolver;
//    }
//    @Bean
//    public TemplateEngine templateEngine(ITemplateResolver resolver ){
//        TemplateEngine engine = new TemplateEngine();
//        engine.setTemplateResolver(resolver);
//        return  engine
//    }
//    @Bean
//    public ITemplateResolver templateResolver(){
//        ITemplateResolver resolver=new ServletContextTemplateResolver();
//
//
//    }

}
