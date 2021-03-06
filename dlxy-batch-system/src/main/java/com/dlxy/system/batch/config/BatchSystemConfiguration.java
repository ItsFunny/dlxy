package com.dlxy.system.batch.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.commons.collections.functors.FalsePredicate;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ServletContextAware;
import org.xml.sax.helpers.DefaultHandler;

import com.alibaba.druid.pool.DruidDataSource;
import com.dlxy.common.event.AmqpListener;
import com.dlxy.common.event.AppEventLogPubliher;
import com.dlxy.common.event.AppEventPublisher;
import com.dlxy.common.event.AppEventRabbitMQPublisher;
import com.dlxy.common.event.Events;
import com.dlxy.system.batch.consumer.ArticleVistitCountListener;
import com.dlxy.system.batch.consumer.FacadedAmqpListener;
import com.dlxy.system.batch.consumer.UserIllegalLogListener;
import com.dlxy.system.batch.consumer.UserRecordListener;
import com.dlxy.system.batch.jobs.JobRunner;
import com.dlxy.system.batch.service.AbstractDeleteFileHandler;
import com.dlxy.system.batch.service.DefaultFileStrategyHandler;
import com.dlxy.system.batch.service.FTPFileStrategyHandler;
import com.dlxy.system.batch.service.IFileDeleteHandler;
import com.dlxy.system.batch.service.IRedisService;
import com.dlxy.system.batch.service.impl.RedisServiceImpl;
import com.joker.library.file.DefaultFileService;
import com.joker.library.file.FTPFileService;
import com.joker.library.file.FileStrategyContext;
import com.joker.library.file.IFileStrategy;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2018年6月29日 下午2:59:37
 */
@Configuration
@EnableConfigurationProperties(value =
        {DlxyProperty.class})
@ComponentScan(basePackages =
        {"com.dlxy.server"}, excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Mapper.class))
@MapperScan(basePackages =
        {"com.dlxy"}, annotationClass = Mapper.class)
public class BatchSystemConfiguration implements BeanFactoryAware, ServletContextAware
{
    private Logger logger = LoggerFactory.getLogger(BatchSystemConfiguration.class);

    private BeanFactory beanFactory;
    private ServletContext servletContext;

    @Autowired
    private DlxyProperty dlxyProperty;

    private Map<String, String> serverInfo;


    @Bean
    public IFileDeleteHandler handler()
    {
        AbstractDeleteFileHandler fileHandler = new FTPFileStrategyHandler(IFileDeleteHandler.FTP_TYPE);

        Map<String, String> ftpBasePathMap = new HashMap<>();
        Map<String, String> ftpVisitPrefixMap = new HashMap<>();
        ftpBasePathMap.put(IFileStrategy.IMG_TYPE, dlxyProperty.getImgFTPStoreBasePath());
        ftpVisitPrefixMap.put(IFileStrategy.IMG_TYPE, dlxyProperty.getImgFTPVisitPrefx());
        FTPFileService ftpFileService = new FTPFileService(dlxyProperty.getFtpHost(), dlxyProperty.getFtpPort(),
                dlxyProperty.getFtpUsername(), dlxyProperty.getFtpPassword());
        ftpFileService.setBasePathMap(ftpBasePathMap);
        ftpFileService.setVisitPrefixMap(ftpVisitPrefixMap);
        fileHandler.setFileStrategy(ftpFileService);

        AbstractDeleteFileHandler defaultHandler = new DefaultFileStrategyHandler(IFileDeleteHandler.NORMAL_TYPE);
        DefaultFileService defaultFileService = new DefaultFileService();
        Map<String, String> defaultBasePathMap = new HashMap<>();
        Map<String, String> defaultVisitPrefixMap = new HashMap<>();
        defaultVisitPrefixMap.put(IFileStrategy.IMG_TYPE, dlxyProperty.getImgLocalVisitPrefix());
        defaultFileService.setBasePathMap(defaultBasePathMap);
        defaultFileService.setVisitPrefixMap(defaultVisitPrefixMap);
        defaultHandler.setFileStrategy(defaultFileService);


        fileHandler.setNextHandler(defaultHandler);

        return fileHandler;
    }

    public String getByKey(String key)
    {
        String value = this.serverInfo.get(key);
        return value;
    }

    public void addKeyValue(String key, String value)
    {
        this.serverInfo.put(key, value);
    }

    @Bean
    public DefaultFileService localFileStrategyService()
    {
        Map<String, String> pathMap = new HashMap<String, String>();
        Map<String, String> visitPrefixMap = new HashMap<>();
        DefaultFileService defaultFileService = new DefaultFileService();
        visitPrefixMap.put(IFileStrategy.IMG_TYPE, "imgs");
        defaultFileService.setVisitPrefixMap(visitPrefixMap);
        defaultFileService.setBasePathMap(pathMap);
        return defaultFileService;
    }

    @Bean
    public FTPFileService ftpFileStrategyService()
    {
        Map<String, String> pathMap = new HashMap<String, String>();
        Map<String, String> visitPrefixMap = new HashMap<>();
        pathMap.put(IFileStrategy.IMG_TYPE, dlxyProperty.getImgFTPStoreBasePath());
        visitPrefixMap.put(IFileStrategy.IMG_TYPE, dlxyProperty.getImgFTPVisitPrefx());
        FTPFileService ftpFileService = new FTPFileService(dlxyProperty.getFtpHost(), dlxyProperty.getFtpPort(),
                dlxyProperty.getFtpUsername(), dlxyProperty.getFtpPassword());
        ftpFileService.setVisitPrefixMap(visitPrefixMap);
        ftpFileService.setBasePathMap(pathMap);

        return ftpFileService;
    }

    @Bean
    public FileStrategyContext fileStrategyContext()
    {
        FileStrategyContext context = new FileStrategyContext();

        return context;
    }

    @Bean
    public JedisPool jedisPool()
    {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMaxTotal(1024);
        jedisPoolConfig.setMaxWaitMillis(10000);
        jedisPoolConfig.setTestOnBorrow(true);
        // JedisPool jedisPool=new
//		JedisPool jedisPool = new JedisPool(jedisPoolConfig, dlxyProperty.getRedisHost(), dlxyProperty.getRedisPort(),
//				10000, dlxyProperty.getRedisPassword());
        JedisPool jedisPool = new JedisPool(dlxyProperty.getRedisHost(), dlxyProperty.getRedisPort());
        return jedisPool;
    }

    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

    @Bean
    public IRedisService redisService()
    {
        return new RedisServiceImpl();
    }

    @Bean
    public DataSource dataSource()
    {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(dlxyProperty.getDbUsername());
        dataSource.setPassword(dlxyProperty.getDbPassword());
        dataSource.setUrl(dlxyProperty.getDbUrl());
        dataSource.setDriverClassName(dlxyProperty.getDriverClassName());
        return dataSource;
    }

    @Bean
    public QueryRunner queryRunner()
    {
        return new QueryRunner(dataSource());
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws IOException
    {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean
                .setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return sqlSessionFactoryBean;
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public ConnectionFactory connectionFactory() throws Exception
    {
        RabbitConnectionFactoryBean rabbitConnectionFactoryBean = new RabbitConnectionFactoryBean();
        rabbitConnectionFactoryBean.setHost(dlxyProperty.getAmqpHost());
        rabbitConnectionFactoryBean.setUsername(dlxyProperty.getAmqpUsername());
        rabbitConnectionFactoryBean.setPassword(dlxyProperty.getAmqpPassword());
        rabbitConnectionFactoryBean.afterPropertiesSet();
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(
                rabbitConnectionFactoryBean.getObject());
        return cachingConnectionFactory;
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public RabbitTemplate rabbitTemplate() throws Exception
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory());
        rabbitTemplate.setExchange("dlxy");
        rabbitTemplate.setEncoding("UTF-8");
        // rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public RabbitAdmin rabbitAdmin() throws Exception
    {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        rabbitAdmin.declareExchange(dlxyExchange());
        return rabbitAdmin;
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public TopicExchange dlxyExchange()
    {
        return new TopicExchange("dlxy");
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public Queue userRecordQueue()
    {
        return new Queue("UserRecordLog");
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public Binding userRecordBinding()
    {
        Queue queue = userRecordQueue();
        return BindingBuilder.bind(queue).to(dlxyExchange()).with(Events.UserRecordLog.name().toUpperCase());
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public Queue userIllegalQueue()
    {
        return new Queue(Events.UserIllegalLog.name());
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public Binding userIllegalBinding()
    {
        Queue queue = userIllegalQueue();
        return BindingBuilder.bind(queue).to(dlxyExchange()).with(Events.UserIllegalLog.name().toUpperCase());
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public Queue articleVistUpdateQueue()
    {
        Queue queue = new Queue(Events.ArticleVisitCount.name());
        return queue;
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public Binding articleVisitCountBinding()
    {
        Queue queue = articleVistUpdateQueue();
        return BindingBuilder.bind(queue).to(dlxyExchange()).with(Events.ArticleVisitCount.name().toUpperCase());
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public AmqpListener userRecordListener()
    {
        return new UserRecordListener();
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public UserIllegalLogListener userIllegalLogListener()
    {
        return new UserIllegalLogListener();
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public ArticleVistitCountListener articleVistitCountListener()
    {
        return new ArticleVistitCountListener();
    }

    @ConditionalOnProperty(prefix = "dlxy.config", name = "amqp-enabled", matchIfMissing = false)
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(List<? extends AmqpListener> listeners)
            throws Exception
    {
        FacadedAmqpListener facadedAmqpListener = new FacadedAmqpListener(listeners);
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
        Set<String> queueNames = new HashSet<>();
        for (AmqpListener amqpListener : listeners)
        {
            queueNames.add(amqpListener.getQueueName());
        }
        String[] array = queueNames.toArray(new String[0]);
        System.out.println(array);
        // simpleMessageListenerContainer.setMessageConverter(messageConverter);
        simpleMessageListenerContainer.setQueueNames(queueNames.toArray(new String[0]));
        simpleMessageListenerContainer.setMaxConcurrentConsumers(3);
        simpleMessageListenerContainer.setConcurrentConsumers(2);
        MessageListenerAdapter adapter = new MessageListenerAdapter();
        adapter.setDelegate(facadedAmqpListener);
        adapter.setDefaultListenerMethod("process");
        simpleMessageListenerContainer.setMessageListener(adapter);
        return simpleMessageListenerContainer;
    }

    @Bean
    public AppEventPublisher appEventPublisher()
    {
        if (dlxyProperty.isAmqpEnabled())
        {
            return new AppEventRabbitMQPublisher();
        } else
        {
            return new AppEventLogPubliher();
        }
    }

    @PostConstruct
    public void initLog()
    {
        try
        {
            dlxyProperty.init();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        logger.info("{}", dlxyProperty);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }

    public JobRunner createBean(Class<? extends JobRunner> job)
    {
        return (JobRunner) ((AutowireCapableBeanFactory) beanFactory).createBean(job,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
    }

    public DlxyProperty getProperties()
    {
        return this.dlxyProperty;
    }

    @Override
    public void setServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }
}
