package com.suxin.demo.sba.client.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

/**
 * @author liqilin
 * @since 2023/11/27 10:08
 */
@Endpoint(id = "custom")
@Component
public class CustomEndpoint {

    @ReadOperation
    public String invoke() {
        return "Hello world";
    }

}
