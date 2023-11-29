package com.suxin.demo.sba.client.actuator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liqilin
 * @since 2023/11/23 17:28
 */
@Configuration
public class ThreadPoolHealthIndicatorConfig {

    private static final ExecutorService executor1 = Executors.newFixedThreadPool(5);
    private static final ExecutorService executor2 = Executors.newFixedThreadPool(10);

    /**
     * 访问： http://localhost:10015/actuator/health ，查看 threadPool
     */
    @Bean
    public ThreadPoolHealthIndicator threadPoolHealthIndicator() {
        final List<ThreadPoolHealthIndicator.ThreadPoolHolder> threadPoolHolders = Arrays.asList(
                new ThreadPoolHealthIndicator.ThreadPoolHolder((ThreadPoolExecutor) executor1, "master", "负责Job调度"),
                new ThreadPoolHealthIndicator.ThreadPoolHolder((ThreadPoolExecutor) executor2, "worker", "负责任务执行")
        );
        return new ThreadPoolHealthIndicator(threadPoolHolders);
    }

}
