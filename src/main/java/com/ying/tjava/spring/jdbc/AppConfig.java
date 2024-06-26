package com.ying.tjava.spring.jdbc;


import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @EnableTransactionManagement: 开启声明式事务，添加该注释后，不需要在添加@EnableAspectJAutoProxy
 * @Transactional: 通过在方法上添加注释，将该方法自动进行事务管理
 *
 * @MapperScan("com.ying.tjava.spring.jdbc"): 可以让MyBatis自动扫描指定包的所有Mapper并创建实现类
 *
 * spring 集成mybatis：
 *          1、创建 SqlSessionFactoryBean 的Bean方法，参考 com.ying.tjava.spring.jdbc.AppConfig#createSqlSessionFactoryBean(javax.sql.DataSource)
 *          2、创建Mapper接口
 *          3、添加 @MapperScan("com.ying.tjava.spring.jdbc") 注解
 *
 */

@ComponentScan
@Configuration
@PropertySource("classpath:/db.properties")
@EnableTransactionManagement
@MapperScan("com.ying.tjava.spring.jdbc")
public class AppConfig {
    @Value("${url}")
    private String url;

    @Value("${user}")
    private String userName;

    @Value("${password}")
    private String password;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Bean(name = "dataSource")
    public DataSource getDataSource() throws SQLException {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        ds.setAutoCommit(true);
        ds.setMaximumPoolSize(10);
        Connection conn = ds.getConnection();
        return ds;
    }

    @Bean("jdbcTemplate")
    public JdbcTemplate getJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * mybatis 数据源
     * @param dataSource
     * @return
     */
    @Bean
    public SqlSessionFactoryBean createSqlSessionFactoryBean(@Autowired DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    //  spring 事务管理器
    @Bean
    public PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
