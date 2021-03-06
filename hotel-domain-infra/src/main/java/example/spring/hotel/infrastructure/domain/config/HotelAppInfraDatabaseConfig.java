package example.spring.hotel.infrastructure.domain.config;

import example.spring.hotel.infrastructure.domain.mybatis.MyBatisRepositories;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackageClasses = {MyBatisRepositories.class})
@MapperScan(basePackageClasses = {MyBatisRepositories.class})
@EnableTransactionManagement    // @Transactional 어노테이션이 가능하도록
public class HotelAppInfraDatabaseConfig {
    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;
    @Value("${datasource.schema}")
    private String schema;

    @Bean
    public DataSource dataSoruce()  {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/" + schema + "?useUnicode=yes&characterEncoding=utf8");
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);

        return sqlSessionFactory.getObject();
    }
// Note. 해당 bean은 생성하지 않아도 spring mybatis starter에서 직접 생성하는 듯. 아래의 bean을 생성하면 같은 bean이 두개라며 에러가 남.
//    @Bean
//    public SqlSession sqlSession(SqlSessionFactory sqlSessionFactory)   {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource)   {
        return new DataSourceTransactionManager(dataSource);
    }
}
