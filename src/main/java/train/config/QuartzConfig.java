package train.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class QuartzConfig {

    @Value("${threadpool.core-pool-size}")
    private int corePoolSize;

    @Value("${threadpool.max-pool-size}")
    private int maxPoolSize;

    @Value("${threadpool.queue-capacity}")
    private int queueCapacity;

    @Value("${threadpool.keep-alive-seconds}")
    private int keepAliveSeconds;


    @Bean("threadPoolTaskScheduler")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(corePoolSize);
        threadPoolTaskScheduler.setPoolSize(maxPoolSize);
        threadPoolTaskScheduler.setThreadGroupName("scheduled");
        threadPoolTaskScheduler.setThreadNamePrefix("scheduled-thread-");
        return threadPoolTaskScheduler;
    }

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);//核心线程池数
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize); // 最大线程
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);//队列容量
        threadPoolTaskExecutor.setThreadGroupName("executor");
        threadPoolTaskExecutor.setThreadNamePrefix("executor-thread-");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy()); //队列满，线程被拒绝执行策略
        return threadPoolTaskExecutor;
    }
}
