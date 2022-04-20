package com.asura.dao.datasource.first;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author asura7969
 * @create 2022-02-27-12:09
 */
@Configuration
@MapperScan(
        basePackages = "com.asura.dao.datasource.first.mapper",
        sqlSessionFactoryRef = "firstSqlSessionFactory")
public class FirstDataSourceConfig {

    @Bean(name = "transactionManager1")
    public DataSourceTransactionManager transactionManager(@Qualifier("firstDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("firstSqlSessionTemplate")
    public SqlSessionTemplate firstSqlSessionTemplate(
            @Qualifier("firstSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean("firstSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("firstDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

        configuration.setLogImpl(Slf4jImpl.class);
        // 开启驼峰命名
        configuration.setMapUnderscoreToCamelCase(true);
        // 开启在属性为null也调用setter方法
        configuration.setCallSettersOnNulls(true);

        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:odps/mapper/*Mapper*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("firstDataSource")
    public DataSource firstDataSource() {
        HikariDataSource druidDataSource = new HikariDataSource();
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druidDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/skeleton?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123456");
        return druidDataSource;
    }

}
