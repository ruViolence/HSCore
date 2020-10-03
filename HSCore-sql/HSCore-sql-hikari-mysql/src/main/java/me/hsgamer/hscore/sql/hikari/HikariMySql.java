package me.hsgamer.hscore.sql.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.hsgamer.hscore.sql.Setting;
import me.hsgamer.hscore.sql.Sql;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariMySql implements Sql<HikariDataSource> {

  private final HikariDataSource hikariDataSource;

  public HikariMySql(Setting setting) {
    final HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:mysql://" + setting.getHost() + ':' + setting.getPort() + '/' + setting.getDatabaseName());
    config.setUsername(setting.getUsername());
    config.setPassword(setting.getPassword());
    config.setDriverClassName(setting.getDriver().isEmpty() ? "com.mysql.cj.jdbc.Driver" : setting.getDriver());
    config.addDataSourceProperty("cachePrepStmts", true);
    config.addDataSourceProperty("prepStmtCacheSize", 250);
    config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
    config.addDataSourceProperty("useServerPrepStmts", true);
    config.addDataSourceProperty("useLocalSessionState", true);
    config.addDataSourceProperty("rewriteBatchedStatements", true);
    config.addDataSourceProperty("cacheResultSetMetadata", true);
    config.addDataSourceProperty("cacheServerConfiguration", true);
    config.addDataSourceProperty("elideSetAutoCommits", true);
    config.addDataSourceProperty("maintainTimeStats", false);
    this.hikariDataSource = new HikariDataSource(config);
  }

  @Override
  public HikariDataSource getOriginal() {
    return hikariDataSource;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return hikariDataSource.getConnection();
  }
}
