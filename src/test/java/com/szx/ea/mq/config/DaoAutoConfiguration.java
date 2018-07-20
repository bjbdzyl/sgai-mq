package com.szx.ea.mq.config;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.szx.core.mybatis.mapper.MyBatisMapper;
import com.szx.core.mybatis.mapper.mapperhelper.MapperHelper;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)  
@PropertySource(value = { "classpath:datasource.properties" })
public class DaoAutoConfiguration implements EnvironmentAware{

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());

        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:/**/*Mapper.xml"));

            Resource configLocation = new ClassPathResource("mybatis-config.xml");
            sqlSessionFactoryBean.setConfigLocation(configLocation);

            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    @Bean
    public static MapperHelper mapperHelper() {
        return new MapperHelper();
    }

    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setMapperHelper(mapperHelper());
        
        String[] basePackages = new String[] {"com.szx.ea.mq.mapper"};
      
        mapperScannerConfigurer.setBasePackage(StringUtils.join(basePackages, ","));
        mapperScannerConfigurer.setMarkerInterface(MyBatisMapper.class);
        mapperScannerConfigurer.setProcessPropertyPlaceHolders(false);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return mapperScannerConfigurer;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driver"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){  
        DataSourceTransactionManager manager = new DataSourceTransactionManager();  
        manager.setDataSource(dataSource());  
        return manager;  
    }  
}
