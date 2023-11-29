/*
 * Copyright 2014-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.suxin.demo.sba.client.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 线程池状态监测
 * health indicator 形式不直观，可通过自定义endpoint结合自定义ui展示
 *
 * @author liqilin
 * @since 2021/9/26 11:42
 */
@Component
@Endpoint(id = "threadPool")
public class ThreadPoolEndpoint {

    @Autowired
    private ThreadPoolHealthIndicator threadPoolHealthIndicator;

    @ReadOperation
    public Map<String, Object> invoke() {
        return threadPoolHealthIndicator.health().getDetails();
    }

}
