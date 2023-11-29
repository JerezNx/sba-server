package com.suxin.demo.sba.client.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liqilin
 * @since 2023/11/16 14:04
 */
@RequestMapping("tmp")
@RestController
public class TmpController {

    @Autowired
    private ServerProperties serverProperties;

    @GetMapping(value = "get")
    public String get() {
        return "get: " + serverProperties.getPort();
    }

}
