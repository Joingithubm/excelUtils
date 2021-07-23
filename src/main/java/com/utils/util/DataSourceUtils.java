package com.utils.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import com.utils.bo.DataSourceBO;
import com.utils.enums.DataSourceEnum;
import com.utils.vo.DataSourceVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Description:
 * @Author Zengfc
 * @Date 2021/7/9 10:13
 * @Version 1.0
 */

@Data
@NoArgsConstructor
public class DataSourceUtils {


    public static DataSource druidDataSource(DataSourceBO dataSourceVo) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dataSourceVo.getUrl());
        dataSource.setUsername(dataSourceVo.getUsername());
        dataSource.setPassword(dataSourceVo.getPassword());
        dataSource.setDriverClassName(dataSourceVo.getDriverClassName());
        return dataSource;
    }


    public static SqlSessionTemplate mainSqlSessionTemplate(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));

/*        //分页插件
        Interceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        //数据库

        //是否将参数offset作为PageNum使用
        properties.setProperty("offsetAsPageNum", "false");
        //是否进行count查询
        properties.setProperty("rowBoundsWithCount", "true");
        //是否分页合理化
        properties.setProperty("reasonable", "false");
        interceptor.setProperties(properties);
        factoryBean.setPlugins(new Interceptor[]{interceptor});*/

        //  返回类型为map，值为null，字段不返回配置
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCallSettersOnNulls(true);
        factoryBean.setConfiguration(configuration);

        return new SqlSessionTemplate(factoryBean.getObject());
    }

}
