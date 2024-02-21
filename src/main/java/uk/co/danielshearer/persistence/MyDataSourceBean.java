package uk.co.danielshearer.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class MyDataSourceBean {
  private final Logger logger = LoggerFactory.getLogger(MyDataSourceBean.class);

  private static final String DB_USER = "sa";
  private static final String DB_PASS = "s3cr3t";  // #Oops. Hard coded credentials

  @Bean
  public DataSource dataSource() throws SQLException {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.sqlite.JDBC");
    dataSource.setUrl("jdbc:sqlite:foo.db");
//    dataSource.setUrl("jdbc:sqlite::memory:");
    dataSource.setUsername(DB_USER);
    dataSource.setPassword(DB_PASS);

    final Connection connection = dataSource.getConnection();
    logger.info("Creating database table");
    try (final PreparedStatement statement = connection.prepareStatement(
      "create table if not exists hash_table(id integer primary key, input string, hash string)"
    )) {
      statement.executeUpdate();
    }

    return dataSource;
  }
}
