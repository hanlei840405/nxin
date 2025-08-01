package com.nxin.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.message.listener.TopicShutdownListener;
import com.nxin.framework.message.listener.TopicTaskExecutedListener;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

@Configuration
public class BeanConfig {

    @Autowired
    private NxinMetaObjectHandler nxinMetaObjectHandler;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(5);
        // 设置最大线程数
        executor.setMaxPoolSize(20);
        //配置队列大小
        executor.setQueueCapacity(1000);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("NXIN");
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //执行初始化
        executor.initialize();
        return executor;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.getMessageConverters().add(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        //设置整个连接池最大连接数
        connectionManager.setMaxTotal(400);

        //路由是对maxTotal的细分
        connectionManager.setDefaultMaxPerRoute(100);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(1000 * 60 * 3)  //返回数据的超时时间,单位是毫秒： 这里设置为3分钟
                .setConnectTimeout(1000 * 60 * 5) //连接上服务器的超时时间 单位是毫秒： 这里设置为5分钟
                .setConnectionRequestTimeout(1000) //从连接池中获取连接的超时时间
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(nxinMetaObjectHandler);
        return globalConfig;
    }

    @Bean
    RedisMessageListenerContainer container(MessageListenerAdapter topicShutdownListenerAdapter, MessageListenerAdapter topicTaskExecutedSuccessListenerAdapter, MessageListenerAdapter topicTaskExecutedFailureListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(new MessageListenerAdapter(topicShutdownListenerAdapter), new PatternTopic(Constant.TOPIC_DESIGNER_SHUTDOWN));
        container.addMessageListener(new MessageListenerAdapter(topicTaskExecutedFailureListenerAdapter), new PatternTopic(Constant.TOPIC_TASK_FAILURE));
        container.addMessageListener(new MessageListenerAdapter(topicTaskExecutedSuccessListenerAdapter), new PatternTopic(Constant.TOPIC_TASK_SUCCESS));
        return container;
    }

    @Bean
    MessageListenerAdapter topicShutdownListenerAdapter(TopicShutdownListener topicShutdownListener) {
        return new MessageListenerAdapter(topicShutdownListener, "onMessage");
    }

    @Bean
    MessageListenerAdapter topicTaskExecutedSuccessListenerAdapter(TopicTaskExecutedListener topicTaskExecutedListener) {
        return new MessageListenerAdapter(topicTaskExecutedListener, "onSuccess");
    }

    @Bean
    MessageListenerAdapter topicTaskExecutedFailureListenerAdapter(TopicTaskExecutedListener topicTaskExecutedListener) {
        return new MessageListenerAdapter(topicTaskExecutedListener, "onFailure");
    }
}
