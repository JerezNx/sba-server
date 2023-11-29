package com.suxin.demo.sba.client.actuator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liqilin
 * @since 2023/11/23 17:26
 */
public class ThreadPoolHealthIndicator implements HealthIndicator {

    /**
     * 需要监测的线程池
     */
    private List<ThreadPoolHolder> threadPools;

    public ThreadPoolHealthIndicator(List<ThreadPoolHolder> threadPools) {
        this.threadPools = threadPools;
    }

    @Override
    public Health health() {
        Map<String, ThreadPoolHealthDetail> details = new HashMap<>();
        for (ThreadPoolHolder threadPool : this.threadPools) {
            ThreadPoolHealthDetail detail = getThreadPoolHealthDetail(threadPool.getThreadPoolExecutor());
            detail.setDescription(threadPool.description);
            details.put(threadPool.getName(), detail);
        }
        return Health.up().withDetails(details).build();
    }

    /**
     * 获取线程池的实时属性
     *
     * @param threadPool 线程池
     * @return 监测属性
     */
    private ThreadPoolHealthDetail getThreadPoolHealthDetail(ThreadPoolExecutor threadPool) {
        int queueSize = threadPool.getQueue().size();
        int activeCount = threadPool.getActiveCount();
        long completedTaskCount = threadPool.getCompletedTaskCount();
        long taskCount = threadPool.getTaskCount();
        return ThreadPoolHealthDetail.builder()
                .activeCount(activeCount)
                .queueSize(queueSize)
                .completedTaskCount(completedTaskCount)
                .taskCount(taskCount).build();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class ThreadPoolHolder {
        private ThreadPoolExecutor threadPoolExecutor;
        private String name;
        private String description;
    }

    @Getter
    @Setter
    @Builder
    private static class ThreadPoolHealthDetail {
        private int activeCount;
        private int queueSize;
        private long completedTaskCount;
        private long taskCount;
        private String description;
    }
}
