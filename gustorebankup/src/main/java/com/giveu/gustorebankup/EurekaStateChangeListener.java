package com.giveu.gustorebankup;

import com.netflix.appinfo.InstanceInfo;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Component
public class EurekaStateChangeListener {
    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        System.err.println(event.getServerId() + "\t" + event.getAppName() + " 服务下线");
    }
    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        System.err.println(instanceInfo.getAppName() + "进行注册");
    }
    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        System.err.println(event.getServerId() + "\t" + event.getAppName() + " 服务进行续约");
    }
    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        System.err.println("注册中心 启动");
    }
    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        System.err.println("Eureka Server 启动");
    }
}
